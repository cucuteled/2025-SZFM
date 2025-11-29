package com.project.sfm2025.entities;

import lombok.Data;

@Data
public class CreateUserRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String role;
}
