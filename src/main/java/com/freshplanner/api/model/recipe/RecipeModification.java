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
public class RecipeModification {

    @ApiModelProperty(value = "Name of the recipe", example = "Pasta with Pesto")
    private String name;

    @ApiModelProperty(value = "Category of the product", example = "Pasta")
    private String category;

    @ApiModelProperty(value = "Name of the recipe", example = "Cook and Eat")
    private String description;
}
