package com.freshplanner.api.service.recipe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface RecipeRepo extends JpaRepository<RecipeEntity, Integer> {

    @Query("select r from Recipe r where r.name like %:name%")
    List<RecipeEntity> searchByName(String name);

    @Query("select r from Recipe r where r.category like %:category%")
    List<RecipeEntity> searchByCategory(String category);

    @Query(value = "select distinct r.category from Recipe r where r.category is not NULL")
    List<String> findAllCategories();
}
