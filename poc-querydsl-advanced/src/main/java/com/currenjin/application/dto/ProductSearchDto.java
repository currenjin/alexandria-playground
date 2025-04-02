package com.currenjin.application.dto;

import com.currenjin.domain.Product.ProductStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductSearchDto {
    private String name;
    private Integer minPrice;
    private Integer maxPrice;
    private ProductStatus status;
    private String categoryName;
    private Boolean inStock;
}