package com.travtronicstech.assignment.controller;

import com.travtronicstech.assignment.model.Product;
import com.travtronicstech.assignment.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private ProductRepo productRepo;

    // Add a new product (Admin only)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add-product")
    public Product addProduct(@RequestBody Product product) {
        return productRepo.save(product);
    }



    // Get all products (Admin and User can access this)
    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }



    // Delete product by admin (Admin only)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete-product/{productId}")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable UUID productId) {
        Optional<Product> product = productRepo.findById(productId);

        // If product is not found, return a message
        if (!product.isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Product not found");
            return ResponseEntity.status(404).body(response);
        }

        // If product is found, delete it
        productRepo.deleteById(productId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Product deleted successfully");
        return ResponseEntity.ok(response);
    }
}
