package com.project.sfm2025.controllers;

import com.project.sfm2025.entities.SupportMSG;
import com.project.sfm2025.repositories.SupportMSGRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;



@RestController
@RequestMapping("/api")
public class ContactController {

    private final SupportMSGRepository supportMSGRepository;

    public ContactController(SupportMSGRepository supportMSGRepository) {
        this.supportMSGRepository = supportMSGRepository;
    }

    public static class ContactMessage {
        private String name;
        private String email;
        private String message;

        // Getters & setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

    public static class ContactResponse {
        private String status;
        private String details;

        public ContactResponse(String status, String details) {
            this.status = status;
            this.details = details;
        }

        public String getStatus() { return status; }
        public String getDetails() { return details; }
    }

    @PostMapping("/contact")
    public ContactResponse handleContact(@RequestBody ContactMessage msg) {
        System.out.println("Új kapcsolat üzenet: "
                + msg.getName() + " | " + msg.getEmail() + " | " + msg.getMessage());

        // Itt jöhetne: adatbázis mentés, email küldés stb
        SupportMSG mi = new SupportMSG();
        mi.setFeladonev(msg.getName());
        mi.setFelado(msg.getEmail());
        mi.setUzenet(msg.getMessage());
        mi.setIdo(LocalDateTime.now());

        supportMSGRepository.save(mi);

        return new ContactResponse("success", "Üzenet fogadva");
    }
}
