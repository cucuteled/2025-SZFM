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
    private Integer id;

    private String uzenet;
    private LocalDateTime ido;
    private String status;
    private String subject;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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


    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }


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
