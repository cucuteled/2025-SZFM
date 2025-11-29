package com.project.sfm2025.entities;

import lombok.Data;

    @Data
    public class UpdateUserRequest {
        private Integer id;
        private String firstname;
        private String lastname;
        private String email;
        private String role;
        private boolean enabled;

        // getter / setter
    }


