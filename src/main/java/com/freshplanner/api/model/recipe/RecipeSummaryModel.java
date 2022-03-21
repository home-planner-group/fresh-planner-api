package com.freshplanner.api.model.recipe;

import com.freshplanner.api.database.recipe.Recipe;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
public class RecipeSummaryModel {

    @ApiModelProperty(value = "Recipe database id", example = "1")
    private Integer id;

    @ApiModelProperty(value = "Name of the recipe", example = "Pasta with Pesto")
    private String name;

    @ApiModelProperty(value = "Category of the product", example = "Pasta")
    private String category;

    @ApiModelProperty(value = "Duration of the recipe", example = "15")
    private Integer duration;

    public RecipeSummaryModel(Recipe recipe) {
        this.id = recipe.getId();
        this.name = recipe.getName();
        this.category = recipe.getCategory();
        this.duration = recipe.getDuration();
    }
}
