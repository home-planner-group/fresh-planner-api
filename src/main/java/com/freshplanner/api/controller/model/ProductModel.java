package com.freshplanner.api.controller.model;

import com.freshplanner.api.database.product.ProductEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductModel {

    @ApiModelProperty(value = "Generated database id", example = "1")
    private Integer id;

    @ApiModelProperty(value = "Name of the product", example = "Apple")
    private String name;

    public ProductModel(ProductEntity product) {
        this.id = product.getId();
        this.name = product.getName();
    }
}
