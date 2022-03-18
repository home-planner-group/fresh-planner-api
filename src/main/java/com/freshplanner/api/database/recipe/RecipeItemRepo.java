package com.freshplanner.api.database.recipe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface RecipeItemRepo extends JpaRepository<RecipeItem, RecipeItem.Key> {
}
