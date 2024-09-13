package com.travtronicstech.assignment.repo;

import com.travtronicstech.assignment.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CartRepo extends JpaRepository<CartItem, UUID> {
    List<CartItem> findByUserId(UUID userId);
    void deleteByUserId(UUID userId);
}
