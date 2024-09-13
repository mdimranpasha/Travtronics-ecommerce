package com.travtronicstech.assignment.service;

import com.travtronicstech.assignment.model.*;
import com.travtronicstech.assignment.repo.CartRepo;
import com.travtronicstech.assignment.repo.OrderRepo;
import com.travtronicstech.assignment.repo.ProductRepo;
import com.travtronicstech.assignment.repo.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private UserRepo userRepo; // To fetch user details

    @Transactional
    public Order placeOrder(UUID userId) throws Exception {

        // Fetch the user object by userId (you may keep this if you need the user details for other logic)
        Users user = userRepo.findById(userId).orElseThrow(() -> new Exception("User not found"));


        // Get cart items for the user using userId
        List<CartItem> cartItems = cartRepo.findByUserId(userId);  // Updated to use userId


        if (cartItems.isEmpty()) {
            throw new Exception("Cart is empty");
        }


        // Create new order
        Order order = new Order();
        order.setUser(user);  // Set user if you need user details
        order.setOrderDate(LocalDateTime.now());

        List<OrderItem> orderItems = new ArrayList<>();

        // Check stock for each product
        for (CartItem cartItem : cartItems) {
            Product product = productRepo.findByIdWithLock(cartItem.getProductId())
                    .orElseThrow(() -> new Exception("Product not found"));

            if (product.getQuantity() < cartItem.getQuantity()) {
                throw new Exception("Insufficient stock for product: " + product.getTitle());
            }

            // Update product stock
            product.setQuantity(product.getQuantity() - cartItem.getQuantity());
            productRepo.save(product);

            // Add item to order
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(cartItem.getProductId());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setOrder(order);
            orderItems.add(orderItem);
        }

        // Save the order and remove items from the cart
        order.setOrderItems(orderItems);
        orderRepo.save(order);

        cartRepo.deleteByUserId(userId);  // Clear the cart using userId
        return order;
    }


}
