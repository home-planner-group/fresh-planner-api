package com.freshplanner.api.controller;

import com.freshplanner.api.controller.model.Cart;
import com.freshplanner.api.exception.ElementNotFoundException;
import com.freshplanner.api.exception.NoAccessException;
import com.freshplanner.api.security.SecurityContext;
import com.freshplanner.api.service.cart.CartDB;
import com.freshplanner.api.service.cart.CartEntity;
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
    public ResponseEntity<Cart> addCart(@RequestBody Cart cartModel) throws ElementNotFoundException {
        String username = SecurityContext.extractUsername();

        return ResponseEntity.ok(cartDB.insertCart(username, cartModel).mapToModel());
    }

    @PreAuthorize("hasRole('USER') or hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Insert a new cart item into the database. User validation for cart ownership.")
    @PostMapping(path = "/insert-item/{cartId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Cart> addCartItem(@ApiParam(value = "cart db id", example = "1")
                                            @PathVariable Integer cartId,
                                            @RequestBody Cart.Item item) throws ElementNotFoundException, NoAccessException {
        String username = SecurityContext.extractUsername();

        return ResponseEntity.ok(cartDB.insertCartItem(username, cartId, item).mapToModel());
    }

    // === GET =========================================================================================================

    @PreAuthorize("hasRole('USER') or hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Get all carts from the user.")
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Cart>> getUserCarts() {
        String username = SecurityContext.extractUsername();

        return ResponseEntity.ok(
                cartDB.selectUserCarts(username)
                        .stream().map(CartEntity::mapToModel).collect(Collectors.toList()));
    }

    @PreAuthorize("hasRole('USER') or hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Get cart by database ID. User validation for cart ownership.")
    @GetMapping(path = "/get/{cartId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Cart> getCartById(@ApiParam(value = "cart db id", example = "1")
                                            @PathVariable Integer cartId) throws ElementNotFoundException, NoAccessException {
        String username = SecurityContext.extractUsername();

        return ResponseEntity.ok(cartDB.selectCartById(username, cartId).mapToModel());
    }

    // === PUT =========================================================================================================

    @PreAuthorize("hasRole('USER') or hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Update cart with additional user. User validation for cart ownership.")
    @PutMapping(path = "/add-user/{cartId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Cart> addUser(@ApiParam(value = "cart db id", example = "1")
                                        @PathVariable Integer cartId,
                                        @ApiParam(value = "username", example = "Max")
                                        @RequestParam String username) throws ElementNotFoundException, NoAccessException {
        String usernameOwner = SecurityContext.extractUsername();

        return ResponseEntity.ok(cartDB.updateAddUser(usernameOwner, cartId, username).mapToModel());
    }

    @PreAuthorize("hasRole('USER') or hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Update cart by removing user. User validation for cart ownership.")
    @PutMapping(path = "/remove-user/{cartId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Cart> removeUser(@ApiParam(value = "cart db id", example = "1")
                                           @PathVariable Integer cartId,
                                           @ApiParam(value = "username", example = "Max")
                                           @RequestParam String username) throws ElementNotFoundException, NoAccessException {
        String usernameOwner = SecurityContext.extractUsername();

        return ResponseEntity.ok(cartDB.updateRemoveUser(usernameOwner, cartId, username).mapToModel());
    }

    @PreAuthorize("hasRole('USER') or hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Update cart in the database. User validation for cart ownership.")
    @PutMapping(path = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Cart> updateCart(@RequestBody Cart cart) throws ElementNotFoundException, NoAccessException {
        String usernameOwner = SecurityContext.extractUsername();

        return ResponseEntity.ok(cartDB.updateCart(usernameOwner, cart).mapToModel());
    }

    @PreAuthorize("hasRole('USER') or hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Update cart item in the database. User validation for cart ownership.")
    @PutMapping(path = "/update-item/{cartId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Cart.Item> updateCartItem(@ApiParam(value = "cart db id", example = "1")
                                                    @PathVariable Integer cartId,
                                                    @RequestBody Cart.Item itemModel) throws ElementNotFoundException, NoAccessException {
        String username = SecurityContext.extractUsername();

        return ResponseEntity.ok(cartDB.updateCartItem(username, cartId, itemModel).mapToModel());
    }

    // === DELETE ======================================================================================================

    @PreAuthorize("hasRole('USER') or hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Delete cart item from the database by IDs. User validation for cart ownership.")
    @DeleteMapping(path = "/delete-item/{cartId}/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Cart> deleteCartItem(@ApiParam(value = "cart db id", example = "1")
                                               @PathVariable Integer cartId,
                                               @ApiParam(value = "product db id", example = "1")
                                               @PathVariable Integer productId) throws ElementNotFoundException, NoAccessException {
        String username = SecurityContext.extractUsername();

        return ResponseEntity.ok(cartDB.deleteCartItem(username, cartId, productId).mapToModel());
    }

    @PreAuthorize("hasRole('USER') or hasRole('EDITOR') or hasRole('ADMIN')")
    @ApiOperation("Delete cart from the database by ID. User validation for cart ownership - Admin exception.")
    @DeleteMapping(path = "/delete/{cartId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Cart> deleteCart(@ApiParam(value = "cart db id", example = "1")
                                           @PathVariable Integer cartId) throws ElementNotFoundException, NoAccessException {
        String username = SecurityContext.extractUsername();

        return ResponseEntity.ok(cartDB.deleteCartById(username, cartId).mapToModel());
    }
}
