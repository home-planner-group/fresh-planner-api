package com.freshplanner.api.database.recipe;

import com.freshplanner.api.model.recipe.RecipeModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity(name = "Recipe")
@Table(name = "recipes")
public class Recipe {

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

    public Recipe(RecipeModel recipe) {
        // id gets generated
        this.name = recipe.getName();
        this.description = recipe.getDescription();
        this.category = recipe.getCategory();
    }

    // === OBJECT DEFAULT METHODS ======================================================================================


    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
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
