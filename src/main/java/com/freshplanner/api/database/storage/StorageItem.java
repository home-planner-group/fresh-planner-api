package com.freshplanner.api.database.storage;

import com.freshplanner.api.database.product.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity(name = "StorageItem")
@Table(name = "storage_items")
@IdClass(StorageItem.Key.class)
public class StorageItem implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "storage_id", referencedColumnName = "id", nullable = false)
    private Storage storage;

    @Id
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    private Product product;

    @Column(name = "count", nullable = false)
    private Float count;

    public StorageItem setCount(Float count) {
        this.count = count;
        return this;
    }

    // === OBJECT DEFAULT METHODS ======================================================================================

    @Override
    public String toString() {
        return "StorageItem{" +
                "storageId=" + storage.getId() +
                ", productId=" + product.getId() +
                ", count=" + count + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || Hibernate.getClass(this) != Hibernate.getClass(obj)) return false;
        StorageItem storageItem = (StorageItem) obj;
        return storage != null && Objects.equals(storage.getId(), storageItem.storage.getId())
                && product != null && Objects.equals(product.getId(), storageItem.product.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(storage.getId(), product.getId());
    }

    // === ID CLASS ====================================================================================================

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Key implements Serializable {
        private Integer storage;
        private Integer product;
    }
}
