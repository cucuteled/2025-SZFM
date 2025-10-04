package com.project.sfm2025.repositories;

import com.project.sfm2025.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findAllByOwner(String owner);
    Optional<CartItem> findByOwnerAndProductId(String owner, String productId);
    void deleteByOwnerAndProductId(String owner, String productId);
    boolean existsByOwnerAndProductId(String owner, String productId);
    Optional<CartItem> findByOwnerAndProductIdAndType(String owner, String productId, String type);
}
