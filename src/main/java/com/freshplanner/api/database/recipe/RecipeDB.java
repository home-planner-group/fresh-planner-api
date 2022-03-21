package com.freshplanner.api.database.recipe;

import com.freshplanner.api.database.product.ProductDB;
import com.freshplanner.api.exception.ElementNotFoundException;
import com.freshplanner.api.model.recipe.RecipeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Component
public class RecipeDB {

    private final RecipeRepo recipeRepo;
    private final RecipeItemRepo recipeItemRepo;
    private final ProductDB productSelector;

    @Autowired
    public RecipeDB(RecipeRepo recipeRepo, RecipeItemRepo recipeItemRepo, ProductDB productSelector) {
        this.recipeRepo = recipeRepo;
        this.recipeItemRepo = recipeItemRepo;
        this.productSelector = productSelector;
    }

    /**
     * SELECT recipe WHERE recipeId
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
     * SELECT recipe
     *
     * @return list with all objects
     */
    public List<Recipe> getAllRecipes() {
        return recipeRepo.findAll();
    }

    /**
     * INSERT recipe
     *
     * @param recipeModel with input data
     * @return result object
     * @throws ElementNotFoundException if id does not exist
     */
    @Transactional
    public Recipe addRecipe(RecipeModel recipeModel) throws ElementNotFoundException {
        Recipe recipe = recipeRepo.save(new Recipe(recipeModel));
        for (RecipeModel.Item item : recipeModel.getItems()) {
            recipe.getRecipeItems().add(addRecipeItem(recipe, item));
        }
        return recipe;
    }

    /**
     * INSERT recipeItem
     *
     * @param recipeId        linked database id
     * @param recipeItemModel with input data
     * @throws ElementNotFoundException if linked id does not exist
     */
    @Transactional
    public Recipe addRecipeItem(int recipeId, RecipeModel.Item recipeItemModel) throws ElementNotFoundException {
        Recipe recipe = getRecipeById(recipeId);
        recipe.getRecipeItems().add(addRecipeItem(recipe, recipeItemModel));
        return recipe;
    }

    private RecipeItem addRecipeItem(Recipe recipe, RecipeModel.Item recipeItemModel) throws ElementNotFoundException {
        return recipeItemRepo.save(new RecipeItem(
                recipe,
                productSelector.getProductById(recipeItemModel.getProductId()),
                recipeItemModel.getCount(),
                recipeItemModel.getDescription()));
    }

    // TODO implement updateRecipe with PUT request
    public Recipe updateRecipe(RecipeModel modification) throws ElementNotFoundException {
        return recipeRepo.save(
                this.modifyRecipe(
                        this.getRecipeById(modification.getId()),
                        modification));
    }

    // TODO implement updateRecipeItem with PUT request
    public RecipeItem updateRecipeItem(int recipeId, RecipeModel.Item modification) throws ElementNotFoundException {
        return recipeItemRepo.save(
                this.getRecipeItemById(recipeId, modification.getProductId())
                        .setCount(modification.getCount())
                        .setDescription(modification.getDescription()));
    }

    /**
     * DELETE recipeItem WHERE recipeId AND productId
     *
     * @param recipeId  linked database id
     * @param productId linked database id
     * @return deleted object
     * @throws ElementNotFoundException if linked id does not exist
     */
    public RecipeItem deleteRecipeItemById(Integer recipeId, Integer productId) throws ElementNotFoundException {
        RecipeItem item = this.getRecipeItemById(recipeId, productId);
        recipeItemRepo.delete(item);
        return item;
    }

    /**
     * DELETE recipe WHERE recipeId
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
