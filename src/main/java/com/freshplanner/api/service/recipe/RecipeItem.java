package com.freshplanner.api.service.recipe;

import com.freshplanner.api.model.recipe.RecipeModel;
import com.freshplanner.api.service.product.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity(name = "RecipeItem")
@Table(name = "recipe_items")
@IdClass(RecipeItem.Key.class)
public class RecipeItem implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    private Recipe recipe;

    @Id
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private ProductEntity product;

    @Column(name = "count", nullable = false)
    private Float count;

    @Column(name = "description")
    private String description;

    public RecipeItem update(RecipeModel.Item model) {
        if (model.getCount() != null && model.getCount() >= 0) {
            this.count = model.getCount();
        } else {
            this.count = 0f;
        }
        this.description = model.getDescription();
        return this;
    }

    @Override
    public String toString() {
        return "RecipeItem{" +
                "recipe=" + recipe.toString() +
                ", product=" + product.toString() +
                ", count=" + count +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecipeItem that = (RecipeItem) o;

        if (!Objects.equals(recipe, that.recipe)) return false;
        return Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        int result = recipe != null ? recipe.hashCode() : 0;
        result = 31 * result + (product != null ? product.hashCode() : 0);
        return result;
    }

    // === ID CLASS ====================================================================================================

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Key implements Serializable {
        private Integer recipe;
        private Integer product;
    }
}
