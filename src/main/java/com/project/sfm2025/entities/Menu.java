package com.project.sfm2025.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Menu {

    @Id
    @GeneratedValue
    private int id;

    private String name;
    private int price;
    private String owner;

    @ManyToMany
    private List<Food> foods;

    @ManyToMany
    private List<Drink> drinks;

    public Menu() {}

    public Menu(String name, int price, String owner, List<Food> foods, List<Drink> drinks) {
        this.name = name;
        this.price = price;
        this.owner = owner;
        this.foods = foods;
        this.drinks = drinks;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public int getPrice() { return price; }

    public void setPrice(int price) { this.price = price; }

    public String getOwner() { return owner; }

    public void setOwner(String owner) { this.owner = owner; }

    public List<Food> getFoods() { return foods; }

    public void setFoods(List<Food> foods) { this.foods = foods; }

    public List<Drink> getDrinks() { return drinks; }

    public void setDrinks(List<Drink> drinks) { this.drinks = drinks; }
}
