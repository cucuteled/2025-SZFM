package com.project.sfm2025.controllers;

import com.project.sfm2025.entities.Drink;
import com.project.sfm2025.entities.Food;
import com.project.sfm2025.entities.Menu;
import com.project.sfm2025.repositories.DrinkRepository;
import com.project.sfm2025.repositories.FoodRepository;
import com.project.sfm2025.repositories.MenuRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/menuk")
public class MenuController {

    private final MenuRepository menuRepository;
    private final FoodRepository foodRepository;
    private final DrinkRepository drinkRepository;

    public MenuController(MenuRepository menuRepository, FoodRepository foodRepository, DrinkRepository drinkRepository) {
        this.menuRepository = menuRepository;
        this.foodRepository = foodRepository;
        this.drinkRepository = drinkRepository;
    }

    @GetMapping
    public ResponseEntity<List<Menu>> getAll() {
        return ResponseEntity.ok(menuRepository.findAll());
    }

    @GetMapping("/{name}")
    public ResponseEntity<Menu> getMenu(@PathVariable String name) {
        Optional<Menu> menu = menuRepository.findByName(name);
        return menu.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //A food és drink résznél elég az id-t beírni, a többit kitölti
    @PostMapping
    public ResponseEntity<?> add(@RequestBody Menu menu) {
        List<Food> foods = menu.getFoods().stream()
                .map(f -> foodRepository.findById(f.getId())
                        .orElseThrow(() -> new RuntimeException("Food not found: " + f.getId())))
                .toList();

        List<Drink> drinks = menu.getDrinks().stream()
                .map(d -> drinkRepository.findById(d.getId())
                        .orElseThrow(() -> new RuntimeException("Drink not found: " + d.getId())))
                .toList();

        menu.setFoods(foods);
        menu.setDrinks(drinks);
        menuRepository.save(menu);
        return ResponseEntity.ok(menu);
    }

    //NOT WORKING!!!!
//    @PutMapping("/{name}")
//    public ResponseEntity<Menu> updateMenu(@PathVariable String name, @RequestBody Menu newMenu) {
//        return menuRepository.findByName(name)
//                .map(menu -> {
//                    menu.setName(newMenu.getName());
//                    menu.setPrice(newMenu.getPrice());
//                    menu.setOwner(newMenu.getOwner());
//
//                    List<Food> foods = newMenu.getFoods().stream()
//                            .map(f -> foodRepository.findById(f.getId())
//                                    .orElseThrow(() -> new RuntimeException("Food not found: " + f.getId())))
//                            .toList();
//
//                    List<Drink> drinks = newMenu.getDrinks().stream()
//                            .map(d -> drinkRepository.findById(d.getId())
//                                    .orElseThrow(() -> new RuntimeException("Drink not found: " + d.getId())))
//                            .toList();
//
//                    menu.setFoods(foods);
//                    menu.setDrinks(drinks);
//
//                    return ResponseEntity.ok(menuRepository.save(menu));
//                })
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenu(@PathVariable int id) {
        if (menuRepository.existsById(id)) {
            Menu menu = menuRepository.findById(id).orElseThrow();
            menuRepository.delete(menu);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
