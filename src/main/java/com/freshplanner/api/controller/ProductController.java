package com.freshplanner.api.controller;

import com.freshplanner.api.database.product.ProductDB;
import com.freshplanner.api.exception.ElementNotFoundException;
import com.freshplanner.api.model.product.ProductModel;
import com.freshplanner.api.model.product.ProductSummaryModel;
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
@RequestMapping("/product")
public class ProductController {

    private final ProductDB productDB;

    @Autowired
    public ProductController(ProductDB productDB) {
        this.productDB = productDB;
    }

    // === POST ========================================================================================================

    @ApiOperation("Insert a new product into the database.")
    @PostMapping(path = "/insert", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductModel> addProduct(@RequestBody ProductModel productModel) {

        return ResponseEntity.ok(new ProductModel(
                productDB.addProduct(productModel)));
    }

    // === GET =========================================================================================================

    @ApiOperation("Get all products from the database.")
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductSummaryModel>> getAllProducts() {

        return ResponseEntity.ok(
                productDB.getAllProducts()
                        .stream().map(ProductSummaryModel::new).collect(Collectors.toList()));
    }

    @ApiOperation("Get product by its given name.")
    @GetMapping(path = "/get/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductModel> getProductById(@ApiParam(value = "product db id", example = "1")
                                                       @PathVariable Integer productId) throws ElementNotFoundException {

        return ResponseEntity.ok(new ProductModel(
                productDB.getProductById(productId)));
    }

    @ApiOperation("Search products by contained name.")
    @GetMapping(path = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductModel>> searchProducts(@ApiParam(value = "product name", example = "Apple")
                                                             @RequestParam(value = "name") String productName) {

        return ResponseEntity.ok(
                productDB.searchProductByName(productName)
                        .stream().map(ProductModel::new).collect(Collectors.toList()));
    }

    // === DELETE ======================================================================================================

    @PreAuthorize("hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Delete product from the database by its generated ID.")
    @DeleteMapping(path = "/delete/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductModel> deleteProductById(@ApiParam(value = "product db id", example = "1")
                                                          @PathVariable Integer productId) throws ElementNotFoundException {

        return ResponseEntity.ok(new ProductModel(
                productDB.deleteProductById(productId)));
    }
}
