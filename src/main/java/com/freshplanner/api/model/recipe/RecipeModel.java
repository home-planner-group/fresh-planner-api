package com.freshplanner.api.model.recipe;

import com.freshplanner.api.database.product.Product;
import com.freshplanner.api.database.recipe.Recipe;
import com.freshplanner.api.database.recipe.RecipeItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@ApiModel
@Getter
@NoArgsConstructor
public class RecipeModel {

    @ApiModelProperty(value = "Recipe database id", example = "1")
    private Integer id;

    @ApiModelProperty(value = "Name of the recipe", example = "Pasta with Pesto")
    private String name;

    @ApiModelProperty(value = "Category of the product", example = "Pasta")
    private String category;

    @ApiModelProperty(value = "Name of the recipe", example = "Cook and Eat")
    private String description;

    private List<RecipeItemModel> items;

    @ApiModelProperty(value = "kCal per 100g", example = "400")
    private Float kcal;

    @ApiModelProperty(value = "Carbohydrates per 100g", example = "20")
    private Float carbohydrates;

    @ApiModelProperty(value = "Protein per 100g", example = "10")
    private Float protein;

    @ApiModelProperty(value = "Fat per 100g", example = "10")
    private Float fat;

    public RecipeModel(Recipe recipe) {
        this.id = recipe.getId();
        this.name = recipe.getName();
        this.category = recipe.getCategory();
        this.description = recipe.getDescription();
        this.items = recipe.getRecipeItems().stream().map(RecipeItemModel::new).collect(Collectors.toList());
        if (recipe.getRecipeItems().size() > 0) {
            this.kcal = 0f;
            this.carbohydrates = 0f;
            this.protein = 0f;
            this.fat = 0f;
            for (RecipeItem item : recipe.getRecipeItems()) {
                Product product = item.getProduct();
                this.kcal += product.getKcal() * item.getCount();
                this.carbohydrates += product.getCarbohydrates() * item.getCount();
                this.protein += product.getProtein() * item.getCount();
                this.fat += product.getFat() * item.getCount();
            }
        }
    }
}
