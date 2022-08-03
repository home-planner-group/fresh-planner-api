package com.freshplanner.api.model.product;

import com.freshplanner.api.service.enums.Unit;
import com.freshplanner.api.service.product.Product;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
public class ProductSummaryModel {

    @ApiModelProperty(value = "Generated database id", example = "1")
    private Integer id;

    @ApiModelProperty(value = "Name of the product", example = "Apple")
    private String name;

    @ApiModelProperty(value = "Category of the product", example = "Fruit")
    private String category;

    @ApiModelProperty(value = "Unit of the product in the package", example = "GRAM")
    private Unit unit;

    @ApiModelProperty(value = "Size of the product package in the unit", example = "500")
    private Float packageSize;

    public ProductSummaryModel(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.category = product.getCategory();
        this.unit = product.getUnit();
        this.packageSize = product.getPackageSize();
    }
}
