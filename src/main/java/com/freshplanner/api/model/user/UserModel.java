package com.freshplanner.api.model.user;

import com.freshplanner.api.database.enums.RoleName;
import com.freshplanner.api.database.user.Role;
import com.freshplanner.api.database.user.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@ApiModel
@Getter
@NoArgsConstructor
public class UserModel {

    @ApiModelProperty(value = "User information: username", example = "Admin")
    private String username;

    @ApiModelProperty(value = "User information: email", example = "steinke.felix@yahoo.de")
    private String email;

    @ApiModelProperty(value = "User information: roles")
    private List<RoleName> roles;

    public UserModel(User user) {
        this.username = user.getName();
        this.email = user.getEmail();
        this.roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
    }
}
