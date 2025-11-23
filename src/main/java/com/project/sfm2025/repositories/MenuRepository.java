package com.project.sfm2025.repositories;

import com.project.sfm2025.entities.Food;
import com.project.sfm2025.entities.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Integer> {
    Optional<Menu> findByName(String name);
    List<Menu> findAllByOwner(String name);
}