package com.project.sfm2025.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class CodeCoupon {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String code;
    private String kibocsato_owner;
    private LocalDateTime validUntil;
    private Integer Osszeg;

//    public CodeCoupon(String code, String kibocsato_owner, LocalDateTime validUntil, Integer Osszeg) {
//        this.code = code;
//        this.kibocsato_owner = kibocsato_owner;
//        this.validUntil = validUntil;
//        this.Osszeg = Osszeg;
//    }

    public Integer getOsszeg() {
        return Osszeg;
    }

    public void setOsszeg(Integer osszeg) {
        Osszeg = osszeg;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getKibocsato_owner() {
        return kibocsato_owner;
    }

    public void setKibocsato_owner(String kibocsato_owner) {
        this.kibocsato_owner = kibocsato_owner;
    }

    public LocalDateTime getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalDateTime validUntil) {
        this.validUntil = validUntil;
    }
}
