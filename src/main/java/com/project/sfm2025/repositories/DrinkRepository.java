package com.project.sfm2025.repositories;

import com.project.sfm2025.entities.Drink;
import com.project.sfm2025.entities.Food;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DrinkRepository extends JpaRepository<Drink, Integer> {
    Optional<Drink> findByName(String name);
}
