package com.project.sfm2025.controllers;

import com.project.sfm2025.entities.Coupon;
import com.project.sfm2025.repositories.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponRepository couponRepository;

    // Kuponok lekérése a profil oldalon
    @GetMapping("/my")
    public ResponseEntity<List<Coupon>> getMyCoupons(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        String email = auth.getName(); // Email a username/principal
        List<Coupon> coupons = couponRepository.findAllByOwnerEmailAndUsedFalse(email);
        return ResponseEntity.ok(coupons);
    }
}