package com.project.sfm2025.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Food
{
    private @Id
    @GeneratedValue int id;
    private String name;
    private int price;

    public Food()
    {

    }

    Food(String name, int price) {

        this.name = name;
        this.price = price;
    }

    public int getId()
    {
        return this.id;
    }
    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getPrice()
    {
        return this.price;
    }

    public void setPrice(int price)
    {
        this.price = price;
    }
}
