package com.freshplanner.api.model.recipe;

import com.freshplanner.api.enums.Unit;
import com.freshplanner.api.service.product.ProductEntity;
import com.freshplanner.api.service.recipe.Recipe;
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
public class RecipeModel {

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

    public RecipeModel(Recipe recipe) {
        this.id = recipe.getId();
        this.name = recipe.getName();
        this.category = recipe.getCategory();
        this.duration = recipe.getDuration();
        this.description = recipe.getDescription();
        this.items = recipe.getRecipeItems().stream().map(Item::new).collect(Collectors.toList());
        if (recipe.getRecipeItems().size() > 0) {
            this.kcal = 0f;
            this.carbohydrates = 0f;
            this.protein = 0f;
            this.fat = 0f;
            for (RecipeItem item : recipe.getRecipeItems()) {
                ProductEntity product = item.getProduct();
                this.kcal += product.getKcal() * item.getCount();
                this.carbohydrates += product.getCarbohydrates() * item.getCount();
                this.protein += product.getProtein() * item.getCount();
                this.fat += product.getFat() * item.getCount();
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
            this.productId = item.getProduct().getId();
            this.productName = item.getProduct().getName();
            this.count = item.getCount();
            this.unit = item.getProduct().getUnit();
            this.description = item.getDescription();
        }
    }
}
