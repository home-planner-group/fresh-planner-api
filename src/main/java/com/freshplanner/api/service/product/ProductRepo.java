package com.freshplanner.api.service.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface ProductRepo extends JpaRepository<Product, Integer> {

    @Query("select p from Product p where p.name like %:name%")
    List<Product> searchByName(String name);

    @Query("select p from Product p where p.category like %:category%")
    List<Product> searchByCategory(String category);

    @Query(value = "select distinct p.category from Product p where p.category is not NULL")
    List<String> findAllCategories();
}
