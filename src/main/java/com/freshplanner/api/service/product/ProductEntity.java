package com.freshplanner.api.service.product;

import com.freshplanner.api.controller.model.Product;
import com.freshplanner.api.enums.Unit;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "Product")
@Table(name = "products")
public class ProductEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Setter(AccessLevel.NONE)
    private Integer id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "category")
    private String category;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "unit")
    private Unit unit;

    @Column(name = "package_size")
    private Float packageSize;

    @Column(name = "kcal")
    private Float kcal;

    @Column(name = "carbohydrates")
    private Float carbohydrates;

    @Column(name = "protein")
    private Float protein;

    @Column(name = "fat")
    private Float fat;

    public ProductEntity(Product product) {
        // id gets generated
        this.name = product.getName();
        this.category = product.getCategory();
        this.unit = product.getUnit();
        this.packageSize = product.getPackageSize();
        this.kcal = product.getKcal();
        this.carbohydrates = product.getCarbohydrates();
        this.protein = product.getProtein();
        this.fat = product.getFat();
    }

    public Product getModel() {
        return new Product(this);
    }

    public ProductEntity update(Product model) {
        if (model.getName() != null) {
            this.name = model.getName();
        }
        if (model.getCategory() != null) {
            this.category = model.getCategory();
        }
        if (model.getUnit() != null) {
            this.unit = model.getUnit();
        }
        if (model.getPackageSize() != null) {
            this.packageSize = model.getPackageSize();
        }
        this.kcal = model.getKcal();
        this.carbohydrates = model.getCarbohydrates();
        this.protein = model.getProtein();
        this.fat = model.getFat();
        return this;
    }

    public Float getKcal() {
        return kcal != null ? kcal : 0;
    }

    public Float getCarbohydrates() {
        return carbohydrates != null ? carbohydrates : 0;
    }

    public Float getProtein() {
        return protein != null ? protein : 0;
    }

    public Float getFat() {
        return fat != null ? fat : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductEntity product = (ProductEntity) o;

        if (!Objects.equals(id, product.id)) return false;
        return Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
