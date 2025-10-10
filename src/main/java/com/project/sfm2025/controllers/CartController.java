package com.project.sfm2025.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.project.sfm2025.entities.*;
import com.project.sfm2025.repositories.*;
import io.swagger.v3.core.util.Json;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.hibernate.query.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartItemRepository cartItemRepository;
    private final OrderItemRepository orderItemRepository;
    private final FoodRepository foodRepository;
    private final DrinkRepository drinkRepository;
    private final CouponRepository couponRepository;

    private String sanitize(String input) {
        return input.replaceAll("[^a-zA-Z0-9_]", "_");
    }

    @PostMapping("/add/{productId}")
    public ResponseEntity<String> addToCart(
            @PathVariable String productId,
            @RequestParam String type,
            Authentication auth) {

        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).body("Nem vagy bejelentkezve");
        }

        String username = sanitize(auth.getName());

        String productName;
        int price;

        if ("etel".equalsIgnoreCase(type)) {
            Optional<Food> foodOpt = foodRepository.findById(Integer.parseInt(productId));
            if (foodOpt.isEmpty()) return ResponseEntity.status(404).body("A megadott étel nem található");
            Food food = foodOpt.get();
            productName = food.getName();
            price = food.getPrice();

        } else if ("ital".equalsIgnoreCase(type)) {
            Optional<Drink> drinkOpt = drinkRepository.findById(Integer.parseInt(productId));
            if (drinkOpt.isEmpty()) return ResponseEntity.status(404).body("A megadott ital nem található");
            Drink drink = drinkOpt.get();
            productName = drink.getName();
            price = drink.getPrice();

        } else {
            return ResponseEntity.badRequest().body("Ismeretlen terméktípus: " + type);
        }

        Optional<CartItem> existingItemOpt = cartItemRepository.findByOwnerAndProductIdAndType(username, productId, type);

        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            int currentQuantity = (existingItem.getQuantity() != null) ? existingItem.getQuantity() : 1;
            existingItem.setQuantity(currentQuantity + 1);
            cartItemRepository.save(existingItem);
            return ResponseEntity.ok("Mennyiség növelve: " + productName);
        }

        CartItem item = new CartItem();
        item.setOwner(username);
        item.setProductId(productId);
        item.setName(productName);
        item.setPrice(price);
        item.setQuantity(1);
        item.setType(type);

        cartItemRepository.save(item);

        return ResponseEntity.ok("Hozzáadva a kosárhoz: " + productName);
    }


    @PostMapping("/remove/{productId}")
    public ResponseEntity<String> deleteFromCart(@PathVariable String productId,@RequestParam String type, Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).body("Nem vagy bejelentkezve");
        }

        String username = sanitize(auth.getName());

        Optional<CartItem> itemOptional = cartItemRepository.findByOwnerAndProductIdAndType(username, productId, type);

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

    private List<Coupon> selectCoupons(List<Coupon> coupons, Integer ordertotal) {
        coupons.sort(Comparator.comparing(Coupon::getOsszeg));
        List<Coupon> sendBack = new ArrayList<>();
        for (Coupon cup : coupons) { // addig válogatjuk a kuponokat amíg az 0 alá nem esik
            if (ordertotal - cup.getOsszeg() >= 0) {
                ordertotal -= cup.getOsszeg();
                sendBack.add(cup);
            } else {
                sendBack.add(cup); // hozzáadjuk az utolsót is így akár 0 alá is eshet DE a végösszeg sosem -, hanem 0!
                break;
            }
        }
        return sendBack;
    }

    @GetMapping("/getcupons")
    public ResponseEntity<List<Coupon>> getCoupon(@RequestParam Integer total,
                                                  Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        String username = auth.getName();
        List<Coupon> coupons = couponRepository.findAllByOwnerEmailAndIsUsedFalseAndPlannedToUseIsTrue(username);
        List<Coupon> sendBack = selectCoupons(coupons, total);

        // Szállítási díj kupon hozzáadása (-1 ID-val)
        Coupon shippingFee = new Coupon();
        shippingFee.setId((long)-1L);
        shippingFee.setCode("SHIPPING");
        shippingFee.setOsszeg(990); // például 990 Ft szállítási díj
        shippingFee.setOwnerEmail(username); // opcionális, ha szükséges

        sendBack.add(0, shippingFee); // első elemként hozzáadjuk

        return ResponseEntity.ok(sendBack);
    }

    @PostMapping("/order")
    public ResponseEntity<String> order(Authentication auth,
                                        @RequestBody OrderData data
    ) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).body("Nem vagy bejelentkezve");
        }

        String username = sanitize(auth.getName());
        List<CartItem> cartItems = cartItemRepository.findAllByOwner(username);

        if (cartItems.isEmpty()) {
            return ResponseEntity.ok("A kosarad üres.");
        }

        // kuponok lekérése

        Integer total = 0;
        for (CartItem ci : cartItems) {
            total += ci.getPrice();
        }
        List<Coupon> coupons = couponRepository.findAllByOwnerEmailAndIsUsedFalseAndPlannedToUseIsTrue(auth.getName());
        List<Coupon> usedcoupons = selectCoupons(coupons, total);

        Integer shippingFee = 990;
        String usedcouponsString = shippingFee.toString() + ";";
        for (Coupon cup : usedcoupons) {
            usedcouponsString += cup.getCode() + "," + cup.getOsszeg() + ";";
        } // az összes felhasznált kupont mentjük így -> "kupon1;kupon2..." kódja alapján

        for (Coupon coupon : usedcoupons) {
            coupon.setUsed(true);
        }
        couponRepository.saveAll(usedcoupons);

        for (CartItem ci : cartItems) {
            OrderItem oi = new OrderItem();
            oi.setOwner(username);
            oi.setProductId(ci.getProductId());
            oi.setName(ci.getName());
            oi.setPrice(ci.getPrice());
            oi.setOrderTime(LocalDateTime.now());
            oi.setQuantity(ci.getQuantity());

            if ("etel".equalsIgnoreCase(ci.getType())) {
                foodRepository.findByName(ci.getName())
                        .ifPresent(food -> oi.setEtelowner(food.getOwner()));
            } else if ("ital".equalsIgnoreCase(ci.getType())) {
                drinkRepository.findByName(ci.getName())
                        .ifPresent(drink -> oi.setEtelowner(drink.getOwner()));
            }

            oi.setOrder_ShipAddress(data.getAddress());
            oi.setOrder_BillingAddress(data.getBillingAddress());
            //
            oi.setOrder_phonenumber(data.getPhone());
            oi.setOrder_name(data.getName());
            //
            oi.setUsedcoupons(usedcouponsString); // itt mentjük a felhasznált kuponokat

            orderItemRepository.save(oi);
        }

        cartItemRepository.deleteAll(cartItems);

        return ResponseEntity.ok("Rendelés sikeresen leadva.");
    }

    @GetMapping("/myorders")
    public ResponseEntity<List<OrderItem>> getMyOrderItems(Authentication auth)
    {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        String username = sanitize(auth.getName());
        List<OrderItem> orderek = orderItemRepository.findAllByOwner(username);
        orderek.sort(Comparator.comparing(OrderItem::getOrderTime));

        return ResponseEntity.ok(orderek);
    }
}
