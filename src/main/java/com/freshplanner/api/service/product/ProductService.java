package com.freshplanner.api.service.product;

import com.freshplanner.api.exception.ElementNotFoundException;
import org.springframework.stereotype.Service;

@Service
public interface ProductService {
    /**
     * SELECT product WHERE productId
     *
     * @param productId database id
     * @return result object
     * @throws ElementNotFoundException if id does not exist
     */
    ProductEntity selectProductById(Integer productId) throws ElementNotFoundException;
}
