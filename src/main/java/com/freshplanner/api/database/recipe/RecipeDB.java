package com.freshplanner.api.database.recipe;

import com.freshplanner.api.database.product.ProductDB;
import com.freshplanner.api.exception.ElementNotFoundException;
import com.freshplanner.api.model.recipe.RecipeModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public record RecipeDB(RecipeRepo recipeRepo,
                       RecipeItemRepo recipeItemRepo,
                       ProductDB productSelector) {

    /**
     * SELECT
     *
     * @param recipeId database id
     * @return result object
     * @throws ElementNotFoundException if id does not exist
     */
    public Recipe getRecipeById(Integer recipeId) throws ElementNotFoundException {
        Optional<Recipe> recipe = recipeRepo.findById(recipeId);
        if (recipe.isPresent()) {
            return recipe.get();
        } else {
            throw new ElementNotFoundException(Recipe.class, recipeId.toString());
        }
    }

    /**
     * SELECT
     *
     * @return all database objects
     */
    public List<Recipe> getAllRecipes() {
        return recipeRepo.findAll();
    }

    /**
     * SELECT
     *
     * @param recipeId  associated recipe id
     * @param productId associated product id
     * @return result object
     * @throws ElementNotFoundException if composite id does not exist
     */
    private RecipeItem getRecipeItemById(Integer recipeId, Integer productId) throws ElementNotFoundException {
        RecipeItem.Key id = new RecipeItem.Key(recipeId, productId);
        Optional<RecipeItem> item = recipeItemRepo.findById(id);
        if (item.isPresent()) {
            return item.get();
        } else {
            throw new ElementNotFoundException(RecipeItem.class, id.toString());
        }
    }

    /**
     * INSERT
     *
     * @param modification with input data
     * @return created object
     */
    public Recipe addRecipe(RecipeModel modification) {
        return recipeRepo.save(new Recipe(
                modification.getName(),
                modification.getCategory(),
                modification.getDescription()));
    }

    /**
     * INSERT
     *
     * @param modification with input data
     * @return created object
     * @throws ElementNotFoundException if associated id does not exist
     */
    public RecipeItem addRecipeItem(int recipeId, RecipeModel.Item modification) throws ElementNotFoundException {
        return recipeItemRepo.save(new RecipeItem(
                this.getRecipeById(recipeId),
                productSelector.getProductById(modification.getProductId()),
                modification.getCount(),
                modification.getDescription()));
    }

    /**
     * UPDATE
     *
     * @param modification with input data
     * @return updated object
     * @throws ElementNotFoundException if id does not exist
     */
    public Recipe updateRecipe(RecipeModel modification) throws ElementNotFoundException {
        return recipeRepo.save(
                this.modifyRecipe(
                        this.getRecipeById(modification.getId()),
                        modification));
    }

    /**
     * UPDATE
     *
     * @param modification with input data
     * @return updated object
     * @throws ElementNotFoundException if id does not exist
     */
    public RecipeItem updateRecipeItem(int recipeId, RecipeModel.Item modification) throws ElementNotFoundException {
        return recipeItemRepo.save(
                this.getRecipeItemById(recipeId, modification.getProductId())
                        .setCount(modification.getCount())
                        .setDescription(modification.getDescription()));
    }

    /**
     * DELETE
     *
     * @param recipeId  associated recipe id
     * @param productId associated product id
     * @return deleted object
     * @throws ElementNotFoundException if associated id does not exist
     */
    public RecipeItem deleteRecipeItem(Integer recipeId, Integer productId) throws ElementNotFoundException {
        RecipeItem item = this.getRecipeItemById(recipeId, productId);
        recipeItemRepo.delete(item);
        return item;
    }

    /**
     * DELETE
     *
     * @param recipeId database id
     * @return deleted object
     * @throws ElementNotFoundException if id does not exist
     */
    public Recipe deleteRecipeById(Integer recipeId) throws ElementNotFoundException {
        Recipe recipe = this.getRecipeById(recipeId);
        recipeRepo.delete(recipe);
        return recipe;
    }

    // === UTILITY =====================================================================================================

    private Recipe modifyRecipe(Recipe recipe, RecipeModel modification) {
        if (modification.getName() != null) {
            recipe.setName(modification.getName());
        }
        if (modification.getCategory() != null) {
            recipe.setCategory(modification.getCategory());
        }
        if (modification.getDescription() != null) {
            if (modification.getDescription().equals("")) {
                recipe.setDescription(null);
            } else {
                recipe.setDescription(modification.getDescription());
            }
        }
        return recipe;
    }
}
