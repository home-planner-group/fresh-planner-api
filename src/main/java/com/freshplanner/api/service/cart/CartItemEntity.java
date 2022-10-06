package com.freshplanner.api.service.cart;

import com.freshplanner.api.controller.model.Cart;
import com.freshplanner.api.service.product.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "CartItem")
@Table(name = "cart_items")
@IdClass(CartItemEntity.Key.class)
public class CartItemEntity {

    @Id
    @ManyToOne
    @JoinColumn(name = "cart_id", referencedColumnName = "id", nullable = false)
    private CartEntity cart;

    @Id
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    private ProductEntity product;

    @Column(name = "count", nullable = false)
    private Float count;

    public Cart.Item mapToModel() {
        Cart.Item item = new Cart.Item();
        item.setProductId(product.getId());
        item.setProductName(product.getName());
        item.setCategory(product.getCategory());
        item.setPackageSize(product.getPackageSize());
        item.setCount(count);
        item.setUnit(product.getUnit());
        return item;
    }

    public CartItemEntity update(Cart.Item model) {
        if (model.getCount() != null && model.getCount() >= 0) {
            this.count = model.getCount();
        } else {
            this.count = 0f;
        }
        return this;
    }

    public CartEntity getCart() {
        return cart;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public Float getCount() {
        return count != null ? count : 0;
    }

    // === ID CLASS ====================================================================================================

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Key implements Serializable {
        private Integer cart;
        private Integer product;
    }
}
