package com.vvi.btb.service.abs;

import com.vvi.btb.domain.request.ProductRequest;
import com.vvi.btb.domain.response.ProductRating;
import com.vvi.btb.domain.response.ProductResponse;
import com.vvi.btb.exception.domain.CategoryException;
import com.vvi.btb.exception.domain.ProductException;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    ProductResponse saveProduct(ProductRequest productRequest) throws ProductException, CategoryException;
    ProductResponse updateProduct(Long id, ProductRequest productRequest) throws ProductException, CategoryException;
    boolean deleteProduct(Long id) throws ProductException;
    List<ProductResponse> getAllProducts();
    Optional<ProductResponse> getProductByName(String productName) throws ProductException;
    Optional<ProductResponse> getProductDetail(Long id);

    ProductRating getProductRatings(Optional<ProductResponse> productResponse);
}