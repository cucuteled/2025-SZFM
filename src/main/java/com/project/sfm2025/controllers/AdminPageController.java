package com.project.sfm2025.controllers;

import com.project.sfm2025.entities.UserViewJson;
import com.project.sfm2025.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/")
@RequiredArgsConstructor
public class AdminPageController {

    // Repok
    private final UserRepository userRepository;

    // Felhasználók listázása
    @GetMapping("/getUsers")
    public ResponseEntity<?> listUsers(Authentication auth) {
        if (auth == null || auth.getName() == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }
        List<UserViewJson> users = userRepository.findAll().stream()
                .map(user -> new UserViewJson(user.getFirstname(), user.getLastname(), user.getEmail(), user.isAccountNonLocked(), user.getRole().toString()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

}
