package com.project.sfm2025.repositories;

import com.project.sfm2025.entities.Etterem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EtteremRepository extends JpaRepository<Etterem, Integer> {

    Optional<Etterem> findByUserEmail(String email);
}
