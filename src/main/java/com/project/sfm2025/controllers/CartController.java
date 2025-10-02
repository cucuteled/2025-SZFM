package com.project.sfm2025.controllers;

import com.project.sfm2025.entities.CartItem;
import com.project.sfm2025.entities.Food;
import com.project.sfm2025.entities.OrderItem;
import com.project.sfm2025.repositories.CartItemRepository;
import com.project.sfm2025.repositories.OrderItemRepository;
import com.project.sfm2025.repositories.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartItemRepository cartItemRepository;
    private final OrderItemRepository orderItemRepository;
    private final FoodRepository foodRepository;

    private String sanitize(String input) {
        return input.replaceAll("[^a-zA-Z0-9_]", "_");
    }

    @PostMapping("/add/{productId}")
    public ResponseEntity<String> addToCart(@PathVariable String productId, Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).body("Nem vagy bejelentkezve");
        }

        String username = sanitize(auth.getName());

        //  Lekérjük az ételt az adatbázisból
        Optional<Food> foodOptional = foodRepository.findById(Integer.parseInt(productId));
        if (foodOptional.isEmpty()) {
            return ResponseEntity.status(404).body("A megadott termék nem található");
        }

        Food food = foodOptional.get();

        // Megnézzük, van-e már ilyen tétel a kosárban
        Optional<CartItem> existingItemOpt = cartItemRepository.findByOwnerAndProductId(username, productId);

        if (existingItemOpt.isPresent()) {
            // Már van ilyen, növeljük a quantity-t
            CartItem existingItem = existingItemOpt.get();
            int currentQuantity = (existingItem.getQuantity() != null) ? existingItem.getQuantity() : 1;
            existingItem.setQuantity(currentQuantity + 1);
            cartItemRepository.save(existingItem);
            return ResponseEntity.ok("Mennyiség növelve: " + food.getName());
        }

        // Ha még nincs, újként hozzáadjuk
        CartItem item = new CartItem();
        item.setOwner(username);
        item.setProductId(productId);
        item.setName(food.getName());
        item.setPrice(food.getPrice());
        item.setQuantity(1);

        cartItemRepository.save(item);

        return ResponseEntity.ok("Hozzáadva a kosárhoz: " + food.getName());
    }


    @PostMapping("/remove/{productId}")
    public ResponseEntity<String> deleteFromCart(@PathVariable String productId, Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).body("Nem vagy bejelentkezve");
        }

        String username = sanitize(auth.getName());

        Optional<CartItem> itemOptional = cartItemRepository.findByOwnerAndProductId(username, productId);

        if (itemOptional.isEmpty()) {
            return ResponseEntity.status(404).body("A tétel nem található a kosárban.");
        }

        CartItem item = itemOptional.get();

        if (item.getQuantity() != null && item.getQuantity() > 1) {
            item.setQuantity(item.getQuantity() - 1);
            cartItemRepository.save(item);
        } else {
            cartItemRepository.delete(item);
        }

        return ResponseEntity.ok("Tétel csökkentve vagy törölve a kosárból: " + productId);
    }


    @GetMapping("/getcart")
    public ResponseEntity<List<CartItem>> getCart(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        String username = sanitize(auth.getName());
        return ResponseEntity.ok(cartItemRepository.findAllByOwner(username));
    }

    @PostMapping("/order")
    public ResponseEntity<String> order(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).body("Nem vagy bejelentkezve");
        }

        String username = sanitize(auth.getName());
        List<CartItem> cartItems = cartItemRepository.findAllByOwner(username);

        if (cartItems.isEmpty()) {
            return ResponseEntity.ok("A kosarad üres.");
        }

        for (CartItem ci : cartItems) {
            OrderItem oi = new OrderItem();
            oi.setOwner(username);
            oi.setProductId(ci.getProductId());
            oi.setName(ci.getName());
            oi.setPrice(ci.getPrice());
            oi.setOrderTime(LocalDateTime.now());
            oi.setQuantity(ci.getQuantity());
            oi.setAddress("Alapértelmezett cím"); // később módosítható

            orderItemRepository.save(oi);
        }

        cartItemRepository.deleteAll(cartItems);

        return ResponseEntity.ok("Rendelés sikeresen leadva.");
    }
}
