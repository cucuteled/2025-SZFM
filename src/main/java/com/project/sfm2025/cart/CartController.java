package com.project.sfm2025.cart;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    @PostMapping("/add/{id}")
    public ResponseEntity<String> addToCart(@PathVariable String id, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Nem vagy bejelentkezve");
        }
        // ha szeretnéd, itt tudsz jogosultságot ellenőrizni:
        // authentication.getAuthorities()...
        String username = authentication.getName();
        // egyszerű tesztválasz:
        return ResponseEntity.ok("Hozzáadva a kosárhoz: " + id + " (user: " + username + ")");
    }
}
