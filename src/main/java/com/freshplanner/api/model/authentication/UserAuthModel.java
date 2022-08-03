package com.freshplanner.api.model.authentication;

import com.freshplanner.api.enums.RoleName;
import com.freshplanner.api.service.user.Role;
import com.freshplanner.api.service.user.User;
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
public class UserAuthModel {

    @ApiModelProperty(value = "User information: username", example = "Admin")
    private String username;

    @ApiModelProperty(value = "JWT token.", example = "header.payload.signature")
    private String jwt;

    @ApiModelProperty(value = "JWT type - default 'Bearer'.", example = "Bearer")
    private String jwtType;

    private List<RoleName> roles;

    public UserAuthModel(User user, String jwt, String jwtType) {
        this.username = user.getName();
        this.jwt = jwt;
        this.jwtType = jwtType;
        this.roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
    }
}
