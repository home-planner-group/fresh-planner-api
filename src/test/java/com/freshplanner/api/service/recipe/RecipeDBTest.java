package com.freshplanner.api.service.recipe;

import com.freshplanner.api.controller.model.Product;
import com.freshplanner.api.controller.model.Recipe;
import com.freshplanner.api.exception.ElementNotFoundException;
import com.freshplanner.api.service.product.ProductDB;
import com.freshplanner.api.service.product.ProductEntity;
import environment.ApplicationTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import utility.DataFactory;
import utility.TestLogger;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utility.AssertionUtils.*;

class RecipeDBTest extends ApplicationTest {

    @Autowired
    private ProductDB productDB;

    @Autowired
    private RecipeDB recipeDB;

    private ProductEntity productExpected;
    private RecipeEntity recipeExpected;

    @BeforeAll
    void beforeAll() {
        Product productModel = DataFactory.Product.productModelV1(null);
        TestLogger.info("Model for operation: " + productModel);

        ProductEntity productActual = productDB.insertProduct(productModel);
        assertNotNull(productActual.getId());
        assertEquals(productModel.getName(), productActual.getName());

        productExpected = productActual;
        TestLogger.info("Inserted product: " + productExpected);
    }

    @Test
    @Order(1)
    void insertRecipe() throws ElementNotFoundException {
        Recipe recipeModel = DataFactory.Recipe.recipeModelV1(null);
        TestLogger.info("Model for operation: " + recipeModel);

        RecipeEntity recipeActual = recipeDB.insertRecipe(recipeModel);
        assertNotNull(recipeActual.getId());
        assertEquals(recipeModel.getName(), recipeActual.getName());

        recipeExpected = recipeActual;
        TestLogger.info("Inserted recipe: " + recipeExpected);
    }

    @Test
    @Order(2)
    void insertRecipeItem() throws ElementNotFoundException {
        Recipe.Item recipeItemModel = DataFactory.Recipe.recipeItemV1(productExpected.getId());
        TestLogger.info("Model for operation: " + recipeItemModel);

        RecipeEntity recipeActual = recipeDB.insertRecipeItem(recipeExpected.getId(), recipeItemModel);
        assertContains(recipeActual.getRecipeItems().stream().map(RecipeItem::getProductId).collect(Collectors.toSet()), productExpected.getId());

        recipeExpected = recipeActual;
        TestLogger.info("Updated recipe: " + recipeExpected);
    }


    @Test
    @Order(3)
    void selectRecipeById() throws ElementNotFoundException {
        RecipeEntity recipeActual = recipeDB.selectRecipeById(recipeExpected.getId());
        assertEquals(recipeExpected, recipeActual);
    }

    @Test
    @Order(4)
    void selectRecipesByName() {
        List<RecipeEntity> result = recipeDB.selectRecipesByName(recipeExpected.getName().substring(2));
        assertContains(result, recipeExpected);
    }

    @Test
    @Order(5)
    void selectRecipesByCategory() {
        List<RecipeEntity> result = recipeDB.selectRecipesByCategory(recipeExpected.getCategory().substring(2));
        assertContains(result, recipeExpected);
    }

    @Test
    @Order(6)
    void selectAllRecipes() {
        List<RecipeEntity> result = recipeDB.selectAllRecipes();
        assertContains(result, recipeExpected);
    }

    @Test
    @Order(7)
    void selectDistinctCategories() {
        List<String> result = recipeDB.selectDistinctCategories();
        assertContains(result, recipeExpected.getCategory());
    }

    @Test
    @Order(8)
    void updateRecipe() throws ElementNotFoundException {
        Recipe recipeModel = DataFactory.Recipe.recipeModelV2(recipeExpected.getId());
        TestLogger.info("Model for operation: " + recipeModel);

        RecipeEntity recipeActual = recipeDB.updateRecipe(recipeModel);
        assertEquals(recipeExpected.getId(), recipeActual.getId());
        assertEquals(recipeModel.getName(), recipeActual.getName());
        assertEquals(recipeModel.getCategory(), recipeActual.getCategory());
        assertEquals(recipeModel.getDuration(), recipeActual.getDuration());
        assertEquals(recipeModel.getDescription(), recipeActual.getDescription());

        recipeExpected = recipeActual;
        TestLogger.info("Updated recipe: " + recipeExpected);
    }

    @Test
    @Order(9)
    void updateRecipeItem() throws ElementNotFoundException {
        Recipe.Item itemModel = DataFactory.Recipe.recipeItemV2(productExpected.getId());
        TestLogger.info("Model for operation: " + itemModel);

        RecipeItem recipeItem = recipeDB.updateRecipeItem(recipeExpected.getId(), itemModel);
        assertEquals(itemModel.getCount(), recipeItem.getCount());
        assertEquals(itemModel.getDescription(), recipeItem.getDescription());
        TestLogger.info("Updated recipe: " + recipeItem);

        RecipeEntity recipeActual = recipeDB.selectRecipeById(recipeExpected.getId());
        assertContains(recipeActual.getRecipeItems(), recipeItem);

        recipeExpected = recipeActual;
        TestLogger.info("Updated recipe: " + recipeExpected);
    }


    @Test
    @Order(10)
    void deleteRecipeItemById() throws ElementNotFoundException {
        RecipeEntity recipeActual = recipeDB.deleteRecipeItemById(recipeExpected.getId(), productExpected.getId());
        assertNotContains(recipeActual.getRecipeItems().stream().map(RecipeItem::getProductId).collect(Collectors.toSet()), productExpected.getId());

        recipeExpected = recipeActual;
        TestLogger.info("Updated recipe: " + recipeExpected);
    }

    @Test
    @Order(11)
    void deleteRecipeById() throws ElementNotFoundException {
        RecipeEntity recipeActual = recipeDB.deleteRecipeById(recipeExpected.getId());
        assertEquals(recipeExpected, recipeActual);


        recipeExpected = recipeActual;
        TestLogger.info("Deleted recipe: " + recipeExpected);
    }

    @Test
    @Order(12)
    void assertElementNotFound() {
        assertThrows(ElementNotFoundException.class,
                () -> recipeDB.selectRecipeById(recipeExpected.getId()));
        // TODO insert Recipe with items
        assertThrows(ElementNotFoundException.class,
                () -> recipeDB.insertRecipeItem(recipeExpected.getId(), new Recipe.Item()));
        assertThrows(ElementNotFoundException.class,
                () -> recipeDB.updateRecipe(new Recipe(recipeExpected)));
        assertThrows(ElementNotFoundException.class,
                () -> recipeDB.updateRecipeItem(recipeExpected.getId(), new Recipe.Item()));
        assertThrows(ElementNotFoundException.class,
                () -> recipeDB.deleteRecipeItemById(recipeExpected.getId(), productExpected.getId()));
        assertThrows(ElementNotFoundException.class,
                () -> recipeDB.deleteRecipeById(recipeExpected.getId()));
    }

    @AfterAll
    void afterAll() throws ElementNotFoundException {
        ProductEntity productActual = productDB.deleteProductById(productExpected.getId());
        assertEquals(productExpected, productActual);

        productExpected = productActual;
        TestLogger.info("Deleted product: " + productExpected);
    }
}
