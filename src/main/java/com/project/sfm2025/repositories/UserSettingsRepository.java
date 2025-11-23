package com.project.sfm2025.repositories;

import com.project.sfm2025.entities.User;
import com.project.sfm2025.entities.UserSettings;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSettingsRepository extends JpaRepository<UserSettings, Integer> {
    Optional<UserSettings> findById(User user);
}
