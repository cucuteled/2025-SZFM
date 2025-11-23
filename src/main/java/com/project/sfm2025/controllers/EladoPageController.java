package com.project.sfm2025.controllers;

import com.project.sfm2025.entities.Drink;
import com.project.sfm2025.entities.Food;
import com.project.sfm2025.entities.Menu;
import com.project.sfm2025.entities.OrderItem;
import com.project.sfm2025.repositories.DrinkRepository;
import com.project.sfm2025.repositories.FoodRepository;
import com.project.sfm2025.repositories.MenuRepository;
import com.project.sfm2025.repositories.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/elado/")
@RequiredArgsConstructor
public class EladoPageController {

    private final FoodRepository foodRepository;
    private final DrinkRepository drinkRepository;
    private final MenuRepository menuRepository;
    private final OrderItemRepository orderItemRepository;

    private String sanitize(String input) {
        return input.replaceAll("[^a-zA-Z0-9_]", "_");
    }

    // vissza adja a food, drink, menu táblából az összes példányt melyhez köze van az eladónak
    @GetMapping("/myitems")
    public ResponseEntity<?> myitems(Authentication auth) {
        if (auth == null || auth.getName() == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }
        List<Object> returnItems = new ArrayList<>();

        String owner = sanitize(auth.getName());

        returnItems.addAll(foodRepository.findAllByOwner(owner));
        returnItems.addAll(drinkRepository.findAllByOwner(owner));
        returnItems.addAll(menuRepository.findAllByOwner(owner));

        if (returnItems.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(returnItems);
    }

    // Eladói oldalon vissza adja az összes Rendelést melyhez közve van az eladónak
    @GetMapping("/getOrders")
    public ResponseEntity<?> getOrders(Authentication auth) {
        if (auth == null || auth.getName() == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }
        List<OrderItem> items = new ArrayList<>(orderItemRepository.findAllByEtelowner(sanitize(auth.getName())));
        if (items.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(items);
    }
}
