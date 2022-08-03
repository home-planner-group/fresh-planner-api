package com.freshplanner.api.service.recipe;

import com.freshplanner.api.controller.model.Recipe;
import com.freshplanner.api.enums.Unit;
import com.freshplanner.api.service.product.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@Entity(name = "RecipeItem")
@Table(name = "recipe_items")
@IdClass(RecipeItemEntity.Key.class)
public class RecipeItemEntity implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    private RecipeEntity recipe;

    @Id
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private ProductEntity product;

    @Column(name = "count", nullable = false)
    private Float count;

    @Column(name = "description")
    private String description;

    public RecipeItemEntity(RecipeEntity recipe, ProductEntity product, Float count, String description) {
        this.recipe = recipe;
        this.product = product;
        this.count = (count != null && count >= 0) ? count : 0f;
        this.description = description;
    }

    public Recipe.Item mapToModel() {
        Recipe.Item item = new Recipe.Item();
        item.setProductId(product.getId());
        item.setProductName(product.getName());
        item.setCount(count);
        item.setUnit(product.getUnit());
        item.setDescription(description);
        return item;
    }

    public RecipeItemEntity update(Recipe.Item item) {
        // ignore recipe and product changes
        this.count = (item.getCount() != null && item.getCount() >= 0) ? item.getCount() : 0f;
        this.description = item.getDescription();
        return this;
    }

    public Integer getProductId() {
        return product.getId();
    }

    public Float getCount() {
        return count;
    }

    public Unit getUnit() {
        return product.getUnit();
    }

    public String getDescription() {
        return description;
    }

    public Float getKcal() {
        return product.getKcal() * count;
    }

    public Float getCarbohydrates() {
        return product.getCarbohydrates() * count;
    }

    public Float getProtein() {
        return product.getProtein() * count;
    }

    public Float getFat() {
        return product.getFat() * count;
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

        RecipeItemEntity that = (RecipeItemEntity) o;

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
