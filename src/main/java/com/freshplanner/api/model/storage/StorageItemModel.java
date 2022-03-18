package com.freshplanner.api.model.storage;

import com.freshplanner.api.database.storage.StorageItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@ApiModel
@Getter
@NoArgsConstructor
public class StorageItemModel {

    @ApiModelProperty(value = "Storage database id", example = "1")
    private Integer storageId;

    @ApiModelProperty(value = "Product database id", example = "1")
    private Integer productId;

    @ApiModelProperty(value = "Product name", example = "Apple")
    private String productName;

    @ApiModelProperty(value = "Product count in the storage", example = "1")
    private Float count;

    public StorageItemModel(StorageItem item) {
        this.storageId = item.getStorage().getId();
        this.productId = item.getProduct().getId();
        this.productName = item.getProduct().getName();
        this.count = item.getCount();
    }
}
