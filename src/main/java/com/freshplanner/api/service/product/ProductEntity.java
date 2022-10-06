package com.freshplanner.api.service.product;

import com.freshplanner.api.controller.model.Product;
import com.freshplanner.api.enums.Unit;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@ToString
@Entity(name = "Product")
@Table(name = "products")
public class ProductEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "category")
    private String category;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "unit", nullable = false)
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

    // === OBJECT CONSTRUCTION =========================================================================================

    public ProductEntity(Product product) {
        this.id = null; // id gets generated
        this.name = product.getName();
        this.category = product.getCategory();
        this.unit = product.getUnit();
        this.packageSize = product.getPackageSize();
        this.kcal = product.getKcal();
        this.carbohydrates = product.getCarbohydrates();
        this.protein = product.getProtein();
        this.fat = product.getFat();
    }

    public Product mapToModel() {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setCategory(category);
        product.setUnit(unit);
        product.setPackageSize(packageSize);
        product.setKcal(kcal);
        product.setCarbohydrates(carbohydrates);
        product.setProtein(protein);
        product.setFat(fat);
        return product;
    }

    // === OBJECT CHANGES ==============================================================================================

    public ProductEntity update(Product model) {
        // ignore id changes
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

    // === OBJECT ACCESS ===============================================================================================

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public Unit getUnit() {
        return unit;
    }

    public Float getPackageSize() {
        return packageSize;
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

    // === OBJECT DEFAULTS =============================================================================================

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
