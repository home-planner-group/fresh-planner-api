package com.freshplanner.api.service.product;

import com.freshplanner.api.controller.model.Product;
import com.freshplanner.api.exception.ElementNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Component
public class ProductDB implements ProductService {

    private final ProductRepo productRepo;

    @Autowired
    public ProductDB(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    // === SELECT ======================================================================================================

    /**
     * SELECT product WHERE productId
     *
     * @param productId database id
     * @return result object
     * @throws ElementNotFoundException if id does not exist
     */
    @Override
    public ProductEntity selectProductById(Integer productId) throws ElementNotFoundException {
        Optional<ProductEntity> product = productRepo.findById(productId);
        if (product.isPresent()) {
            return product.get();
        } else {
            throw new ElementNotFoundException(ProductEntity.class, productId.toString());
        }
    }

    /**
     * SELECT product WHERE LIKE productName
     *
     * @param productName partial name
     * @return list with result objects
     */
    public List<ProductEntity> selectProductsByName(String productName) {
        return productRepo.searchByName(productName);
    }

    /**
     * SELECT product WHERE LIKE productCategory
     *
     * @param productCategory partial name
     * @return list with result objects
     */
    public List<ProductEntity> selectProductsByCategory(String productCategory) {
        return productRepo.searchByCategory(productCategory);
    }

    /**
     * SELECT product
     *
     * @return list with all objects
     */
    public List<ProductEntity> selectAllProducts() {
        return productRepo.findAll();
    }

    /**
     * SELECT DISTINCT product-category
     *
     * @return list with all objects
     */
    public List<String> selectDistinctCategories() {
        return productRepo.findAllCategories();
    }

    // === INSERT ======================================================================================================

    /**
     * INSERT product
     *
     * @param productModel with input data
     * @return created object
     */
    @Transactional
    public ProductEntity insertProduct(Product productModel) {
        return productRepo.save(new ProductEntity(productModel));
    }


    // === UPDATE ======================================================================================================

    /**
     * UPDATE product
     *
     * @param productModel with input data
     * @return updated object
     * @throws ElementNotFoundException if id does not exist
     */
    @Transactional
    public ProductEntity updateProduct(Product productModel) throws ElementNotFoundException {
        ProductEntity product = this.selectProductById(productModel.getId());
        return productRepo.save(product.update(productModel));
    }

    // === DELETE ======================================================================================================

    /**
     * DELETE product WHERE productId
     *
     * @param productId database id
     * @return deleted object
     * @throws ElementNotFoundException if id does not exist
     */
    public ProductEntity deleteProductById(Integer productId) throws ElementNotFoundException {
        ProductEntity product = this.selectProductById(productId);
        productRepo.delete(product);
        return product;
    }
}
