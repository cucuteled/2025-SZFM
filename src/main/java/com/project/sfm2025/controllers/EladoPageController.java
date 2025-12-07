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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.lang.management.MemoryNotificationInfo;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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

    private final FileService fileService;

    @PostMapping("/saveItem")
    public ResponseEntity<?> saveItem(Authentication auth,
                                      @RequestParam("name") String name,
                                      @RequestParam("price") int price,
                                      @RequestParam("description") String description,
                                      @RequestParam("type") String type,
                                      @RequestParam("id") Integer id,
                                      @RequestParam("picture") MultipartFile picture) {
        if (auth == null || auth.getName() == null) {
            return ResponseEntity.status(401).body(Map.of("status", "error", "message", "Not authenticated"));
        }

        String elado = sanitize(auth.getName());

        // Ellenőrzés: létezik-e már tétel ezzel a névvel és típussal
        boolean exists = false;
        if (type.equals("Étel") && foodRepository.findByName(name).isPresent()) exists = true;
        if (type.equals("Ital") && drinkRepository.findByName(name).isPresent()) exists = true;
        if (type.equals("Menü") && menuRepository.findByName(name).isPresent()) exists = true;

        if (exists && id == -1) {
            return ResponseEntity
                    .ok(Map.of("status", "exists", "message", "Már létezik tétel ezzel a névvel"));
        }

        // Mentés vagy módosítás
        try {
            if (type.equals("Étel")) {
                Food obj = (id != -1) ? foodRepository.findById(id).orElse(new Food()) : new Food();
                obj.setOwner(elado);
                obj.setName(name);
                obj.setPrice(price);
                obj.setDescription(description);
                foodRepository.save(obj);
            } else if (type.equals("Ital")) {
                Drink obj = (id != -1) ? drinkRepository.findById(id).orElse(new Drink()) : new Drink();
                obj.setOwner(elado);
                obj.setName(name);
                obj.setPrice(price);
                obj.setDescription(description);
                drinkRepository.save(obj);
            } else if (type.equals("Menü")) {
                Menu obj = (id != -1) ? menuRepository.findById(id).orElse(new Menu()) : new Menu();
                obj.setOwner(elado);
                obj.setName(name);
                obj.setPrice(price);
                menuRepository.save(obj);
            }

            // Kép mentése
            if (picture != null && !picture.isEmpty()) {
                if (!picture.getContentType().equals("image/jpeg")) {
                    return ResponseEntity
                            .ok(Map.of("status", "error", "message", "Csak JPG képet lehet feltölteni"));
                }
                fileService.saveOriginalNameFile(picture); // vagy a megfelelő mentő függvényed
            }

            return ResponseEntity.ok(Map.of("status", "ok", "message", "Sikeresen mentve"));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("status", "error", "message", e.getMessage()));
        }
    }


}
