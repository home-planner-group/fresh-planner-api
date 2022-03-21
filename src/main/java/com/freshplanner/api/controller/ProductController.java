package com.freshplanner.api.controller;

import com.freshplanner.api.database.product.ProductDB;
import com.freshplanner.api.exception.ElementNotFoundException;
import com.freshplanner.api.model.product.ProductModel;
import com.freshplanner.api.model.product.ProductModification;
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

    // === GET =========================================================================================================

    @ApiOperation("Get all products from the database.")
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductModel>> getAllProducts() {

        return ResponseEntity.ok(productDB.getAllProducts()
                .stream().map(ProductModel::new).collect(Collectors.toList()));
    }

    @ApiOperation("Get product by its given name.")
    @GetMapping(path = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductModel> getProductByName(
            @ApiParam(value = "product name", example = "Apple") @RequestParam(value = "name") String productName)
            throws ElementNotFoundException {

        return ResponseEntity.ok(new ProductModel(productDB.getProductByName(productName)));
    }

    @ApiOperation("Search products by contained name.")
    @GetMapping(path = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductModel>> searchProducts(
            @ApiParam(value = "product name", example = "Apple") @RequestParam(value = "name") String productName) {

        return ResponseEntity.ok(productDB.searchProductByName(productName)
                .stream().map(ProductModel::new).collect(Collectors.toList()));
    }

    // === POST ========================================================================================================

    @PreAuthorize("hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Insert a new product into the database.")
    @PostMapping(path = "/insert", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductModel> addProduct(
            @RequestBody ProductModification product) {

        return ResponseEntity.ok(new ProductModel(productDB.addProduct(product)));
    }

    // === PUT =========================================================================================================

    @PreAuthorize("hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Update an existing product by its generated ID.")
    @PutMapping(path = "/update/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductModel> updateProduct(
            @ApiParam(value = "product db id", example = "1") @PathVariable Integer productId,
            @RequestBody ProductModification product)
            throws ElementNotFoundException {

        return ResponseEntity.ok(new ProductModel(productDB.updateProduct(productId, product)));
    }

    // === DELETE ======================================================================================================

    @PreAuthorize("hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Delete product from the database by its generated ID.")
    @DeleteMapping(path = "/delete/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductModel> deleteProduct(
            @ApiParam(value = "product db id", example = "1") @PathVariable Integer productId)
            throws ElementNotFoundException {

        return ResponseEntity.ok(new ProductModel(productDB.deleteProductById(productId)));
    }
}
