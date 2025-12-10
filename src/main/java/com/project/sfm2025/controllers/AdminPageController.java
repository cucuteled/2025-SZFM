package com.project.sfm2025.controllers;

import com.project.sfm2025.entities.*;
import com.project.sfm2025.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/")
@RequiredArgsConstructor
public class AdminPageController {

    // Repok
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ----------------------------
    // 1) Felhasználók listázása
    // ----------------------------
    @GetMapping("/getUsers")
    public ResponseEntity<?> listUsers(Authentication auth) {
        if (auth == null || auth.getName() == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }

        List<UserViewJson> users = userRepository.findAll().stream()
                .map(user -> new UserViewJson(
                        user.getId(),
                        user.getFirstname(),
                        user.getLastname(),
                        user.getEmail(),
                        user.isAccountNonLocked(),
                        user.getRole().toString()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }

    @PutMapping("/updateUser")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserRequest request) {

        // Keressük a usert az ID alapján
        var user = userRepository.findById(request.getId())
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        // Frissítjük az adatokat
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setEmail(request.getEmail()); // most már bátran módosítható
        user.setRole(Role.valueOf(request.getRole()));
        userRepository.save(user);

        return ResponseEntity.ok("User updated successfully");
    }


    // ----------------------------
// 3) Felhasználó törlése
// ----------------------------
    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {

        var user = userRepository.findById(id).orElse(null);

        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        userRepository.delete(user);
        return ResponseEntity.ok("User deleted successfully");
    }

    @DeleteMapping("/deleteUserSearch")
    public ResponseEntity<?> deleteUser(@RequestBody DeleteUserRequest req) {

        var user = userRepository.findAll().stream()
                .filter(u -> u.getFirstname().equals(req.getFirstname()))
                .filter(u -> u.getLastname().equals(req.getLastname()))
                .filter(u -> u.getEmail().equalsIgnoreCase(req.getEmail()))
                .filter(u -> u.getRole().toString().equalsIgnoreCase(req.getRole()))
                .findFirst()
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        userRepository.delete(user);
        return ResponseEntity.ok("User deleted successfully");
    }



    @PostMapping("/search")
    public ResponseEntity<?> searchUsers(@RequestBody SearchUserRequest request) {
        var allUsers = userRepository.findAll();

        var filtered = allUsers.stream()
                .filter(u -> request.getId() == null || u.getId().equals(request.getId()))
                .filter(u -> request.getFirstname() == null || u.getFirstname().toLowerCase().contains(request.getFirstname().toLowerCase()))
                .filter(u -> request.getLastname() == null || u.getLastname().toLowerCase().contains(request.getLastname().toLowerCase()))
                .filter(u -> request.getEmail() == null || u.getEmail().toLowerCase().contains(request.getEmail().toLowerCase()))
                .map(u -> new UserViewJson(
                        u.getId(),
                        u.getFirstname(),
                        u.getLastname(),
                        u.getEmail(),
                        u.isAccountNonLocked(),
                        u.getRole().toString()
                ))
                .toList();

        return ResponseEntity.ok(filtered);
    }

    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest req) {

        // 1. Kötelező mezők ellenőrzése
        if (req.getFirstname() == null || req.getFirstname().isBlank() ||
                req.getLastname() == null || req.getLastname().isBlank() ||
                req.getEmail() == null || req.getEmail().isBlank() ||
                req.getRole() == null || req.getRole().isBlank()) {

            return ResponseEntity
                    .status(400)
                    .body("All fields are required");
        }

        // 2. Duplikált e-mail ellenőrzés
        if (userRepository.existsByEmail(req.getEmail())) {
            return ResponseEntity
                    .status(409)           // 409 = Conflict
                    .body("Email already exists");
        }

        // 3. User létrehozása
        var user = new com.project.sfm2025.entities.User();

        user.setFirstname(req.getFirstname());
        user.setLastname(req.getLastname());
        user.setEmail(req.getEmail());

        try {
            user.setRole(Role.valueOf(req.getRole().toUpperCase()));
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Invalid role");
        }

        // 4. Default jelszó beállítása
        user.setPassword(passwordEncoder.encode("Default123"));

        // 5. Mentés
        userRepository.save(user);

        return ResponseEntity.ok("User created successfully");
    }




}
