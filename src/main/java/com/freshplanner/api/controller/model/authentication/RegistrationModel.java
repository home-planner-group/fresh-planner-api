package com.freshplanner.api.controller.model.authentication;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationModel {

    @NotBlank
    @Size(min = 5, max = 25)
    @ApiModelProperty(value = "Username for registration.", example = "MaxMastermind")
    private String username;

    @NotBlank
    @Size(min = 9, max = 50)
    @Email
    @ApiModelProperty(value = "Email for registration.", example = "Max.Mastermind@yahoo.de")
    private String email;

    @NotBlank
    @Size(min = 5, max = 50)
    @ApiModelProperty(value = "Password for registration.", example = "SafePassword")
    private String password;
}
