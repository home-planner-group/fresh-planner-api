package com.freshplanner.api.controller;

import com.freshplanner.api.controller.model.ProductModel;
import com.freshplanner.api.database.product.ProductDB;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @ApiOperation("Get all products from the database.")
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductModel>> getAllProducts() {

        return ResponseEntity.ok(productDB.getAllProducts()
                .stream().map(ProductModel::new).collect(Collectors.toList()));
    }
}
