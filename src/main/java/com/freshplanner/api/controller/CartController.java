package com.freshplanner.api.controller;

import com.freshplanner.api.database.cart.CartDB;
import com.freshplanner.api.exception.ElementNotFoundException;
import com.freshplanner.api.exception.NoAccessException;
import com.freshplanner.api.model.cart.CartModel;
import com.freshplanner.api.model.cart.CartSummaryModel;
import com.freshplanner.api.security.SecurityContext;
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
@RequestMapping("/cart")
public class CartController {

    private final CartDB cartDB;

    @Autowired
    public CartController(CartDB cartDB) {
        this.cartDB = cartDB;
    }

    // === POST ========================================================================================================

    @PreAuthorize("hasRole('USER') or hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Insert a new cart for the user.")
    @PostMapping(path = "/insert", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CartModel> addCart(@RequestBody CartSummaryModel cartModel) throws ElementNotFoundException {
        String username = SecurityContext.extractUsername();

        return ResponseEntity.ok(new CartModel(
                cartDB.insertCart(username, cartModel)));
    }

    @PreAuthorize("hasRole('USER') or hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Insert a new cart item into the database. User validation for cart ownership.")
    @PostMapping(path = "/insert-item/{cartId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CartModel> addCartItem(@ApiParam(value = "cart db id", example = "1")
                                                 @PathVariable Integer cartId,
                                                 @RequestBody CartModel.Item item) throws ElementNotFoundException, NoAccessException {
        String username = SecurityContext.extractUsername();

        return ResponseEntity.ok(new CartModel(
                cartDB.insertCartItem(username, cartId, item)));
    }

    // === GET =========================================================================================================

    @PreAuthorize("hasRole('USER') or hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Get all carts from the user.")
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CartSummaryModel>> getUserCarts() {
        String username = SecurityContext.extractUsername();

        return ResponseEntity.ok(
                cartDB.selectUserCarts(username)
                        .stream().map(CartSummaryModel::new).collect(Collectors.toList()));
    }

    @PreAuthorize("hasRole('USER') or hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Get cart by database ID. User validation for cart ownership.")
    @GetMapping(path = "/get/{cartId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CartModel> getCartById(@ApiParam(value = "cart db id", example = "1")
                                                 @PathVariable Integer cartId) throws ElementNotFoundException, NoAccessException {
        String username = SecurityContext.extractUsername();

        return ResponseEntity.ok(new CartModel(
                cartDB.selectCartById(username, cartId)));
    }

    // === PUT =========================================================================================================

    @PreAuthorize("hasRole('USER') or hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Update cart with additional user. User validation for cart ownership.")
    @PutMapping(path = "/add-user/{cartId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CartModel> addUser(@ApiParam(value = "cart db id", example = "1")
                                             @PathVariable Integer cartId,
                                             @ApiParam(value = "username", example = "Max")
                                             @RequestParam String username) throws ElementNotFoundException, NoAccessException {
        String usernameOwner = SecurityContext.extractUsername();

        return ResponseEntity.ok(new CartModel(
                cartDB.updateAddUser(usernameOwner, cartId, username)));
    }

    @PreAuthorize("hasRole('USER') or hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Update cart by removing user. User validation for cart ownership.")
    @PutMapping(path = "/remove-user/{cartId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CartModel> removeUser(@ApiParam(value = "cart db id", example = "1")
                                                @PathVariable Integer cartId,
                                                @ApiParam(value = "username", example = "Max")
                                                @RequestParam String username) throws ElementNotFoundException, NoAccessException {
        String usernameOwner = SecurityContext.extractUsername();

        return ResponseEntity.ok(new CartModel(
                cartDB.updateRemoveUser(usernameOwner, cartId, username)));
    }

    @PreAuthorize("hasRole('USER') or hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Update cart in the database. User validation for cart ownership.")
    @PutMapping(path = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CartModel> updateCart(@RequestBody CartModel cartModel) throws ElementNotFoundException, NoAccessException {
        String usernameOwner = SecurityContext.extractUsername();

        return ResponseEntity.ok(new CartModel(
                cartDB.updateCart(usernameOwner, cartModel)));
    }

    @PreAuthorize("hasRole('USER') or hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Update cart item in the database. User validation for cart ownership.")
    @PutMapping(path = "/update-item/{cartId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CartModel.Item> updateCartItem(@ApiParam(value = "cart db id", example = "1")
                                                         @PathVariable Integer cartId,
                                                         @RequestBody CartModel.Item itemModel) throws ElementNotFoundException, NoAccessException {
        String username = SecurityContext.extractUsername();

        return ResponseEntity.ok(new CartModel.Item(
                cartDB.updateCartItem(username, cartId, itemModel)));
    }

    // === DELETE ======================================================================================================

    @PreAuthorize("hasRole('USER') or hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Delete cart item from the database by IDs. User validation for cart ownership.")
    @DeleteMapping(path = "/delete-item/{cartId}/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CartModel> deleteCartItem(@ApiParam(value = "cart db id", example = "1")
                                                    @PathVariable Integer cartId,
                                                    @ApiParam(value = "product db id", example = "1")
                                                    @PathVariable Integer productId) throws ElementNotFoundException, NoAccessException {
        String username = SecurityContext.extractUsername();

        return ResponseEntity.ok(new CartModel(
                cartDB.deleteCartItem(username, cartId, productId)));
    }

    @PreAuthorize("hasRole('USER') or hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Delete cart from the database by ID. User validation for cart ownership - Admin exception.")
    @DeleteMapping(path = "/delete/{cartId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CartModel> deleteCart(@ApiParam(value = "cart db id", example = "1")
                                                @PathVariable Integer cartId) throws ElementNotFoundException, NoAccessException {
        String username = SecurityContext.extractUsername();

        return ResponseEntity.ok(new CartModel(
                cartDB.deleteCartById(username, cartId)));
    }
}
