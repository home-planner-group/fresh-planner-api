package com.freshplanner.api.controller.model;

import com.freshplanner.api.enums.RoleName;
import com.freshplanner.api.service.user.RoleEntity;
import com.freshplanner.api.service.user.UserEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @ApiModelProperty(value = "UserEntity information: username", example = "Admin")
    private String username;

    @ApiModelProperty(value = "UserEntity information: email", example = "steinke.felix@yahoo.de")
    private String email;

    @ApiModelProperty(value = "UserEntity information: roles")
    private List<RoleName> roles;

    public User(UserEntity user) {
        this.username = user.getName();
        this.email = user.getEmail();
        this.roles = user.getRoles().stream().map(RoleEntity::getName).collect(Collectors.toList());
    }
}
