package com.project.sfm2025.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    // Profil oldal
    @GetMapping("/myprofile")
    public String myProfile() {
        return "myprofile";
    }
    // rendelés leadás oldal
    @GetMapping("/checkout")
    public String OrderCheckOut() {
        return "checkout";
    }

    // Admin oldal
    @GetMapping("/admin")
    public String adminPage() {
        return "admin";
    }

    // eladói oldal
    @GetMapping("/elado")
    public String eladoPage() {
        return "elado";
    }
}
