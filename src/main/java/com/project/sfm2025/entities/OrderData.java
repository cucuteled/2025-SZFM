package com.project.sfm2025.entities;

import java.time.LocalDateTime;

public class OrderData {
    private String orderfirstname;
    private String ordersecondname;
    private String address;
    private String billingAddress;
    private String phone;
    private Boolean updateUserInfo;
    private String selectedPayment;
    private LocalDateTime scheduledDate;

    public String getName() {
        return orderfirstname + " " + ordersecondname;
    }



    // Getterek & Setterek:


    public LocalDateTime getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDateTime scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public String getSelectedPayment() { return selectedPayment; }

    public void setSelectedPayment(String selectedPayment) { this.selectedPayment = selectedPayment; }

    public Boolean getUpdateUserInfo() { return updateUserInfo; }

    public void setUpdateUserInfo(Boolean updateUserInfo) { this.updateUserInfo = updateUserInfo; }

    public String getOrderfirstname() {
        return orderfirstname;
    }

    public void setOrderfirstname(String orderfirstname) {
        this.orderfirstname = orderfirstname;
    }

    public String getOrdersecondname() {
        return ordersecondname;
    }

    public void setOrdersecondname(String ordersecondname) {
        this.ordersecondname = ordersecondname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
