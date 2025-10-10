package com.project.sfm2025.controllers;

import com.project.sfm2025.entities.CodeCoupon;
import com.project.sfm2025.entities.Coupon;
import com.project.sfm2025.repositories.CodeCouponRepository;
import com.project.sfm2025.repositories.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponRepository couponRepository;
    private final CodeCouponRepository codeCouponRepository;

    // Kuponok lekérése a profil oldalon
    @GetMapping("/my")
    public ResponseEntity<List<Coupon>> getMyCoupons(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        String email = auth.getName(); // Email a username/principal
        List<Coupon> coupons = couponRepository.findAllByOwnerEmailAndIsUsedFalse(email);
        return ResponseEntity.ok(coupons);
    }

    @PostMapping("/newCoupon")
    public ResponseEntity<?> createNewCoupon(@RequestBody CodeCoupon newCoupon, Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).body("Nem vagy bejelentkezve!");
        }

        String email = auth.getName();

        CodeCoupon nc = new CodeCoupon();
        nc.setKibocsato_owner(email);
        nc.setValidUntil(newCoupon.getValidUntil().plusDays(10));
        nc.setOsszeg(newCoupon.getOsszeg());
        nc.setCode(newCoupon.getCode());

        try {
            codeCouponRepository.save(nc);
            return ResponseEntity.ok("mentve");
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500).body("Hiba történt a kupon mentése közben.");

        }
    }


    @GetMapping("/addCoupon")
    public ResponseEntity<String> addCoupon(@RequestParam(name = "code") String code,
                                                    Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        String email = auth.getName();
        System.out.println(code);
        CodeCoupon kupon = codeCouponRepository.findByCode(code);

        if (kupon == null) {
            return ResponseEntity.status(401).body("A kupon nem található.");
        }

        if (kupon.getValidUntil().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(401).body("A kupon lejárt :(");
        }

        Coupon co = new Coupon();
        co.setCode(code);
        co.setOsszeg(kupon.getOsszeg());
        co.setOwnerEmail(email);
        co.setValidUntil(kupon.getValidUntil());

        Optional<Coupon> voltemar = couponRepository.findByCodeAndOwnerEmail(code, email);
        if (voltemar.isEmpty()) {
            couponRepository.save(co);
        } else {
            return ResponseEntity.status(401).body("Már felhasználtad");
        }

        return ResponseEntity.ok("Kupon aktiválva!");
    }

    @GetMapping("/usecoupon")
    public ResponseEntity<?> useCoupon(
            @RequestParam(name = "use") boolean usestate,
            @RequestParam(name = "code") String code,
            Authentication auth) {

        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        Optional<Coupon> cup = couponRepository.findByCodeAndOwnerEmail(code, auth.getName());
        if (cup.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Kupon nem található vagy nem a tiéd.");
        }

        Coupon coupon = cup.get();
        coupon.setPlannedToUse(usestate);

        couponRepository.save(coupon);

        return ResponseEntity.ok("Kupon állapot frissítve: " + usestate);
    }

}