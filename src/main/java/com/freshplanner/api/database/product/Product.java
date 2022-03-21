package com.freshplanner.api.database.product;

import com.freshplanner.api.database.enums.Unit;
import com.freshplanner.api.model.product.ProductModel;
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
public class Product implements Serializable {

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

    // === OBJECT DEFAULT METHODS ======================================================================================

    public Product(ProductModel product) {
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

    // === OBJECT DEFAULT METHODS ======================================================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

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
