package com.freshplanner.api.model.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank
    @Size(min = 5, max = 25)
    @ApiModelProperty(value = "Username for login.", example = "MaxMastermind")
    private String username;

    @NotBlank
    @Size(min = 5, max = 50)
    @ApiModelProperty(value = "Password for login.", example = "SafePassword")
    private String password;
}
