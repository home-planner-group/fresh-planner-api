package com.freshplanner.api.service.cart;

import com.freshplanner.api.controller.model.Cart;
import com.freshplanner.api.exception.ElementNotFoundException;
import com.freshplanner.api.exception.NoAccessException;
import com.freshplanner.api.service.product.ProductService;
import com.freshplanner.api.service.user.UserEntity;
import com.freshplanner.api.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Component
public class CartDB {

    private final CartRepo cartRepo;
    private final CartItemRepo cartItemRepo;
    private final ProductService productService;
    private final UserService userService;

    @Autowired
    public CartDB(CartRepo cartRepo, CartItemRepo cartItemRpo, ProductService productService, UserService userService) {
        this.cartRepo = cartRepo;
        this.cartItemRepo = cartItemRpo;
        this.productService = productService;
        this.userService = userService;
    }

    // === SELECT ======================================================================================================

    /**
     * SELECT cart WHERE cartId
     *
     * @param username as owner
     * @param cartId   database id
     * @return result object
     * @throws ElementNotFoundException if id does not exist
     * @throws NoAccessException        if user is no owner
     */
    public CartEntity selectCartById(String username, Integer cartId) throws ElementNotFoundException, NoAccessException {
        CartEntity cart = selectCartById(cartId);
        if (cart.containsUser(username)) {
            return cart;
        } else {
            throw new NoAccessException(username, CartEntity.class, cartId.toString());
        }
    }

    private CartEntity selectCartById(Integer cartId) throws ElementNotFoundException {
        Optional<CartEntity> cart = cartRepo.findById(cartId);
        if (cart.isPresent()) {
            return cart.get();
        } else {
            throw new ElementNotFoundException(CartEntity.class, cartId.toString());
        }
    }

    /**
     * SELECT cart WHERE user
     *
     * @param username as owner
     * @return list with result objects
     */
    public List<CartEntity> selectUserCarts(String username) {
        return cartRepo.findCartsByUsername(username);
    }

    private CartItemEntity selectCartItemById(Integer cartId, Integer productId) throws ElementNotFoundException {
        CartItemEntity.Key id = new CartItemEntity.Key(cartId, productId);
        Optional<CartItemEntity> item = cartItemRepo.findById(id);
        if (item.isPresent()) {
            return item.get();
        } else {
            throw new ElementNotFoundException(CartItemEntity.class, id.toString());
        }
    }

    // === INSERT ======================================================================================================

    /**
     * INSERT cart WITH user
     *
     * @param username  as owner
     * @param cartModel with input data
     * @return created object
     * @throws ElementNotFoundException if id does not exist
     */
    @Transactional
    public CartEntity insertCart(String username, Cart cartModel) throws ElementNotFoundException {
        UserEntity user = userService.getUserByName(username);
        return cartRepo.save(new CartEntity(user, cartModel));
    }

    /**
     * INSERT cartItem
     *
     * @param username      as owner
     * @param cartId        linked database id
     * @param cartItemModel with input data
     * @return updated object
     * @throws ElementNotFoundException if id does not exist
     * @throws NoAccessException        if user is no owner
     */
    @Transactional
    public CartEntity insertCartItem(String username, int cartId, Cart.Item cartItemModel) throws ElementNotFoundException, NoAccessException {
        CartEntity cart = selectCartById(username, cartId);
        CartItemEntity item = cartItemRepo.save(new CartItemEntity(
                cart,
                productService.selectProductById(cartItemModel.getProductId()),
                cartItemModel.getCount()));
        cart.getCartItems().add(item);
        return cart;
    }

    // === UPDATE ======================================================================================================

    /**
     * INSERT user INTO cart
     *
     * @param usernameOwner  as owner
     * @param cartId         database id
     * @param usernameMember new member
     * @return updated object
     * @throws ElementNotFoundException if id does not exist
     * @throws NoAccessException        if user is no owner
     */
    @Transactional
    public CartEntity updateAddUser(String usernameOwner, int cartId, String usernameMember) throws ElementNotFoundException, NoAccessException {
        CartEntity cart = selectCartById(usernameOwner, cartId);
        UserEntity user = userService.getUserByName(usernameMember);
        return cartRepo.save(cart.addUser(user));
    }

    /**
     * DELETE user FROM cart
     *
     * @param usernameOwner  as owner
     * @param cartId         database id
     * @param usernameMember member to delete
     * @return updated object
     * @throws ElementNotFoundException if id does not exist
     * @throws NoAccessException        if user is no owner
     */
    @Transactional
    public CartEntity updateRemoveUser(String usernameOwner, int cartId, String usernameMember) throws ElementNotFoundException, NoAccessException {
        CartEntity cart = selectCartById(usernameOwner, cartId);
        UserEntity user = userService.getUserByName(usernameMember);
        return cartRepo.save(cart.removeUser(user));
    }

    /**
     * UPDATE cart
     *
     * @param username  as owner
     * @param cartModel with input data
     * @return updated object
     * @throws NoAccessException        if user is no owner
     * @throws ElementNotFoundException if id does not exist
     */
    public CartEntity updateCart(String username, Cart cartModel) throws NoAccessException, ElementNotFoundException {
        CartEntity cart = selectCartById(username, cartModel.getId());
        return cartRepo.save(cart.update(cartModel));
    }

    /**
     * UPDATE cartItem
     *
     * @param username  as owner
     * @param cartId    linked database id
     * @param itemModel with input data
     * @return updated object
     * @throws ElementNotFoundException if id does not exist
     * @throws NoAccessException        if user is no owner
     */
    public CartItemEntity updateCartItem(String username, Integer cartId, Cart.Item itemModel) throws ElementNotFoundException, NoAccessException {
        CartItemEntity item = this.selectCartItemById(cartId, itemModel.getProductId());
        if (!item.getCart().containsUser(username)) {
            throw new NoAccessException(username, CartEntity.class, cartId.toString());
        }
        return cartItemRepo.save(item.update(itemModel));
    }

    // === DELETE ======================================================================================================

    /**
     * DELETE cartItem WHERE cartId and productId
     *
     * @param username  as owner
     * @param cartId    linked database id
     * @param productId linked database id
     * @return updated object
     * @throws ElementNotFoundException if id does not exist
     * @throws NoAccessException        if user is no owner
     */
    public CartEntity deleteCartItem(String username, Integer cartId, Integer productId) throws ElementNotFoundException, NoAccessException {
        CartEntity cart = this.selectCartById(username, cartId);
        CartItemEntity item = this.selectCartItemById(cartId, productId);
        cartItemRepo.delete(item);
        cart.getCartItems().remove(item);
        return cart;
    }

    /**
     * DELETE cart WHERE cartId
     *
     * @param username as owner
     * @param cartId   database id
     * @return deleted object
     * @throws ElementNotFoundException if id does not exist
     * @throws NoAccessException        if user is no owner
     */
    public CartEntity deleteCartById(String username, Integer cartId) throws ElementNotFoundException, NoAccessException {
        CartEntity cart = this.selectCartById(username, cartId);
        cartRepo.delete(cart);
        return cart;
    }
}
