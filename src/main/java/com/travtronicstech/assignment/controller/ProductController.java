package com.travtronicstech.assignment.controller;

import com.travtronicstech.assignment.model.Product;
import com.travtronicstech.assignment.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Paginated search for products by name or category
    @GetMapping("/search")
    public Page<Product> searchProducts(
            @RequestParam(required = false, defaultValue = "") String query,
            Pageable pageable) {
        return productService.searchProducts(query, pageable);
    }
}
