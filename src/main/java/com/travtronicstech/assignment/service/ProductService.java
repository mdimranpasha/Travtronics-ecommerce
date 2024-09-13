package com.travtronicstech.assignment.service;

import com.travtronicstech.assignment.model.Product;
import com.travtronicstech.assignment.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    public Page<Product> searchProducts(String searchQuery, Pageable pageable) {
        return productRepo.findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(searchQuery, searchQuery, pageable);
    }
}
