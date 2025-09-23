package com.project.sfm2025.repositories;

import com.project.sfm2025.entities.Food;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FoodRepository extends JpaRepository<Food, Integer>
{
    Optional<Food> findByName(String name);
}
