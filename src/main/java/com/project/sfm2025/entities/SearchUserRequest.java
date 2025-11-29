package com.project.sfm2025.entities;

import lombok.Data;

    @Data
    public class SearchUserRequest {
        private Integer id;
        private String firstname;
        private String lastname;
        private String email;
    }

