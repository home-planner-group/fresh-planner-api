package com.freshplanner.api.model.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@ApiModel
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JwtModel {

    @ApiModelProperty(value = "JWT token.", example = "header.payload.signature")
    private String token;

    @ApiModelProperty(value = "JWT type - default 'Bearer'.", example = "Bearer")
    private String tokenType;

    private UserModel userModel;
}
