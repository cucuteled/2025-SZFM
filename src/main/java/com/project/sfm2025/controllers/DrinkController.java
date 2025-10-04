package com.project.sfm2025.controllers;

import com.project.sfm2025.entities.Drink;
import com.project.sfm2025.entities.Food;
import com.project.sfm2025.repositories.DrinkRepository;
import com.project.sfm2025.repositories.FoodRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/italok")
public class DrinkController
{
    private final DrinkRepository drinkRepository;

    DrinkController(DrinkRepository drinkRepository)
    {
        this.drinkRepository = drinkRepository;
    }

    @GetMapping
    public ResponseEntity<List<Drink>> getAll()
    {
        return ResponseEntity.ok(drinkRepository.findAll());
    }

    @GetMapping("/{name}")
    public ResponseEntity<Drink> getDrink(@PathVariable String name) {
        Optional<Drink> drink = drinkRepository.findByName(name);
        return drink.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody Drink drink)
    {
        if (drinkRepository.existsById(drink.getId()))
        {
            return ResponseEntity.status(409).build();
        }
        drinkRepository.save(drink);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDrink(@PathVariable int id)
    {
        if (drinkRepository.existsById(id)) {
            drinkRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{name}")
    public ResponseEntity<Drink> updateDrink(@PathVariable String name, @RequestBody Drink newDrink)
    {
        return drinkRepository.findByName(name)
                .map(food -> {
                    food.setName(newDrink.getName());
                    food.setPrice(newDrink.getPrice());
                    return ResponseEntity.ok(drinkRepository.save(food));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
