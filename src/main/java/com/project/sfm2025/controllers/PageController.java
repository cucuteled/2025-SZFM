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
}
