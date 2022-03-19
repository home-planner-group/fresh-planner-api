package com.freshplanner.api.model.cart;

import com.freshplanner.api.database.enums.Unit;
import com.freshplanner.api.database.storage.StorageItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
public class CartModel {

    @ApiModelProperty(value = "Cart database id", example = "1")
    private Integer id;

    @ApiModelProperty(value = "Cart name", example = "Home")
    private String name;

    private List<String> users;

    private List<Item> items;

    // TODO cart consturctor
//    public CartModel(Storage storage) {
//        this.id = storage.getId();
//        this.name = "Work in progress!";
//        this.users = storage.getUsers().stream().map(User::getName).collect(Collectors.toList());
//        this.items = storage.getStorageItems().stream().map(Item::new).collect(Collectors.toList());
//    }

    @Data
    @ApiModel
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {

        @ApiModelProperty(value = "Generated database id", example = "1")
        private Integer productId;

        @ApiModelProperty(value = "Name of the product", example = "Apple")
        private String productName;

        @ApiModelProperty(value = "Product count in the storage", example = "1")
        private Float count;

        @ApiModelProperty(value = "Unit of the product in the package", example = "GRAM")
        private Unit unit;

        public Item(StorageItem item) {
            this.productId = item.getProduct().getId();
            this.productName = item.getProduct().getName();
            this.count = item.getCount();
            this.unit = item.getProduct().getUnit();
        }
    }
}
