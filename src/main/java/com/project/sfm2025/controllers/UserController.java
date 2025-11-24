package com.project.sfm2025.controllers;

import com.project.sfm2025.entities.Etterem;
import com.project.sfm2025.repositories.EtteremRepository;
import com.project.sfm2025.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    /**
     * Kijelentkezés (JWT cookie törlése)
     */
//    @PostMapping("/logout")
//    public ResponseEntity<?> logout() {
//        ResponseCookie deleteCookie = ResponseCookie.from("jwt", "")
//                .httpOnly(true)
//                .secure(false)
//                .path("/")
//                .maxAge(0)
//                .sameSite("Lax")
//                .build();
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
//                .body("Logged out");
//    }

    /**
     * Fiók törlése (csak bejelentkezett felhasználó)
     */
    @DeleteMapping("/delete")
    @Transactional
    public ResponseEntity<?> deleteAccount(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }

        String email = authentication.getName();
        userRepository.deleteByEmail(email);

        // Töröljük a JWT sütit is
        ResponseCookie deleteCookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .body("Account deleted");
    }

    private final EtteremRepository etteremRepository;

    @GetMapping("/getEtterem/{user_name}")
    public ResponseEntity<?> getEtterem(Authentication auth,
                                        @PathVariable String user_name) {
        if (auth == null || auth.getName() == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }

        Optional<Etterem> etterem = etteremRepository.findByUserEmail(user_name);
        return etterem
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
