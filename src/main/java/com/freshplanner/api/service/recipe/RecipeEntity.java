package com.freshplanner.api.service.recipe;

import com.freshplanner.api.controller.model.Recipe;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@NoArgsConstructor
@Getter
@Entity(name = "Recipe")
@Table(name = "recipes")
public class RecipeEntity implements Serializable {

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "recipe", orphanRemoval = true)
    private final Set<RecipeItemEntity> recipeItems = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "category")
    private String category;

    @Column(name = "description")
    private String description;

    @Column(name = "duration")
    private Integer duration;

    // === OBJECT CONSTRUCTION =========================================================================================

    public RecipeEntity(Recipe recipe) {
        this.id = null; // id gets generated
        this.name = recipe.getName();
        this.category = recipe.getCategory();
        this.duration = recipe.getDuration();
        this.description = recipe.getDescription();
    }

    public Recipe mapToModel() {
        Recipe recipe = new Recipe();
        recipe.setId(id);
        recipe.setName(name);
        recipe.setCategory(category);
        recipe.setDuration(duration);
        recipe.setDescription(description);
        float kcal = 0f;
        float carbohydrates = 0f;
        float protein = 0f;
        float fat = 0f;
        List<Recipe.Item> items = new ArrayList<>();
        for (RecipeItemEntity item : recipeItems) {
            kcal += item.getKcal();
            carbohydrates += item.getCarbohydrates();
            protein += item.getProtein();
            fat += item.getFat();
            items.add(item.mapToModel());
        }
        recipe.setKcal(kcal);
        recipe.setCarbohydrates(carbohydrates);
        recipe.setProtein(protein);
        recipe.setFat(fat);
        recipe.setItems(items);
        return recipe;

    }

    // === OBJECT CHANGES ==============================================================================================

    public RecipeEntity update(Recipe model) {
        // ignore id changes
        if (model.getName() != null) {
            this.name = model.getName();
        }
        if (model.getCategory() != null) {
            this.category = model.getCategory();
        }
        this.description = model.getDescription();
        this.duration = model.getDuration();
        return this;
    }

    // === OBJECT DEFAULTS =============================================================================================

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", duration=" + duration +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecipeEntity recipe = (RecipeEntity) o;

        if (!Objects.equals(id, recipe.id)) return false;
        return Objects.equals(name, recipe.name);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
