package com.project.sfm2025.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Drink
{
    private @Id
    @GeneratedValue int id;
    private String name;
    private int price;
    private String owner;

    public Drink()
    {

    }

    public Drink(int id, String name, int price, String owner)
    {
        this.id = id;
        this.name = name;
        this.price = price;
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
