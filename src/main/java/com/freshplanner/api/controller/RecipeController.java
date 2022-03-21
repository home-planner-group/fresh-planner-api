package com.freshplanner.api.controller;

import com.freshplanner.api.database.recipe.RecipeDB;
import com.freshplanner.api.exception.ElementNotFoundException;
import com.freshplanner.api.model.recipe.RecipeModel;
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

    // === GET =========================================================================================================

    @ApiOperation("Get all recipes from the database.")
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RecipeModel>> getAllRecipes() {

        return ResponseEntity.ok(recipeDB.getAllRecipes()
                .stream().map(RecipeModel::new).collect(Collectors.toList()));
    }

    @ApiOperation("Get recipe by its generated ID.")
    @GetMapping(path = "/get/{recipeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RecipeModel> getRecipe(
            @ApiParam(value = "recipe db id", example = "1") @PathVariable Integer recipeId)
            throws ElementNotFoundException {
        return ResponseEntity.ok(new RecipeModel(recipeDB.getRecipeById(recipeId)));
    }

    // === POST ========================================================================================================

    @PreAuthorize("hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Create a new recipe.")
    @PostMapping(path = "/insert", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RecipeModel> addRecipe(@RequestBody RecipeModel modification) {
        return ResponseEntity.ok(new RecipeModel(recipeDB.addRecipe(modification)));
    }

//    @PreAuthorize("hasRole('EDITOR') or hasRole('ADMIN')")
//    @ApiOperation("Insert a recipe item by given parameters.")
//    @PostMapping(path = "/insert/item", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<RecipeModel.Item> addRecipeItem(@RequestBody RecipeModel.Item item)
//            throws ElementNotFoundException {
//        return ResponseEntity.ok(new RecipeModel.Item(recipeDB.addRecipeItem(item)));
//    }
//
//    // === PUT =========================================================================================================
//
//    @PreAuthorize("hasRole('EDITOR') or hasRole('ADMIN')")
//    @ApiOperation("Update a recipe item by given parameters.")
//    @PutMapping(path = "/update/{recipeId}", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<RecipeModel> updateRecipe(
//            @ApiParam(value = "recipe db id", example = "1") @PathVariable Integer recipeId,
//            @RequestBody RecipeModel.Item modification)
//            throws ElementNotFoundException {
//        return ResponseEntity.ok(new RecipeModel(recipeDB.updateRecipe(recipeId, modification)));
//    }
//
//    @PreAuthorize("hasRole('EDITOR') or hasRole('ADMIN')")
//    @ApiOperation("Update a recipe item by given parameters.")
//    @PutMapping(path = "/update/item", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<RecipeModel.Item> updateRecipeItemItem(@RequestBody RecipeModel.Item item)
//            throws ElementNotFoundException {
//        return ResponseEntity.ok(new RecipeModel.Item(recipeDB.updateRecipeItem(item)));
//    }

    // === DELETE ======================================================================================================

    @PreAuthorize("hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Delete a recipe item via associated IDs.")
    @DeleteMapping(path = "/delete/item/{recipeId}/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RecipeModel.Item> deleteRecipeItem(
            @ApiParam(value = "recipe db id", example = "1") @PathVariable Integer recipeId,
            @ApiParam(value = "product db id", example = "1") @PathVariable Integer productId)
            throws ElementNotFoundException {
        return ResponseEntity.ok(new RecipeModel.Item(recipeDB.deleteRecipeItem(recipeId, productId)));
    }

    @PreAuthorize("hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Delete recipe from the database by its generated ID.")
    @DeleteMapping(path = "/delete/{recipeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RecipeModel> deleteRecipe(
            @ApiParam(value = "recipe db id", example = "1") @PathVariable Integer recipeId)
            throws ElementNotFoundException {
        return ResponseEntity.ok(new RecipeModel(recipeDB.deleteRecipeById(recipeId)));
    }
}
