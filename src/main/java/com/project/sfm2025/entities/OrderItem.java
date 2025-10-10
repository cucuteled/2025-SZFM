package com.project.sfm2025.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productId;
    private String name;
    private Integer price;
    private Integer quantity;

    private String order_name; // (VEZETÉKNÉV + " " + KERESZTNÉV)
    private String order_ShipAddress;
    private String order_BillingAddress;
    private String order_phonenumber;
    private String usedcoupons;

    public String getUsedcoupons() {
        return usedcoupons;
    }

    public void setUsedcoupons(String usedcoupons) {
        this.usedcoupons = usedcoupons;
    }

    public String getOrder_name() {
        return order_name;
    }

    public void setOrder_name(String order_name) {
        this.order_name = order_name;
    }

    public String getOrder_ShipAddress() {
        return order_ShipAddress;
    }

    public void setOrder_ShipAddress(String order_ShipAddress) {
        this.order_ShipAddress = order_ShipAddress;
    }

    public String getOrder_BillingAddress() {
        return order_BillingAddress;
    }

    public void setOrder_BillingAddress(String order_BillingAddress) {
        this.order_BillingAddress = order_BillingAddress;
    }

    public String getOrder_phonenumber() {
        return order_phonenumber;
    }

    public void setOrder_phonenumber(String order_phonenumber) {
        this.order_phonenumber = order_phonenumber;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    private String etelowner;

    public String getEtelowner() {
        return etelowner;
    }

    public void setEtelowner(String etelowner) {
        this.etelowner = etelowner;
    }

    private String owner;  // felhasználó
    private LocalDateTime orderTime;

    // getterek / setterek
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }
}
