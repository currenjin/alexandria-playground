package com.currenjin.domain.repository;

import com.currenjin.domain.Product;
import com.currenjin.domain.Product.ProductStatus;
import com.currenjin.application.dto.ProductSearchDto;

import java.util.List;

public interface ProductRepositoryCustom {
    List<Product> findByCategoryName(String categoryName);
    List<Product> findByPriceRange(int minPrice, int maxPrice);
    List<Product> findByNameContainingAndStatus(String name, ProductStatus status);
    List<Product> searchProducts(ProductSearchDto searchDto);
}