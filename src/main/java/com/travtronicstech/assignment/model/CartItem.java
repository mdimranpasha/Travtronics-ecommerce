package com.travtronicstech.assignment.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID productId;
    private int quantity;
    private UUID userId;

    // Default constructor
    public CartItem() {}

    // Constructor with productId, quantity, and userId
    public CartItem(UUID productId, int quantity, UUID userId) {
        this.productId = productId;
        this.quantity = quantity;
        this.userId = userId;
    }

    // Constructor with productId and quantity only
    public CartItem(UUID productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}
