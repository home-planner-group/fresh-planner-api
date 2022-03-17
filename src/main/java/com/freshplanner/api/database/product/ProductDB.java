package com.freshplanner.api.database.product;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public record ProductDB(ProductRepository productRepo) {

    public List<ProductEntity> getAllProducts() {
        return productRepo.findAll();
    }
}
