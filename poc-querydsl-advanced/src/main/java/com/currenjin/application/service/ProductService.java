package com.currenjin.application.service;

import com.currenjin.application.dto.ProductSearchDto;
import com.currenjin.domain.Product;
import com.currenjin.domain.Product.ProductStatus;
import com.currenjin.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public Long saveProduct(Product product) {
        productRepository.save(product);
        return product.getId();
    }

    public List<Product> findProducts() {
        return productRepository.findAll();
    }

    public Product findOne(Long productId) {
        return productRepository.findById(productId).orElse(null);
    }

    public List<Product> findByCategoryName(String categoryName) {
        return productRepository.findByCategoryName(categoryName);
    }

    public List<Product> findByPriceRange(int minPrice, int maxPrice) {
        return productRepository.findByPriceRange(minPrice, maxPrice);
    }

    public List<Product> findByNameContainingAndStatus(String name, ProductStatus status) {
        return productRepository.findByNameContainingAndStatus(name, status);
    }

    public List<Product> searchProducts(ProductSearchDto searchDto) {
        return productRepository.searchProducts(searchDto);
    }
}