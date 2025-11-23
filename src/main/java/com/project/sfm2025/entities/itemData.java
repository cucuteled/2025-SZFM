package com.project.sfm2025.entities;

public class itemData {
    private String name;
    private int price;
    private String description;
    private String type;
    private int id;

    public itemData(String name, int price, String description, String type, int id) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.type = type;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public int getId() {
        return id;
    }
}
