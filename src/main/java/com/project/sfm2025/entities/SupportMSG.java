package com.project.sfm2025.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class SupportMSG {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String felado;
    private String feladonev;

    public String getFeladonev() {
        return feladonev;
    }

    public void setFeladonev(String feladonev) {
        this.feladonev = feladonev;
    }

    private String uzenet;
    private LocalDateTime ido;

    public String getFelado() {
        return felado;
    }

    public void setFelado(String felado) {
        this.felado = felado;
    }

    public String getUzenet() {
        return uzenet;
    }

    public void setUzenet(String uzenet) {
        this.uzenet = uzenet;
    }

    public LocalDateTime getIdo() {
        return ido;
    }

    public void setIdo(LocalDateTime ido) {
        this.ido = ido;
    }


}
