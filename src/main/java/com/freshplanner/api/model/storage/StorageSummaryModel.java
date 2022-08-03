package com.freshplanner.api.model.storage;

import com.freshplanner.api.service.storage.Storage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
public class StorageSummaryModel {

    @ApiModelProperty(value = "Storage database id", example = "1")
    private Integer id;

    @ApiModelProperty(value = "Storage name", example = "Home")
    private String name;

    @ApiModelProperty(value = "Count of owners", example = "1")
    private Integer userCount;

    @ApiModelProperty(value = "Count of items", example = "1")
    private Integer itemCount;

    public StorageSummaryModel(Storage storage) {
        this.id = storage.getId();
        this.name = storage.getName();
        this.userCount = storage.getUsers().size();
        this.itemCount = storage.getStorageItems().size();
    }
}
