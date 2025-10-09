package com.project.sfm2025.repositories;

import com.project.sfm2025.entities.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    // Kupon keresése a kódja és a tulajdonos email címe alapján
    Optional<Coupon> findByCodeAndOwnerEmail(String code, String ownerEmail);

    // Kupon keresése a kódja és a tulajdonos email címe alapján
    List<Coupon> findAllByOwnerEmail(String ownerEmail);

    // Kupon keresése kód alapján (használt/nem használt ellenőrzéshez)
    Optional<Coupon> findByCode(String code);
}