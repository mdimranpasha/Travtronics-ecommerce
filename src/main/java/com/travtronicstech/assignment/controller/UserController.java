package com.travtronicstech.assignment.controller;

import com.travtronicstech.assignment.model.CartItem;
import com.travtronicstech.assignment.model.Order;
import com.travtronicstech.assignment.model.Users;
import com.travtronicstech.assignment.repo.ProductRepo;
import com.travtronicstech.assignment.repo.UserRepo;
import com.travtronicstech.assignment.service.OrderService;
import com.travtronicstech.assignment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    // POST endpoint to save user data
    @PostMapping("/save")
    public Users saveUser(@RequestBody Users user) {
        user.setPassword(encoder.encode(user.getPassword()));
        System.out.println(user);
        return userService.saveUser(user);
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Users user) {
        System.out.println("imran");
        return userService.verify(user);
    }

    // Add product to the user's cart
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/add-to-cart")
    public Users addToCart(@RequestBody Map<String, Object> request) {
        UUID userId = UUID.fromString((String) request.get("userId"));
        UUID productId = UUID.fromString((String) request.get("productId"));
        int quantity = (int) request.get("quantity");

        Users user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // Check if product already in cart, then update quantity
        boolean productExistsInCart = false;
        for (CartItem item : user.getCart()) {
            if (item.getProductId().equals(productId)) {
                item.setQuantity(item.getQuantity() + quantity); // Update quantity
                productExistsInCart = true;
                break;
            }
        }

        // If product does not exist, add new cart item
        if (!productExistsInCart) {
            CartItem cartItem = new CartItem(productId, quantity);
            user.getCart().add(cartItem);
        }

        return userRepo.save(user);
    }


    @PreAuthorize("hasRole('USER')")
    @GetMapping("/cart/{userId}")
    public ResponseEntity<List<CartItem>> getCartItems(@PathVariable UUID userId) {
        Users user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        List<CartItem> cartItems = user.getCart();

        // Ensure you return the updated cart items after any deletions/updates
        return ResponseEntity.ok(cartItems);
    }



    // Delete product from cart (User only)
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{userId}/cart/{productId}")
    public ResponseEntity<Map<String, String>> removeFromCart(@PathVariable UUID userId, @PathVariable UUID productId) {
        Users user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // If the cart is empty, return a message indicating no products to delete
        if (user.getCart().isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "No products in the cart to delete");
            return ResponseEntity.ok(response);
        }

        System.out.println("Before removal: " + user.getCart());

        boolean productExists = false;
        for (CartItem item : user.getCart()) {
            if (item.getProductId().equals(productId)) {
                if (item.getQuantity() > 1) {
                    // Decrease the quantity if it's greater than 1
                    item.setQuantity(item.getQuantity() - 1);
                } else {
                    // If quantity is 1, remove the product from the cart
                    user.getCart().remove(item);
                }
                productExists = true;
                break;
            }
        }

        if (!productExists) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Product not found in cart");
            return ResponseEntity.ok(response);
        }

        // Save the updated user entity back to the database
        userRepo.save(user);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Product removed/updated in cart successfully");
        return ResponseEntity.ok(response);
    }


    @PostMapping("/place-order/{userId}")
    public ResponseEntity<Order> placeOrder(@PathVariable UUID userId) {
        try {
            Order order = orderService.placeOrder(userId);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}

