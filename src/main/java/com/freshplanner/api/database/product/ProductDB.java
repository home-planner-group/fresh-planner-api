package com.freshplanner.api.database.product;

import com.freshplanner.api.exception.ElementNotFoundException;
import com.freshplanner.api.model.product.ProductModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Component
public class ProductDB {

    private final ProductRepo productRepo;

    @Autowired
    public ProductDB(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    /**
     * SELECT product WHERE productId
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
     * SELECT product WHERE LIKE productName
     *
     * @param productName partial product name
     * @return list with result objects
     */
    public List<Product> searchProductByName(String productName) {
        return productRepo.searchByName(productName);
    }

    /**
     * SELECT product
     *
     * @return list with all objects
     */
    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    /**
     * INSERT product
     *
     * @param productModel with input data
     * @return created object
     */
    @Transactional
    public Product addProduct(ProductModel productModel) {
        return productRepo.save(new Product(productModel));
    }

    // TODO implement updateProduct with PUT request
    public Product updateProduct(Integer productId, ProductModel modification) throws ElementNotFoundException {
        Product product = this.getProductById(productId);
        return productRepo.save(this.modifyProduct(product, modification));
    }

    /**
     * DELETE product WHERE productId
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
