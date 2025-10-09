package com.project.sfm2025.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code; // Kupon kód, pl. "WELCOME50"
    private int discountPercent; // Kedvezmény %-ban (pl. 50)
    private String ownerEmail; // A kupon tulajdonosának email címe
    private boolean used; // Felhasználták-e már
    private LocalDateTime validUntil; // Meddig érvényes (opcionális, most nem használjuk)

    public Coupon() {}

    public Coupon(String code, int discountPercent, String ownerEmail, boolean used) {
        this.code = code;
        this.discountPercent = discountPercent;
        this.ownerEmail = ownerEmail;
        this.used = used;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public LocalDateTime getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalDateTime validUntil) {
        this.validUntil = validUntil;
    }
}