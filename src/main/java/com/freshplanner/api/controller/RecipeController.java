package com.freshplanner.api.controller;

import com.freshplanner.api.database.recipe.RecipeDB;
import com.freshplanner.api.exception.ElementNotFoundException;
import com.freshplanner.api.model.recipe.RecipeModel;
import com.freshplanner.api.model.recipe.RecipeSummaryModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/recipe")
public class RecipeController {

    private final RecipeDB recipeDB;

    @Autowired
    public RecipeController(RecipeDB recipeDB) {
        this.recipeDB = recipeDB;
    }

    // === POST ========================================================================================================

    @ApiOperation("Create a new recipe.")
    @PostMapping(path = "/insert", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RecipeModel> addRecipe(@RequestBody RecipeModel recipeModel) throws ElementNotFoundException {

        return ResponseEntity.ok(new RecipeModel(
                recipeDB.addRecipe(recipeModel)));
    }

    @ApiOperation("Create a new recipe item.")
    @PostMapping(path = "/insert-item/{recipeId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RecipeModel> addRecipeItem(@ApiParam(value = "recipe db id", example = "1")
                                                     @PathVariable Integer recipeId,
                                                     @RequestBody RecipeModel.Item recipeItemModel) throws ElementNotFoundException {

        return ResponseEntity.ok(new RecipeModel(
                recipeDB.addRecipeItem(recipeId, recipeItemModel)));
    }

    // === GET =========================================================================================================

    @ApiOperation("Get all recipes from the database.")
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RecipeSummaryModel>> getAllRecipes() {

        return ResponseEntity.ok(
                recipeDB.getAllRecipes()
                        .stream().map(RecipeSummaryModel::new).collect(Collectors.toList()));
    }

    @ApiOperation("Get recipe by its generated ID.")
    @GetMapping(path = "/get/{recipeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RecipeModel> getRecipeById(@ApiParam(value = "recipe db id", example = "1")
                                                     @PathVariable Integer recipeId) throws ElementNotFoundException {

        return ResponseEntity.ok(new RecipeModel(
                recipeDB.getRecipeById(recipeId)));
    }

    // === DELETE ======================================================================================================

    @PreAuthorize("hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Delete a recipe item via associated IDs.")
    @DeleteMapping(path = "/delete-item/{recipeId}/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RecipeModel> deleteRecipeItemById(@ApiParam(value = "recipe db id", example = "1")
                                                            @PathVariable Integer recipeId,
                                                            @ApiParam(value = "product db id", example = "1")
                                                            @PathVariable Integer productId) throws ElementNotFoundException {

        return ResponseEntity.ok(new RecipeModel(
                recipeDB.deleteRecipeItemById(recipeId, productId)));
    }

    @PreAuthorize("hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Delete recipe from the database by its generated ID.")
    @DeleteMapping(path = "/delete/{recipeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RecipeModel> deleteRecipeById(@ApiParam(value = "recipe db id", example = "1")
                                                        @PathVariable Integer recipeId) throws ElementNotFoundException {

        return ResponseEntity.ok(new RecipeModel(
                recipeDB.deleteRecipeById(recipeId)));
    }
}
