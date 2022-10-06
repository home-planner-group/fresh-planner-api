package com.freshplanner.api.controller.model;

import com.freshplanner.api.enums.Unit;
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
public class Storage {

    @ApiModelProperty(value = "StorageEntity database id", example = "1")
    private Integer id;
    @ApiModelProperty(value = "StorageEntity name", example = "Home")
    private String name;
    @ApiModelProperty(value = "List of owners")
    private List<String> users;
    @ApiModelProperty(value = "List of items")
    private List<Item> items;

    @Data
    @ApiModel
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {

        @ApiModelProperty(value = "Generated database id", example = "1")
        private Integer productId;
        @ApiModelProperty(value = "Name of the product", example = "Product Name")
        private String productName;
        @ApiModelProperty(value = "Category of the product", example = "Category Name")
        private String category;
        @ApiModelProperty(value = "Package Size of the product in the unit", example = "500")
        private Float packageSize;
        @ApiModelProperty(value = "Product count in the storage", example = "1")
        private Float count;
        @ApiModelProperty(value = "Unit of the product in the package", example = "GRAM")
        private Unit unit;
    }
}
