package com.freshplanner.api.model.storage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@ApiModel
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StorageItemModification {

    @ApiModelProperty(value = "Storage database id", example = "1")
    private Integer storageId;

    @ApiModelProperty(value = "Product database id", example = "1")
    private Integer productId;

    @ApiModelProperty(value = "Product count in the storage", example = "1")
    private Float count;
}
