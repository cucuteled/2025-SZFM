package com.project.sfm2025.controllers;

import com.project.sfm2025.entities.User;
import com.project.sfm2025.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class ProfileController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfileController(UserRepository userRepository,
                             PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateProfile(@RequestBody ProfileUpdateRequest req,
                                           @AuthenticationPrincipal UserDetails userDetails) {

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Felhasználó nem található"));

        user.setFirstname(req.getFirstname());
        user.setLastname(req.getLastname());

        if (req.getPassword() != null && !req.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(req.getPassword()));
        }

        userRepository.save(user);
        return ResponseEntity.ok("Profil frissítve");
    }

    @PostMapping("/changepassword")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest req,
                                            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow();

        if (!passwordEncoder.matches(req.getOldPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Hibás régi jelszó!");
        }

        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("OK");
    }
}

