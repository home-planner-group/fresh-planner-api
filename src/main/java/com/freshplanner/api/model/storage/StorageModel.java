package com.freshplanner.api.model.storage;

import com.freshplanner.api.database.storage.Storage;
import com.freshplanner.api.model.user.UserModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@ApiModel
@Getter
@NoArgsConstructor
public class StorageModel {

    @ApiModelProperty(value = "Storage database id", example = "1")
    private Integer id;

    private List<StorageItemModel> items;

    private List<UserModel> users;

    public StorageModel(Storage storage) {
        this.id = storage.getId();
        this.items = storage.getStorageItems().stream().map(StorageItemModel::new).collect(Collectors.toList());
        this.users = storage.getUsers().stream().map(UserModel::new).collect(Collectors.toList());
    }
}
