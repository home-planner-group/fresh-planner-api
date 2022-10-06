package com.freshplanner.api.controller.model.authentication;

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
public class AuthModel {

    @ApiModelProperty(value = "UserEntity information: username", example = "Admin")
    private String username;

    @ApiModelProperty(value = "JWT token.", example = "header.payload.signature")
    private String jwt;

    @ApiModelProperty(value = "JWT type - default 'Bearer'.", example = "Bearer")
    private String jwtType;

    private List<RoleName> roles;

    public AuthModel(UserEntity user, String jwt, String jwtType) {
        this.username = user.getName();
        this.jwt = jwt;
        this.jwtType = jwtType;
        this.roles = user.getRoles().stream().map(RoleEntity::getName).collect(Collectors.toList());
    }
}
