package com.freshplanner.api.database.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface ProductRepo extends JpaRepository<Product, Integer> {

    @Query("select p from Product p where p.name = :name")
    Optional<Product> findByName(String name);

    @Query("select p from Product p where p.name like %:name%")
    List<Product> searchByName(String name);
}
