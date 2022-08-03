package com.freshplanner.api.controller;

import com.freshplanner.api.exception.ElementNotFoundException;
import com.freshplanner.api.model.recipe.RecipeModel;
import com.freshplanner.api.model.recipe.RecipeSummaryModel;
import com.freshplanner.api.service.recipe.RecipeDB;
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
    public ResponseEntity<RecipeModel> addRecipe(@RequestBody RecipeModel recipeModel) throws ElementNotFoundException {

        return ResponseEntity.ok(new RecipeModel(
                recipeDB.insertRecipe(recipeModel)));
    }

    @ApiOperation("Insert a new recipe item into the database.")
    @PostMapping(path = "/insert-item/{recipeId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RecipeModel> addRecipeItem(@ApiParam(value = "recipe db id", example = "1")
                                                     @PathVariable Integer recipeId,
                                                     @RequestBody RecipeModel.Item recipeItemModel) throws ElementNotFoundException {

        return ResponseEntity.ok(new RecipeModel(
                recipeDB.insertRecipeItem(recipeId, recipeItemModel)));
    }

    // === GET =========================================================================================================

    @ApiOperation("Get all recipes from the database.")
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RecipeSummaryModel>> getAllRecipes() {

        return ResponseEntity.ok(
                recipeDB.selectAllRecipes()
                        .stream().map(RecipeSummaryModel::new).collect(Collectors.toList()));
    }

    @ApiOperation("Get recipe by database ID.")
    @GetMapping(path = "/get/{recipeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RecipeModel> getRecipeById(@ApiParam(value = "recipe db id", example = "1")
                                                     @PathVariable Integer recipeId) throws ElementNotFoundException {

        return ResponseEntity.ok(new RecipeModel(
                recipeDB.selectRecipeById(recipeId)));
    }

    @ApiOperation("Search recipes by contained name.")
    @GetMapping(path = "/search-name", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RecipeSummaryModel>> searchRecipesByName(@ApiParam(value = "recipe name", example = "Apple")
                                                                        @RequestParam(value = "name") String recipeName) {

        return ResponseEntity.ok(
                recipeDB.selectRecipesByName(recipeName)
                        .stream().map(RecipeSummaryModel::new).collect(Collectors.toList()));
    }

    @ApiOperation("Search recipes by contained category.")
    @GetMapping(path = "/search-category", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RecipeSummaryModel>> searchRecipesByCategory(@ApiParam(value = "recipe category", example = "Apple")
                                                                            @RequestParam(value = "category") String recipeCategory) {

        return ResponseEntity.ok(
                recipeDB.selectRecipesByCategory(recipeCategory)
                        .stream().map(RecipeSummaryModel::new).collect(Collectors.toList()));
    }

    @ApiOperation("Get all existing recipe categories.")
    @GetMapping(path = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getCategories() {

        return ResponseEntity.ok(recipeDB.selectDistinctCategories());
    }

    // === PUT =========================================================================================================

    @ApiOperation("Update recipe item in the database.")
    @PutMapping(path = "/update-item/{recipeId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RecipeModel.Item> updateRecipeItem(@ApiParam(value = "recipe db id", example = "1")
                                                             @PathVariable Integer recipeId,
                                                             @RequestBody RecipeModel.Item itemModel) throws ElementNotFoundException {

        return ResponseEntity.ok(new RecipeModel.Item(
                recipeDB.updateRecipeItem(recipeId, itemModel)));
    }

    @ApiOperation("Update recipe in the database.")
    @PutMapping(path = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RecipeModel> updateRecipe(@RequestBody RecipeModel recipeModel) throws ElementNotFoundException {

        return ResponseEntity.ok(new RecipeModel(
                recipeDB.updateRecipe(recipeModel)));
    }


    // === DELETE ======================================================================================================

    @PreAuthorize("hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Delete recipe item from the database by IDs.")
    @DeleteMapping(path = "/delete-item/{recipeId}/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RecipeModel> deleteRecipeItem(@ApiParam(value = "recipe db id", example = "1")
                                                        @PathVariable Integer recipeId,
                                                        @ApiParam(value = "product db id", example = "1")
                                                        @PathVariable Integer productId) throws ElementNotFoundException {

        return ResponseEntity.ok(new RecipeModel(
                recipeDB.deleteRecipeItemById(recipeId, productId)));
    }

    @PreAuthorize("hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Delete recipe from the database by ID.")
    @DeleteMapping(path = "/delete/{recipeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RecipeModel> deleteRecipe(@ApiParam(value = "recipe db id", example = "1")
                                                    @PathVariable Integer recipeId) throws ElementNotFoundException {

        return ResponseEntity.ok(new RecipeModel(
                recipeDB.deleteRecipeById(recipeId)));
    }
}
