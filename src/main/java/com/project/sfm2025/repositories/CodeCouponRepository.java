package com.project.sfm2025.repositories;

import com.project.sfm2025.entities.CodeCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CodeCouponRepository extends JpaRepository<CodeCoupon, Long> {
    List<CodeCoupon> findAll();
    CodeCoupon findByCode(String code);
}