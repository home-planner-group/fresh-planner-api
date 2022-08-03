package com.freshplanner.api.controller;

import com.freshplanner.api.controller.model.Recipe;
import com.freshplanner.api.exception.ElementNotFoundException;
import com.freshplanner.api.service.recipe.RecipeDB;
import com.freshplanner.api.service.recipe.RecipeEntity;
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

    @ApiOperation("Insert a new recipe into the database.")
    @PostMapping(path = "/insert", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Recipe> addRecipe(@RequestBody Recipe recipeModel) throws ElementNotFoundException {

        return ResponseEntity.ok(recipeDB.insertRecipe(recipeModel).toModel());
    }

    @ApiOperation("Insert a new recipe item into the database.")
    @PostMapping(path = "/insert-item/{recipeId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Recipe> addRecipeItem(@ApiParam(value = "recipe db id", example = "1")
                                                @PathVariable Integer recipeId,
                                                @RequestBody Recipe.Item recipeItemModel) throws ElementNotFoundException {

        return ResponseEntity.ok(recipeDB.insertRecipeItem(recipeId, recipeItemModel).toModel());
    }

    // === GET =========================================================================================================

    @ApiOperation("Get all recipes from the database.")
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Recipe>> getAllRecipes() {

        return ResponseEntity.ok(
                recipeDB.selectAllRecipes()
                        .stream().map(Recipe::new).collect(Collectors.toList()));
    }

    @ApiOperation("Get recipe by database ID.")
    @GetMapping(path = "/get/{recipeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Recipe> getRecipeById(@ApiParam(value = "recipe db id", example = "1")
                                                @PathVariable Integer recipeId) throws ElementNotFoundException {

        return ResponseEntity.ok(recipeDB.selectRecipeById(recipeId).toModel());
    }

    @ApiOperation("Search recipes by contained name.")
    @GetMapping(path = "/search-name", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Recipe>> searchRecipesByName(@ApiParam(value = "recipe name", example = "Apple")
                                                            @RequestParam(value = "name") String recipeName) {

        return ResponseEntity.ok(
                recipeDB.selectRecipesByName(recipeName)
                        .stream().map(Recipe::new).collect(Collectors.toList()));
    }

    @ApiOperation("Search recipes by contained category.")
    @GetMapping(path = "/search-category", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Recipe>> searchRecipesByCategory(@ApiParam(value = "recipe category", example = "Apple")
                                                                @RequestParam(value = "category") String recipeCategory) {

        return ResponseEntity.ok(
                recipeDB.selectRecipesByCategory(recipeCategory)
                        .stream().map(RecipeEntity::toModel).collect(Collectors.toList()));
    }

    @ApiOperation("Get all existing recipe categories.")
    @GetMapping(path = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getCategories() {

        return ResponseEntity.ok(recipeDB.selectDistinctCategories());
    }

    // === PUT =========================================================================================================

    @ApiOperation("Update recipe item in the database.")
    @PutMapping(path = "/update-item/{recipeId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Recipe.Item> updateRecipeItem(@ApiParam(value = "recipe db id", example = "1")
                                                        @PathVariable Integer recipeId,
                                                        @RequestBody Recipe.Item itemModel) throws ElementNotFoundException {

        return ResponseEntity.ok(new Recipe.Item(
                recipeDB.updateRecipeItem(recipeId, itemModel)));
    }

    @ApiOperation("Update recipe in the database.")
    @PutMapping(path = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Recipe> updateRecipe(@RequestBody Recipe recipeModel) throws ElementNotFoundException {

        return ResponseEntity.ok(recipeDB.updateRecipe(recipeModel).toModel());
    }


    // === DELETE ======================================================================================================

    @PreAuthorize("hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Delete recipe item from the database by IDs.")
    @DeleteMapping(path = "/delete-item/{recipeId}/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Recipe> deleteRecipeItem(@ApiParam(value = "recipe db id", example = "1")
                                                   @PathVariable Integer recipeId,
                                                   @ApiParam(value = "product db id", example = "1")
                                                   @PathVariable Integer productId) throws ElementNotFoundException {

        return ResponseEntity.ok(recipeDB.deleteRecipeItemById(recipeId, productId).toModel());
    }

    @PreAuthorize("hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Delete recipe from the database by ID.")
    @DeleteMapping(path = "/delete/{recipeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Recipe> deleteRecipe(@ApiParam(value = "recipe db id", example = "1")
                                               @PathVariable Integer recipeId) throws ElementNotFoundException {

        return ResponseEntity.ok(recipeDB.deleteRecipeById(recipeId).toModel());
    }
}
