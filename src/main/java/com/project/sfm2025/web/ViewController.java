package com.project.sfm2025.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/register")
    public String register() {
        // visszaadjuk a Thymeleaf sablont: src/main/resources/templates/register.html
        return "register";
    }

    @GetMapping("/login")
    public String login() {
        // visszaadjuk a Thymeleaf sablont: src/main/resources/templates/login.html
        return "login";
    }

    @GetMapping("/")
    public String index() {
        // ha az index.html a templates mappában van: visszaadjuk az index sablont
        return "index";
    }
}
