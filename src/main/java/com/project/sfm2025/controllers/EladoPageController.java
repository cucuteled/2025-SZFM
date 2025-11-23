package com.project.sfm2025.controllers;

import com.project.sfm2025.entities.*;
import com.project.sfm2025.repositories.DrinkRepository;
import com.project.sfm2025.repositories.FoodRepository;
import com.project.sfm2025.repositories.MenuRepository;
import com.project.sfm2025.repositories.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.lang.management.MemoryNotificationInfo;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

    @GetMapping("/{itemId}")
    public ResponseEntity<?> getItem(Authentication auth,
                                     @PathVariable("itemId") String itemName) {
        if (auth == null || auth.getName() == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }

        if (drinkRepository.findByName(itemName).isPresent()) {
            Drink drink = drinkRepository.findByName(itemName)
                    .orElseThrow();
            return ResponseEntity.ok(new itemData(drink.getName(),drink.getPrice(),drink.getDescription(),  "Ital", drink.getId()));
        }
        if (foodRepository.findByName(itemName).isPresent()) {
            Food food = foodRepository.findByName(itemName)
                    .orElseThrow();
            return ResponseEntity.ok(new itemData(food.getName(), food.getPrice(),food.getDescription(), "Étel", food.getId()));
        }
        if (menuRepository.findByName(itemName).isPresent()) {
            Menu menu = menuRepository.findByName(itemName)
                    .orElseThrow();
            return ResponseEntity.ok(new itemData(menu.getName(), menu.getPrice(),"",  "Menü", menu.getId()));
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/saveItem")
    public ResponseEntity<?> saveItem(Authentication auth,
                                      @RequestBody itemData item) {
        if (auth == null || auth.getName() == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }

        String elado = sanitize(auth.getName());
        String itemNameOld = "";
        String itemNewName = "";
        Boolean DoesItExist = false;

        // ÉTEL
        if (item.getType().equals("Étel")) {
            Food obj = null;
            if (item.getId() != -1) {
                obj = foodRepository.findById(item.getId())
                        .orElse(null);
            }
            if (obj == null) {
                obj = new Food();
                obj.setOwner(elado);
            } else {
                itemNameOld = obj.getName();
                itemNewName = item.getName();
                DoesItExist = true;
            }
            obj.setDescription(item.getDescription());
            obj.setName(item.getName());
            obj.setPrice(item.getPrice());
            foodRepository.save(obj);
        }
        // ITAL
        if (item.getType().equals("Ital")) {
            Drink obj = null;
            if (item.getId() != -1) {
                obj = drinkRepository.findById(item.getId())
                        .orElse(null);
            }
            if (obj == null) {
                obj = new Drink();
                obj.setOwner(elado);
            }else {
                itemNameOld = obj.getName();
                itemNewName = item.getName();
                DoesItExist = true;
            }
            obj.setDescription(item.getDescription());
            obj.setName(item.getName());
            obj.setPrice(item.getPrice());
            drinkRepository.save(obj);
        }
        // MENÜ
        if (item.getType().equals("Menü")) {
            Menu obj = null;
            if (item.getId() != -1) {
                obj = menuRepository.findById(item.getId())
                        .orElse(null);
            }
            if (obj == null) {
                obj = new Menu();
                obj.setOwner(elado);
            } else {
                itemNameOld = obj.getName();
                itemNewName = item.getName();
                DoesItExist = true;
            }
            obj.setName(item.getName());
            obj.setPrice(item.getPrice());
            menuRepository.save(obj);
        }
// todo_: class path resolver képek áthelyezése resources/uploads/ mappába
        // csak úgy képen a spring átnevezni FILE SERVICE + FILE CONTROLLER
//        if (DoesItExist) {
//
////            File file = new File("/static/pictures/" + itemNameOld + ".jpg");
////            file.renameTo(new File("/static/pictures/"+ itemNewName + ".jpg"));
////            System.out.println(file.getAbsolutePath());
//        }

        return ResponseEntity.ok("Sikeresen mentve!");
    }
}
