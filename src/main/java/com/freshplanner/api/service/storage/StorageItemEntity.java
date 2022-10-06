package com.freshplanner.api.service.storage;

import com.freshplanner.api.controller.model.Storage;
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
@Entity(name = "StorageItem")
@Table(name = "storage_items")
@IdClass(StorageItemEntity.Key.class)
public class StorageItemEntity implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "storage_id", referencedColumnName = "id", nullable = false)
    private StorageEntity storage;

    @Id
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    private ProductEntity product;

    @Column(name = "count", nullable = false)
    private Float count;

    public Storage.Item mapToModel() {
        Storage.Item item = new Storage.Item();
        item.setProductId(product.getId());
        item.setProductName(product.getName());
        item.setCategory(product.getCategory());
        item.setPackageSize(product.getPackageSize());
        item.setCount(count);
        item.setUnit(product.getUnit());
        return item;
    }

    public StorageItemEntity update(Storage.Item model) {
        if (model.getCount() != null && model.getCount() >= 0) {
            this.count = model.getCount();
        } else {
            this.count = 0f;
        }
        return this;
    }

    public StorageEntity getStorage() {
        return storage;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public Float getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "StorageItemEntity{" +
                "storage=" + storage.toString() +
                ", product=" + product.toString() +
                ", count=" + count +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StorageItemEntity that = (StorageItemEntity) o;

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
