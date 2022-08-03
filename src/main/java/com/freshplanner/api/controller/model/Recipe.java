package com.freshplanner.api.controller.model;

import com.freshplanner.api.enums.Unit;
import com.freshplanner.api.service.recipe.RecipeEntity;
import com.freshplanner.api.service.recipe.RecipeItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

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
    private List<Item> items;

    public Recipe(RecipeEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.category = entity.getCategory();
        this.duration = entity.getDuration();
        this.description = entity.getDescription();
        this.items = entity.getRecipeItems().stream().map(Item::new).collect(Collectors.toList());
        if (entity.getRecipeItems().size() > 0) {
            this.kcal = 0f;
            this.carbohydrates = 0f;
            this.protein = 0f;
            this.fat = 0f;
            for (RecipeItem item : entity.getRecipeItems()) {
                this.kcal += item.getKcal();
                this.carbohydrates += item.getCarbohydrates();
                this.protein += item.getProtein();
                this.fat += item.getFat();
            }
        }
    }

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

        public Item(RecipeItem item) {
            this.productId = item.getProductId();
            this.productName = item.getProductName();
            this.count = item.getCount();
            this.unit = item.getUnit();
            this.description = item.getDescription();
        }
    }
}
