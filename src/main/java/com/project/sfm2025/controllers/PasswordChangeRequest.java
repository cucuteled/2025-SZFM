package com.project.sfm2025.controllers;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PasswordChangeRequest {

    private String oldPassword;
    private String newPassword;

}

