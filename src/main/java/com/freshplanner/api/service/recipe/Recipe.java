package com.freshplanner.api.service.recipe;

import com.freshplanner.api.model.recipe.RecipeModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity(name = "Recipe")
@Table(name = "recipes")
public class Recipe implements Serializable {

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "recipe", orphanRemoval = true)
    private final Set<RecipeItem> recipeItems = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Setter(AccessLevel.NONE)
    private Integer id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "category")
    private String category;

    @Column(name = "description")
    private String description;

    @Column(name = "duration")
    private Integer duration;

    public Recipe(RecipeModel recipe) {
        // id gets generated
        this.name = recipe.getName();
        this.category = recipe.getCategory();
        this.duration = recipe.getDuration();
        this.description = recipe.getDescription();
    }

    public Recipe update(RecipeModel model) {
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

        Recipe recipe = (Recipe) o;

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
