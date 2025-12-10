package com.project.sfm2025.controllers;

import com.project.sfm2025.entities.OrderItem;
import com.project.sfm2025.entities.Role;
import com.project.sfm2025.entities.User;
import com.project.sfm2025.repositories.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/")
@RequiredArgsConstructor
public class StatisticsController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private DrinkRepository drinkRepository;

    private final OrderItemRepository orderItemRepository;

    @GetMapping("/usersByRole")
    public ResponseEntity<?> getUsersByRole() {
        var allUsers = userRepository.findAll();

        Map<Role, Long> countByRole = allUsers.stream()
                .collect(Collectors.groupingBy(User::getRole, Collectors.counting()));

        Map<String, Long> result = new LinkedHashMap<>();
        for (Role role : Role.values()) {
            result.put(role.name(), countByRole.getOrDefault(role, 0L));
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/menuFoodDrinkCount")
    public ResponseEntity<?> getMenuFoodDrinkCount() {
        Map<String, Long> result = new LinkedHashMap<>();
        result.put("Menük", menuRepository.count());
        result.put("Ételek", foodRepository.count());
        result.put("Italok", drinkRepository.count());

        return ResponseEntity.ok(result);
    }

    @Data
    @AllArgsConstructor
    static class ProductCountResponse {
        private String name;
        private Integer count;
    }

    @GetMapping("/top5Foods")
    public List<ProductCountResponse> getTop5Foods() {
        return getTop5ByCategory("FOOD");
    }

    @GetMapping("/top5Drinks")
    public List<ProductCountResponse> getTop5Drinks() {
        return getTop5ByCategory("DRINK");
    }

    @GetMapping("/top5Menus")
    public List<ProductCountResponse> getTop5Menus() {
        return getTop5ByCategory("MENU");
    }

    private List<ProductCountResponse> getTop5ByCategory(String category) {
        List<OrderItem> items = orderItemRepository.findAll();

        Map<String, String> foodMap = foodRepository.findAll().stream()
                .collect(Collectors.toMap(f -> Integer.toString(f.getId()), f -> f.getName()));

        Map<String, String> drinkMap = drinkRepository.findAll().stream()
                .collect(Collectors.toMap(d -> Integer.toString(d.getId()), d -> d.getName()));

        Map<String, String> menuMap = menuRepository.findAll().stream()
                .collect(Collectors.toMap(m -> Integer.toString(m.getId()), m -> m.getName()));


        Map<String, Integer> counts = new HashMap<>();

        for (OrderItem item : items) {
            if (item.getProductId() == null || item.getQuantity() == null) continue;

            String name = null;

            switch (category) {
                case "FOOD":
                    name = foodMap.get(item.getProductId());
                    break;
                case "DRINK":
                    name = drinkMap.get(item.getProductId());
                    break;
                case "MENU":
                    name = menuMap.get(item.getProductId());
                    break;
            }

            if (name != null) {
                counts.merge(name, item.getQuantity(), Integer::sum);
            }
        }

        return counts.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .map(e -> new ProductCountResponse(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }


}




