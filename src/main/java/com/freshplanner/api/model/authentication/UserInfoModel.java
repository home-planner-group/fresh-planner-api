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
public class UserInfoModel {

    @ApiModelProperty(value = "User information: username", example = "Admin")
    private String username;

    @ApiModelProperty(value = "User information: email", example = "steinke.felix@yahoo.de")
    private String email;

    @ApiModelProperty(value = "User information: roles")
    private List<RoleName> roles;

    public UserInfoModel(User user) {
        this.username = user.getName();
        this.email = user.getEmail();
        this.roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
    }
}
