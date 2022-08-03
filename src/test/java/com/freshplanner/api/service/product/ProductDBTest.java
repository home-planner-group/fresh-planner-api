package com.freshplanner.api.service.product;

import com.freshplanner.api.controller.model.Product;
import com.freshplanner.api.exception.ElementNotFoundException;
import environment.ApplicationTest;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import utility.DataFactory;
import utility.TestLogger;

import java.util.List;

import static utility.AssertionUtils.*;

class ProductDBTest extends ApplicationTest {

    @Autowired
    private ProductDB productDB;

    private ProductEntity productExpected;

    @Test
    @Order(1)
    void insertProduct() {
        Product productModel = DataFactory.Product.productModelV1(null);
        TestLogger.info("Model for operation: " + productModel);

        ProductEntity productActual = productDB.insertProduct(productModel);
        assertNotNull(productActual.getId());
        assertEquals(productModel.getName(), productActual.getName());

        productExpected = productActual;
        TestLogger.info("Inserted product: " + productExpected);
    }

    @Test
    @Order(2)
    void selectProductById() throws ElementNotFoundException {
        ProductEntity productActual = productDB.selectProductById(productExpected.getId());
        assertEquals(productExpected, productActual);
    }

    @Test
    @Order(3)
    void selectProductsByName() {
        List<ProductEntity> result = productDB.selectProductsByName(productExpected.getName().substring(2));
        assertContains(result, productExpected);
    }

    @Test
    @Order(4)
    void selectProductsByCategory() {
        List<ProductEntity> result = productDB.selectProductsByCategory(productExpected.getCategory().substring(2));
        assertContains(result, productExpected);
    }

    @Test
    @Order(5)
    void selectAllProducts() {
        List<ProductEntity> result = productDB.selectAllProducts();
        assertContains(result, productExpected);
    }


    @Test
    @Order(6)
    void selectDistinctCategories() {
        List<String> result = productDB.selectDistinctCategories();
        assertContains(result, productExpected.getCategory());
    }

    @Test
    @Order(7)
    void updateProduct() throws ElementNotFoundException {
        Product productModel = DataFactory.Product.productModelV2(productExpected.getId());
        TestLogger.info("Model for operation: " + productModel);

        ProductEntity productActual = productDB.updateProduct(productModel);
        assertEquals(productExpected.getId(), productActual.getId());
        assertEquals(productExpected.getName(), productActual.getName());
        assertEquals(productExpected.getCategory(), productActual.getCategory());
        assertNotEquals(productExpected.getUnit(), productActual.getUnit());
        assertNotEquals(productExpected.getPackageSize(), productActual.getPackageSize());
        assertNotEquals(productExpected.getKcal(), productActual.getKcal());
        assertNotEquals(productExpected.getCarbohydrates(), productActual.getCarbohydrates());
        assertNotEquals(productExpected.getProtein(), productActual.getProtein());
        assertNotEquals(productExpected.getFat(), productActual.getFat());

        productExpected = productActual;
        TestLogger.info("Updated product: " + productExpected);
    }

    @Test
    @Order(8)
    void deleteProductById() throws ElementNotFoundException {
        ProductEntity productActual = productDB.deleteProductById(productExpected.getId());
        assertEquals(productExpected, productActual);

        productExpected = productActual;
        TestLogger.info("Deleted product: " + productExpected);
    }

    @Test
    @Order(9)
    void assertElementNotFound() {
        assertThrows(ElementNotFoundException.class,
                () -> productDB.selectProductById(productExpected.getId()));
        assertThrows(ElementNotFoundException.class,
                () -> productDB.updateProduct(DataFactory.Product.productModelV1(productExpected.getId())));
        assertThrows(ElementNotFoundException.class,
                () -> productDB.deleteProductById(productExpected.getId()));
    }
}
