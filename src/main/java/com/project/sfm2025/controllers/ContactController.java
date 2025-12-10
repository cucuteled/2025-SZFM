package com.project.sfm2025.controllers;

import com.project.sfm2025.entities.SupportMSG;
import com.project.sfm2025.repositories.SupportMSGRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


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
        private String status = "NEW";
        private String subject;


        // Getters & setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getSubject() { return subject; }
        public void setSubject(String subject) { this.subject = subject; }
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

        SupportMSG mi = new SupportMSG();
        mi.setFeladonev(msg.getName());
        mi.setFelado(msg.getEmail());
        mi.setUzenet(msg.getMessage());
        mi.setIdo(LocalDateTime.now());
        mi.setSubject(msg.getSubject());
        mi.setStatus("NEW");

        supportMSGRepository.save(mi);

        return new ContactResponse("success", "Üzenet fogadva");
    }


    @GetMapping("/admin/messages")
    public List<SupportMSG> getAllMessages() {
        return supportMSGRepository.findAll();
    }

    @PutMapping("/contact/{id}/status")
    public void changeStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        var msg = supportMSGRepository.findById(id).orElseThrow();
        msg.setStatus(body.get("status"));
        supportMSGRepository.save(msg);
    }

    @PostMapping("/contact/{id}/reply")
    public ContactResponse handleReply(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        String reply = body.get("reply");

        System.out.println("Válasz elküldve az üzenetre (ID: " + id + "): " + reply);

        // Később itt el lehet menteni az adatbázisba, ha akarjuk vagy email rendszert implementálni

        return new ContactResponse("Sikeres üzenet küldés.", "Válasz fogadva a szerveren");
    }




}
