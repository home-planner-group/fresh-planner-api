package com.freshplanner.api.model.recipe;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@ApiModel
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeItemModification {

    @ApiModelProperty(value = "Recipe database id", example = "1")
    private Integer recipeId;

    @ApiModelProperty(value = "Product database id", example = "1")
    private Integer productId;

    @ApiModelProperty(value = "Item count in product unit", example = "1")
    private Float count;

    @ApiModelProperty(value = "Item description", example = "small")
    private String description;
}
