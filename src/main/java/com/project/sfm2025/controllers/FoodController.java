package com.project.sfm2025.controllers;

import com.project.sfm2025.entities.Food;
import com.project.sfm2025.repositories.FoodRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/etelek")
public class FoodController
{
    private final FoodRepository foodRepository;

    FoodController(FoodRepository foodRepository)
    {
        this.foodRepository = foodRepository;
    }

    @GetMapping
    public ResponseEntity<List<Food>> getAll()
    {
        return ResponseEntity.ok(foodRepository.findAll());
    }

    @GetMapping("/{name}")
    public ResponseEntity<Food> getFood(@PathVariable String name) {
        Optional<Food> food = foodRepository.findByName(name);
        return food.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody Food food)
    {
        if (foodRepository.existsById(food.getId()))
        {
            return ResponseEntity.status(409).build();
        }
        foodRepository.save(food);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFood(@PathVariable int id)
    {
        if (foodRepository.existsById(id)) {
            foodRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{name}")
    public ResponseEntity<Food> updateFood(@PathVariable String name, @RequestBody Food newFood)
    {
        return foodRepository.findByName(name)
                .map(food -> {
                    food.setName(newFood.getName());
                    food.setPrice(newFood.getPrice());
                    return ResponseEntity.ok(foodRepository.save(food));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
