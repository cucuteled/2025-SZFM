package com.project.sfm2025.entities;

public class OrderData {
    private String orderfirstname;
    private String ordersecondname;
    private String address;
    private String billingAddress;
    private String phone;

    public String getName() {
        return orderfirstname + " " + ordersecondname;
    }

    // egyebek:

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
