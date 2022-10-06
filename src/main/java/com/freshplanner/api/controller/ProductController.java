package com.freshplanner.api.controller;

import com.freshplanner.api.controller.model.Product;
import com.freshplanner.api.enums.Unit;
import com.freshplanner.api.exception.ElementNotFoundException;
import com.freshplanner.api.service.product.ProductDB;
import com.freshplanner.api.service.product.ProductEntity;
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

    /**
     * GET
     *
     * @return existing products
     */
    @ApiOperation("Get all products from the database.")
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(
                productDB.selectAllProducts()
                        .stream().map(ProductEntity::mapToModel).collect(Collectors.toList()));
    }

    /**
     * GET
     *
     * @param productId from request path
     * @return matching object
     * @throws ElementNotFoundException if id does not exist
     */
    @ApiOperation("Get product by database ID.")
    @GetMapping(path = "/get/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> getProductById(@ApiParam(value = "product db id", example = "1")
                                                  @PathVariable Integer productId) throws ElementNotFoundException {
        return ResponseEntity.ok(productDB.selectProductById(productId).mapToModel());
    }

    /**
     * GET
     *
     * @param productName from request parameter
     * @return matching objects
     */
    @ApiOperation("Search products by contained name.")
    @GetMapping(path = "/search-name", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Product>> searchProductsByName(@ApiParam(value = "product name", example = "Apple")
                                                              @RequestParam(value = "name") String productName) {

        return ResponseEntity.ok(
                productDB.selectProductsByName(productName)
                        .stream().map(ProductEntity::mapToModel).collect(Collectors.toList()));
    }

    /**
     * GET
     *
     * @param productCategory from request parameter
     * @return matching objects
     */
    @ApiOperation("Search products by contained category.")
    @GetMapping(path = "/search-category", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Product>> searchProductsByCategory(@ApiParam(value = "product category", example = "Apple")
                                                                  @RequestParam(value = "category") String productCategory) {

        return ResponseEntity.ok(
                productDB.selectProductsByCategory(productCategory)
                        .stream().map(ProductEntity::mapToModel).collect(Collectors.toList()));
    }

    /**
     * POST
     *
     * @param productModel from request body
     * @return created object
     */
    @ApiOperation("Insert a new product into the database.")
    @PostMapping(path = "/insert", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> addProduct(@RequestBody Product productModel) {
        return ResponseEntity.ok(productDB.insertProduct(productModel).mapToModel());
    }

    /**
     * PUT
     *
     * @param productModel from request body
     * @return updated object
     * @throws ElementNotFoundException if id does not exist
     */
    @ApiOperation("Update product in the database.")
    @PutMapping(path = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> updateProduct(@RequestBody Product productModel) throws ElementNotFoundException {
        return ResponseEntity.ok(productDB.updateProduct(productModel).mapToModel());
    }

    /**
     * DELETE
     *
     * @param productId from request path
     * @return deleted object
     * @throws ElementNotFoundException if id does not exist
     */
    @ApiOperation("Delete product from the database by ID.")
    @PreAuthorize("hasRole('EDITOR') or hasRole('ADMIN')")
    @DeleteMapping(path = "/delete/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> deleteProduct(@ApiParam(value = "product db id", example = "1")
                                                 @PathVariable Integer productId) throws ElementNotFoundException {
        return ResponseEntity.ok(productDB.deleteProductById(productId).mapToModel());
    }

    // === OPTION REQUESTS =============================================================================================

    /**
     * GET
     *
     * @return existing categories
     */
    @ApiOperation("Get all existing product categories.")
    @GetMapping(path = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getCategories() {
        return ResponseEntity.ok(productDB.selectDistinctCategories());
    }

    /**
     * GET
     *
     * @return existing units
     */
    @ApiOperation("Get all existing product units.")
    @GetMapping(path = "/units", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Unit>> getUnits() {
        return ResponseEntity.ok(Unit.getAll());
    }
}
