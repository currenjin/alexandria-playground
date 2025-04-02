package com.currenjin.presentation;

import com.currenjin.application.dto.ProductSearchDto;
import com.currenjin.application.service.ProductService;
import com.currenjin.domain.Product;
import com.currenjin.domain.Product.ProductStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/api/products")
    public CreateProductResponse saveProduct(@RequestBody CreateProductRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .stockQuantity(request.getStockQuantity())
                .status(request.getStockQuantity() > 0 ? ProductStatus.AVAILABLE : ProductStatus.OUT_OF_STOCK)
                .build();

        Long id = productService.saveProduct(product);
        return new CreateProductResponse(id);
    }

    @GetMapping("/api/products")
    public List<ProductDto> getProducts() {
        List<Product> products = productService.findProducts();
        return products.stream()
                .map(ProductDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/products/category/{categoryName}")
    public List<ProductDto> getProductsByCategory(@PathVariable String categoryName) {
        List<Product> products = productService.findByCategoryName(categoryName);
        return products.stream()
                .map(ProductDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/products/price-range")
    public List<ProductDto> getProductsByPriceRange(
            @RequestParam int minPrice,
            @RequestParam int maxPrice) {
        List<Product> products = productService.findByPriceRange(minPrice, maxPrice);
        return products.stream()
                .map(ProductDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/products/search")
    public List<ProductDto> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) ProductStatus status) {
        List<Product> products = productService.findByNameContainingAndStatus(name, status);
        return products.stream()
                .map(ProductDto::new)
                .collect(Collectors.toList());
    }

    @PostMapping("/api/products/advanced-search")
    public List<ProductDto> advancedSearch(@RequestBody ProductSearchDto searchDto) {
        List<Product> products = productService.searchProducts(searchDto);
        return products.stream()
                .map(ProductDto::new)
                .collect(Collectors.toList());
    }

    @Data
    static class CreateProductRequest {
        private String name;
        private int price;
        private int stockQuantity;
    }

    @Data
    static class CreateProductResponse {
        private Long id;

        public CreateProductResponse(Long id) {
            this.id = id;
        }
    }

    @Data
    static class ProductDto {
        private Long id;
        private String name;
        private int price;
        private int stockQuantity;
        private ProductStatus status;
        private String categoryName;

        public ProductDto(Product product) {
            this.id = product.getId();
            this.name = product.getName();
            this.price = product.getPrice();
            this.stockQuantity = product.getStockQuantity();
            this.status = product.getStatus();
            this.categoryName = product.getCategory() != null ? product.getCategory().getName() : null;
        }
    }
}