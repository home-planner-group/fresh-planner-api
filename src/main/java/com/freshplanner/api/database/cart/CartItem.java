package com.freshplanner.api.database.cart;

import com.freshplanner.api.database.product.Product;
import com.freshplanner.api.model.cart.CartModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity(name = "CartItem")
@Table(name = "cart_items")
@IdClass(CartItem.Key.class)
public class CartItem {

    @Id
    @ManyToOne
    @JoinColumn(name = "cart_id", referencedColumnName = "id", nullable = false)
    private Cart cart;

    @Id
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    private Product product;

    @Column(name = "count", nullable = false)
    private Float count;

    public CartItem update(CartModel.Item model) {
        if (model.getCount() != null && model.getCount() >= 0) {
            this.count = model.getCount();
        } else {
            this.count = 0f;
        }
        return this;
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
