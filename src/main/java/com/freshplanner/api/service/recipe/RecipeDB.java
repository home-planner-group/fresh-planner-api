package com.freshplanner.api.service.recipe;

import com.freshplanner.api.controller.model.Recipe;
import com.freshplanner.api.exception.ElementNotFoundException;
import com.freshplanner.api.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Component
public class RecipeDB {

    private final RecipeRepo recipeRepo;
    private final RecipeItemRepo recipeItemRepo;
    private final ProductService productService;

    @Autowired
    public RecipeDB(RecipeRepo recipeRepo, RecipeItemRepo recipeItemRepo, ProductService recipeService) {
        this.recipeRepo = recipeRepo;
        this.recipeItemRepo = recipeItemRepo;
        this.productService = recipeService;
    }

    // === SELECT ======================================================================================================

    /**
     * SELECT recipe WHERE recipeId
     *
     * @param recipeId database id
     * @return result object
     * @throws ElementNotFoundException if id does not exist
     */
    public RecipeEntity selectRecipeById(Integer recipeId) throws ElementNotFoundException {
        Optional<RecipeEntity> recipe = recipeRepo.findById(recipeId);
        if (recipe.isPresent()) {
            return recipe.get();
        } else {
            throw new ElementNotFoundException(RecipeEntity.class, recipeId.toString());
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
    public List<RecipeEntity> selectRecipesByName(String recipeName) {
        return recipeRepo.searchByName(recipeName);
    }

    /**
     * SELECT recipe WHERE LIKE recipeCategory
     *
     * @param recipeCategory partial name
     * @return list with result objects
     */
    public List<RecipeEntity> selectRecipesByCategory(String recipeCategory) {
        return recipeRepo.searchByCategory(recipeCategory);
    }

    /**
     * SELECT recipe
     *
     * @return list with all objects
     */
    public List<RecipeEntity> selectAllRecipes() {
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
    public RecipeEntity insertRecipe(Recipe recipeModel) throws ElementNotFoundException {
        RecipeEntity recipe = recipeRepo.save(new RecipeEntity(recipeModel));
        for (Recipe.Item item : recipeModel.getItems()) {
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
    public RecipeEntity insertRecipeItem(int recipeId, Recipe.Item recipeItemModel) throws ElementNotFoundException {
        RecipeEntity recipe = selectRecipeById(recipeId);
        recipe.getRecipeItems().add(insertRecipeItem(recipe, recipeItemModel));
        return recipe;
    }

    private RecipeItem insertRecipeItem(RecipeEntity recipe, Recipe.Item recipeItemModel) throws ElementNotFoundException {
        return recipeItemRepo.save(new RecipeItem(
                recipe,
                productService.selectProductById(recipeItemModel.getProductId()),
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
    public RecipeEntity updateRecipe(Recipe recipeModel) throws ElementNotFoundException {
        RecipeEntity recipe = this.selectRecipeById(recipeModel.getId());
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
    public RecipeItem updateRecipeItem(int recipeId, Recipe.Item itemModel) throws ElementNotFoundException {
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
    public RecipeEntity deleteRecipeItemById(Integer recipeId, Integer productId) throws ElementNotFoundException {
        RecipeItem item = this.selectRecipeItemById(recipeId, productId);
        recipeItemRepo.delete(item);
        RecipeEntity recipe = selectRecipeById(recipeId);
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
    public RecipeEntity deleteRecipeById(Integer recipeId) throws ElementNotFoundException {
        RecipeEntity recipe = this.selectRecipeById(recipeId);
        recipeRepo.delete(recipe);
        return recipe;
    }
}
