package com.freshplanner.api.model.recipe;

import com.freshplanner.api.database.enums.Unit;
import com.freshplanner.api.database.recipe.RecipeItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@ApiModel
@Getter
@NoArgsConstructor
public class RecipeItemModel {

    @ApiModelProperty(value = "Recipe database id", example = "1")
    private Integer recipeId;

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

    public RecipeItemModel(RecipeItem item) {
        this.recipeId = item.getRecipe().getId();
        this.productId = item.getProduct().getId();
        this.productName = item.getProduct().getName();
        this.count = item.getCount();
        this.unit = item.getProduct().getUnit();
        this.description = item.getDescription();
    }
}
