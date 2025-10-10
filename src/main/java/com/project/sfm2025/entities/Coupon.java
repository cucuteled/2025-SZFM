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
    private int Osszeg; // kupon forintban
    private String ownerEmail; // A kupon tulajdonosának email címe
    private LocalDateTime validUntil; // Meddig érvényes
    private boolean plannedToUse; // akarja e használni avagy tervezi e a felhasználó következő rendelésénél felhasználni
    private boolean isUsed;

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public Coupon() {}

    public Coupon(String code, int Osszeg, String ownerEmail, LocalDateTime validUntil) {
        this.code = code;
        this.Osszeg = Osszeg;
        this.ownerEmail = ownerEmail;
        this.validUntil = validUntil;
        this.plannedToUse = false;
        this.isUsed = false;
    }

    public boolean isPlannedToUse() {
        return plannedToUse;
    }

    public void setPlannedToUse(boolean plannedToUse) {
        this.plannedToUse = plannedToUse;
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

    public int getOsszeg() {
        return Osszeg;
    }

    public void setOsszeg(int Osszeg) {
        this.Osszeg = Osszeg;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public LocalDateTime getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalDateTime validUntil) {
        this.validUntil = validUntil;
    }
}