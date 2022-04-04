package com.freshplanner.api.controller;

import com.freshplanner.api.database.cart.CartDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartDB cartDB;

    @Autowired
    public CartController(CartDB cartDB) {
        this.cartDB = cartDB;
    }


}
