package com.project.sfm2025.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class UserCartInfoJson {
    private String shippingAddress;
    private String billingAddress;
    private String phoneNumber;
    private String firstName;
    private String lastName;

    public UserCartInfoJson(String shippingAddress, String billingAddress, String phoneNumber, String firstName, String lastName) {
        this.shippingAddress = shippingAddress;
        this.billingAddress = billingAddress;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
