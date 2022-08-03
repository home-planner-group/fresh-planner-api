package com.freshplanner.api.controller;

import com.freshplanner.api.controller.model.Product;
import environment.ControllerTest;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import utility.DataFactory;
import utility.JsonFactory;
import utility.TestLogger;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static utility.AssertionUtils.*;

class ProductControllerTest extends ControllerTest {

    private Product productExpected;

    @Test
    @Order(1)
    void addProduct() throws Exception {
        Product productModel = DataFactory.Product.productModelV1(null);
        TestLogger.info("Model for operation: " + productModel);
        String modelJson = JsonFactory.convertToJson(productModel);
        TestLogger.info("Json for operation: " + modelJson);

        MvcResult result = mockMvc.perform(
                        post("/product/insert")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .content(modelJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Product productActual = JsonFactory.convertToObject(result, Product.class);
        assertNotNull(productActual);
        assertNotNull(productActual.getId());
        assertEquals(productModel.getName(), productActual.getName());

        productExpected = productActual;
        TestLogger.info("Inserted product: " + productExpected);
    }

    @Test
    @Order(2)
    void getAllProducts() throws Exception {
        mockMvc.perform(
                        get("/product")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Order(3)
    void getProductById() throws Exception {
        mockMvc.perform(
                        get("/product/get/" + productExpected.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Order(4)
    void searchProductsByName() throws Exception {
        mockMvc.perform(
                        get("/product/search-name?name=" + productExpected.getName().substring(2))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Order(5)
    void searchProductsByCategory() throws Exception {
        mockMvc.perform(
                        get("/product/search-category?category=" + productExpected.getCategory().substring(2))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Order(6)
    void getCategories() throws Exception {
        mockMvc.perform(
                        get("/product/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Order(7)
    void getUnits() throws Exception {
        mockMvc.perform(
                        get("/product/units")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Order(8)
    void updateProduct() throws Exception {
        Product productModel = DataFactory.Product.productModelV2(productExpected.getId());
        TestLogger.info("Model for operation: " + productModel);
        String modelJson = JsonFactory.convertToJson(productModel);
        TestLogger.info("Json for operation: " + modelJson);

        MvcResult result = mockMvc.perform(
                        put("/product/update")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .content(modelJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Product productActual = JsonFactory.convertToObject(result, Product.class);
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
    @Order(9)
    void deleteProduct() throws Exception {
        mockMvc.perform(
                        delete("/product/delete/" + productExpected.getId())
                                .header(HttpHeaders.AUTHORIZATION, userAuth.getJwt())
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        TestLogger.info("Deleted product: " + productExpected);
    }
}
