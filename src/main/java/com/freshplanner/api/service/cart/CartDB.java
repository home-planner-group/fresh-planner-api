package com.freshplanner.api.service.cart;

import com.freshplanner.api.exception.ElementNotFoundException;
import com.freshplanner.api.exception.NoAccessException;
import com.freshplanner.api.model.cart.CartModel;
import com.freshplanner.api.model.cart.CartSummaryModel;
import com.freshplanner.api.service.product.ProductDB;
import com.freshplanner.api.service.user.User;
import com.freshplanner.api.service.user.UserDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Component
public class CartDB {

    private final CartRepo cartRepo;
    private final CartItemRepo cartItemRepo;
    private final ProductDB productDB;
    private final UserDB userDB;

    @Autowired
    public CartDB(CartRepo cartRepo, CartItemRepo cartItemRpo, ProductDB productDB, UserDB userDB) {
        this.cartRepo = cartRepo;
        this.cartItemRepo = cartItemRpo;
        this.productDB = productDB;
        this.userDB = userDB;
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
    public Cart selectCartById(String username, Integer cartId) throws ElementNotFoundException, NoAccessException {
        Cart cart = selectCartById(cartId);
        if (cart.containsUser(username)) {
            return cart;
        } else {
            throw new NoAccessException(username, Cart.class, cartId.toString());
        }
    }

    private Cart selectCartById(Integer cartId) throws ElementNotFoundException {
        Optional<Cart> cart = cartRepo.findById(cartId);
        if (cart.isPresent()) {
            return cart.get();
        } else {
            throw new ElementNotFoundException(Cart.class, cartId.toString());
        }
    }

    /**
     * SELECT cart WHERE user
     *
     * @param username as owner
     * @return list with result objects
     */
    public List<Cart> selectUserCarts(String username) {
        return cartRepo.findCartsByUsername(username);
    }

    private CartItem selectCartItemById(Integer cartId, Integer productId) throws ElementNotFoundException {
        CartItem.Key id = new CartItem.Key(cartId, productId);
        Optional<CartItem> item = cartItemRepo.findById(id);
        if (item.isPresent()) {
            return item.get();
        } else {
            throw new ElementNotFoundException(CartItem.class, id.toString());
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
    public Cart insertCart(String username, CartSummaryModel cartModel) throws ElementNotFoundException {
        User user = userDB.getUserByName(username);
        return cartRepo.save(new Cart(user, cartModel));
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
    public Cart insertCartItem(String username, int cartId, CartModel.Item cartItemModel) throws ElementNotFoundException, NoAccessException {
        Cart cart = selectCartById(username, cartId);
        CartItem item = cartItemRepo.save(new CartItem(
                cart,
                productDB.selectProductById(cartItemModel.getProductId()),
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
    public Cart updateAddUser(String usernameOwner, int cartId, String usernameMember) throws ElementNotFoundException, NoAccessException {
        Cart cart = selectCartById(usernameOwner, cartId);
        User user = userDB.getUserByName(usernameMember);
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
    public Cart updateRemoveUser(String usernameOwner, int cartId, String usernameMember) throws ElementNotFoundException, NoAccessException {
        Cart cart = selectCartById(usernameOwner, cartId);
        User user = userDB.getUserByName(usernameMember);
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
    public Cart updateCart(String username, CartModel cartModel) throws NoAccessException, ElementNotFoundException {
        Cart cart = selectCartById(username, cartModel.getId());
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
    public CartItem updateCartItem(String username, Integer cartId, CartModel.Item itemModel) throws ElementNotFoundException, NoAccessException {
        CartItem item = this.selectCartItemById(cartId, itemModel.getProductId());
        if (!item.getCart().containsUser(username)) {
            throw new NoAccessException(username, Cart.class, cartId.toString());
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
    public Cart deleteCartItem(String username, Integer cartId, Integer productId) throws ElementNotFoundException, NoAccessException {
        Cart cart = this.selectCartById(username, cartId);
        CartItem item = this.selectCartItemById(cartId, productId);
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
    public Cart deleteCartById(String username, Integer cartId) throws ElementNotFoundException, NoAccessException {
        Cart cart = this.selectCartById(username, cartId);
        cartRepo.delete(cart);
        return cart;
    }
}
