package com.project.sfm2025.repositories;

import com.project.sfm2025.entities.OrderItem;
import com.project.sfm2025.entities.SupportMSG;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupportMSGRepository extends JpaRepository<SupportMSG, Integer>{
    List<SupportMSG> findAllByFelado(String felado);
}
