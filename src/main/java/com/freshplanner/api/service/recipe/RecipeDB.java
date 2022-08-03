package com.freshplanner.api.service.recipe;

import com.freshplanner.api.exception.ElementNotFoundException;
import com.freshplanner.api.model.recipe.RecipeModel;
import com.freshplanner.api.service.product.ProductDB;
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
    public RecipeDB(RecipeRepo recipeRepo, RecipeItemRepo recipeItemRepo, ProductDB productDB) {
        this.recipeRepo = recipeRepo;
        this.recipeItemRepo = recipeItemRepo;
        this.productSelector = productDB;
    }

    // === SELECT ======================================================================================================

    /**
     * SELECT recipe WHERE recipeId
     *
     * @param recipeId database id
     * @return result object
     * @throws ElementNotFoundException if id does not exist
     */
    public Recipe selectRecipeById(Integer recipeId) throws ElementNotFoundException {
        Optional<Recipe> recipe = recipeRepo.findById(recipeId);
        if (recipe.isPresent()) {
            return recipe.get();
        } else {
            throw new ElementNotFoundException(Recipe.class, recipeId.toString());
        }
    }

    private RecipeItem selectRecipeItemById(Integer recipeId, Integer productId) throws ElementNotFoundException {
        RecipeItem.Key id = new RecipeItem.Key(recipeId, productId);
        Optional<RecipeItem> item = recipeItemRepo.findById(id);
        if (item.isPresent()) {
            return item.get();
        } else {
            throw new ElementNotFoundException(RecipeItem.class, id.toString());
        }
    }

    /**
     * SELECT recipe WHERE LIKE recipeName
     *
     * @param recipeName partial name
     * @return list with result objects
     */
    public List<Recipe> selectRecipesByName(String recipeName) {
        return recipeRepo.searchByName(recipeName);
    }

    /**
     * SELECT recipe WHERE LIKE recipeCategory
     *
     * @param recipeCategory partial name
     * @return list with result objects
     */
    public List<Recipe> selectRecipesByCategory(String recipeCategory) {
        return recipeRepo.searchByCategory(recipeCategory);
    }

    /**
     * SELECT recipe
     *
     * @return list with all objects
     */
    public List<Recipe> selectAllRecipes() {
        return recipeRepo.findAll();
    }

    /**
     * SELECT DISTINCT recipe-category
     *
     * @return list with all objects
     */
    public List<String> selectDistinctCategories() {
        return recipeRepo.findAllCategories();
    }

    // === INSERT ======================================================================================================

    /**
     * INSERT recipe
     *
     * @param recipeModel with input data
     * @return created object
     * @throws ElementNotFoundException if id does not exist
     */
    @Transactional
    public Recipe insertRecipe(RecipeModel recipeModel) throws ElementNotFoundException {
        Recipe recipe = recipeRepo.save(new Recipe(recipeModel));
        for (RecipeModel.Item item : recipeModel.getItems()) {
            recipe.getRecipeItems().add(insertRecipeItem(recipe, item));
        }
        return recipe;
    }

    /**
     * INSERT recipeItem
     *
     * @param recipeId        linked database id
     * @param recipeItemModel with input data
     * @return updated object
     * @throws ElementNotFoundException if id does not exist
     */
    @Transactional
    public Recipe insertRecipeItem(int recipeId, RecipeModel.Item recipeItemModel) throws ElementNotFoundException {
        Recipe recipe = selectRecipeById(recipeId);
        recipe.getRecipeItems().add(insertRecipeItem(recipe, recipeItemModel));
        return recipe;
    }

    private RecipeItem insertRecipeItem(Recipe recipe, RecipeModel.Item recipeItemModel) throws ElementNotFoundException {
        return recipeItemRepo.save(new RecipeItem(
                recipe,
                productSelector.selectProductById(recipeItemModel.getProductId()),
                recipeItemModel.getCount(),
                recipeItemModel.getDescription()));
    }

    // === UPDATE ======================================================================================================

    /**
     * UPDATE recipe
     *
     * @param recipeModel with input data
     * @return updated object
     * @throws ElementNotFoundException if id does not exist
     */
    @Transactional
    public Recipe updateRecipe(RecipeModel recipeModel) throws ElementNotFoundException {
        Recipe recipe = this.selectRecipeById(recipeModel.getId());
        return recipeRepo.save(recipe.update(recipeModel));
    }

    /**
     * UPDATE recipeItem
     *
     * @param recipeId  linked database id
     * @param itemModel with input data
     * @return updated object
     * @throws ElementNotFoundException if id does not exist
     */
    @Transactional
    public RecipeItem updateRecipeItem(int recipeId, RecipeModel.Item itemModel) throws ElementNotFoundException {
        RecipeItem recipeItem = this.selectRecipeItemById(recipeId, itemModel.getProductId());
        return recipeItemRepo.save(recipeItem.update(itemModel));
    }

    // === DELETE ======================================================================================================

    /**
     * DELETE recipeItem WHERE recipeId AND productId
     *
     * @param recipeId  linked database id
     * @param productId linked database id
     * @return updated object
     * @throws ElementNotFoundException if id does not exist
     */
    public Recipe deleteRecipeItemById(Integer recipeId, Integer productId) throws ElementNotFoundException {
        RecipeItem item = this.selectRecipeItemById(recipeId, productId);
        recipeItemRepo.delete(item);
        Recipe recipe = selectRecipeById(recipeId);
        recipe.getRecipeItems().remove(item);
        return recipe;
    }

    /**
     * DELETE recipe WHERE recipeId
     *
     * @param recipeId database id
     * @return deleted object
     * @throws ElementNotFoundException if id does not exist
     */
    public Recipe deleteRecipeById(Integer recipeId) throws ElementNotFoundException {
        Recipe recipe = this.selectRecipeById(recipeId);
        recipeRepo.delete(recipe);
        return recipe;
    }
}
