package com.freshplanner.api.database.storage;

import com.freshplanner.api.database.product.Product;
import com.freshplanner.api.model.storage.StorageModel;
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

    public StorageItem update(StorageModel.Item model) {
        if (model.getCount() != null && model.getCount() >= 0) {
            this.count = model.getCount();
        } else {
            this.count = 0f;
        }
        return this;
    }

    @Override
    public String toString() {
        return "StorageItem{" +
                "storage=" + storage.toString() +
                ", product=" + product.toString() +
                ", count=" + count +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StorageItem that = (StorageItem) o;

        if (!Objects.equals(storage, that.storage)) return false;
        return Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        int result = storage != null ? storage.hashCode() : 0;
        result = 31 * result + (product != null ? product.hashCode() : 0);
        return result;
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
