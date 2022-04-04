package com.freshplanner.api.model.cart;

import com.freshplanner.api.database.cart.Cart;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
public class CartSummaryModel {

    @ApiModelProperty(value = "Cart database id", example = "1")
    private Integer id;

    @ApiModelProperty(value = "Cart name", example = "Home")
    private String name;

    private Integer userCount;

    private Integer itemCount;

    public CartSummaryModel(Cart cart) {
        this.id = cart.getId();
        this.name = cart.getName();
        this.userCount = cart.getUsers().size();
        this.itemCount = cart.getCartItems().size();
    }
}
