package com.freshplanner.api.service.recipe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface RecipeItemRepo extends JpaRepository<RecipeItemEntity, RecipeItemEntity.Key> {
}
