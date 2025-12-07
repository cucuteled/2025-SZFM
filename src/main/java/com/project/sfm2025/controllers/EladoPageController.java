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

    @PostMapping("/delete/{itemId}")
    public ResponseEntity<?> deleteItem(Authentication auth,
                                     @PathVariable("itemId") int itemId) {
        if (auth == null || auth.getName() == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }

        Food food = foodRepository.findById(itemId)
                .orElseThrow();
        if (food != null) foodRepository.delete(food);
        if (food == null) {
            Drink drink = drinkRepository.findById(itemId)
                    .orElseThrow();
            if (drink != null) drinkRepository.delete(drink);
            if (drink == null) {
                Menu menu = menuRepository.findById(itemId)
                        .orElseThrow();
                if (menu != null) menuRepository.delete(menu);
            }
        }

        return ResponseEntity.ok("!");
    }

    @PostMapping("/updateStatus/{orderId}/{newStatus}")
    public ResponseEntity<?> updateStatus(Authentication auth,
                                          @PathVariable("orderId") Long orderId,
                                          @PathVariable("newStatus") String newStatus) {
        if (auth == null || auth.getName() == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }

        OrderItem od = orderItemRepository.findById(orderId)
                        .orElseThrow();

        List<OrderItem> ods = orderItemRepository.findAllByOwnerAndOrderTime(od.getOwner(),od.getOrderTime());
        for (OrderItem o : ods) {
            o.setStatus(newStatus);
            orderItemRepository.save(o);
        }

        return ResponseEntity.ok("Státusz frissítve!");
    }


    private final FileService fileService;

    @PostMapping("/saveItem")
    public ResponseEntity<?> saveItem(Authentication auth,
                                      @RequestParam("name") String name,
                                      @RequestParam("price") int price,
                                      @RequestParam("description") String description,
                                      @RequestParam("type") String type,
                                      @RequestParam("id") Integer id,
                                      @RequestParam(value = "picture", required = false) MultipartFile picture) {
        if (auth == null || auth.getName() == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }

        String elado = sanitize(auth.getName());
        itemData item = new itemData(name, price, description, type, id);
        String itemNameOld = "";
        String itemNewName = "";
        boolean DoesItExist = false;

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

            // Kép mentése tételnévvel
            if (picture != null && !picture.isEmpty() && !DoesItExist) {
                if (!picture.getContentType().equals("image/jpeg")) {
                    return ResponseEntity.ok("exists");
                }
                try {
                    fileService.saveFileWithItemName(picture, name);
                } catch (Exception e) {
                    // semmi
                }

            }
            // kép felülmentése
            if (picture == null && DoesItExist) {
                fileService.renameFile(itemNameOld, itemNewName);
                // ha nem akarjuk hogy eltűnjön a korábbi név hivatkozások miatt
                //fileService.saveFileWithItemName(picture, itemNewName);
            }
            if (picture != null && DoesItExist)
            {
                try {
                    //fileService.renameFile(name, "old_" + name);
                    fileService.saveFileWithItemName(picture, name);
                } catch (Exception e) {
                    // semmi
                }
            }

            return ResponseEntity.ok("Sikeresen mentve!");

    }


}
