package com.freshplanner.api.database.product;

import com.freshplanner.api.exception.ElementNotFoundException;
import com.freshplanner.api.model.product.ProductModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public record ProductDB(ProductRepo productRepo) {

    /**
     * SELECT
     *
     * @param productId database id
     * @return result object
     * @throws ElementNotFoundException if id does not exist
     */
    public Product getProductById(Integer productId) throws ElementNotFoundException {
        Optional<Product> product = productRepo.findById(productId);
        if (product.isPresent()) {
            return product.get();
        } else {
            throw new ElementNotFoundException(Product.class, productId.toString());
        }
    }

    /**
     * SELECT
     *
     * @param productName partial product name
     * @return list with matched results
     */
    public List<Product> searchProductByName(String productName) {
        return productRepo.searchByName(productName);
    }

    /**
     * SELECT
     *
     * @return all objects from the database
     */
    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    /**
     * INSERT
     *
     * @param productModel with input data
     * @return created object
     */
    public Product addProduct(ProductModel productModel) {
        return productRepo.save(new Product(productModel));
    }

    /**
     * UPDATE
     *
     * @param productId    database id
     * @param modification with input data
     * @return updated object
     * @throws ElementNotFoundException if id does not exist
     */
    public Product updateProduct(Integer productId, ProductModel modification) throws ElementNotFoundException {
        Product product = this.getProductById(productId);
        return productRepo.save(this.modifyProduct(product, modification));
    }


    /**
     * DELETE
     *
     * @param productId database id
     * @return deleted object
     * @throws ElementNotFoundException if id does not exist
     */
    public Product deleteProductById(Integer productId) throws ElementNotFoundException {
        Product product = this.getProductById(productId);
        productRepo.delete(product);
        return product;
    }

    // === UTILITY =====================================================================================================

    /**
     * Updates the product with all NON-NULL fields of the modification.
     *
     * @param product      product to update
     * @param modification modification with new values
     * @return updated product
     */
    private Product modifyProduct(Product product, ProductModel modification) {
        if (modification.getName() != null) {
            product.setName(modification.getName());
        }
        if (modification.getCategory() != null) {
            product.setCategory(modification.getCategory());
        }
        if (modification.getUnit() != null) {
            product.setUnit(modification.getUnit());
        }
        if (modification.getPackageSize() != null) {
            product.setPackageSize(modification.getPackageSize());
        }
        if (modification.getKcal() != null) {
            product.setKcal(modification.getKcal());
        }
        if (modification.getCarbohydrates() != null) {
            product.setCarbohydrates(modification.getCarbohydrates());
        }
        if (modification.getProtein() != null) {
            product.setProtein(modification.getProtein());
        }
        if (modification.getFat() != null) {
            product.setFat(modification.getFat());
        }
        return product;
    }
}
