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
public class Recipe {

    @ApiModelProperty(value = "Recipe database id", example = "1")
    private Integer id;
    @ApiModelProperty(value = "Name of the recipe", example = "Pasta with Pesto")
    private String name;
    @ApiModelProperty(value = "Category of the product", example = "Pasta")
    private String category;
    @ApiModelProperty(value = "Duration of the recipe", example = "15")
    private Integer duration;
    @ApiModelProperty(value = "Name of the recipe", example = "Cook and Eat")
    private String description;
    @ApiModelProperty(value = "kCal per 100g", example = "400")
    private Float kcal;
    @ApiModelProperty(value = "Carbohydrates per 100g", example = "20")
    private Float carbohydrates;
    @ApiModelProperty(value = "Protein per 100g", example = "10")
    private Float protein;
    @ApiModelProperty(value = "Fat per 100g", example = "10")
    private Float fat;
    @ApiModelProperty(value = "List of items")
    private List<Item> items;

    @Data
    @ApiModel
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {

        @ApiModelProperty(value = "Product database id", example = "1")
        private Integer productId;
        @ApiModelProperty(value = "Product name", example = "Apple")
        private String productName;
        @ApiModelProperty(value = "Item count in product unit", example = "1")
        private Float count;
        @ApiModelProperty(value = "Item unit", example = "GRAM")
        private Unit unit;
        @ApiModelProperty(value = "Item description", example = "small")
        private String description;
    }
}
