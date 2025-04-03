package com.currenjin.domain.repository;

import com.currenjin.application.dto.ProductSearchDto;
import com.currenjin.domain.Product;
import com.currenjin.domain.Product.ProductStatus;
import com.currenjin.domain.QCategory;
import com.currenjin.domain.QProduct;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Product> findByCategoryName(String categoryName) {
        QProduct product = QProduct.product;
        QCategory category = QCategory.category;

        return queryFactory
                .selectFrom(product)
                .join(product.category, category)
                .where(category.name.eq(categoryName))
                .fetch();
    }

    @Override
    public List<Product> findByPriceRange(int minPrice, int maxPrice) {
        QProduct product = QProduct.product;

        return queryFactory
                .selectFrom(product)
                .where(
                        product.price.goe(minPrice),
                        product.price.loe(maxPrice)
                )
                .fetch();
    }

    @Override
    public List<Product> findByNameContainingAndStatus(String name, ProductStatus status) {
        QProduct product = QProduct.product;

        return queryFactory
                .selectFrom(product)
                .where(
                        product.name.contains(name),
                        product.status.eq(status)
                )
                .fetch();
    }

    @Override
    public List<Product> searchProducts(ProductSearchDto searchDto) {
        QProduct product = QProduct.product;
        QCategory category = QCategory.category;

        return queryFactory
                .selectFrom(product)
                .leftJoin(product.category, category)
                .where(
                        nameContains(searchDto.getName()),
                        priceGoe(searchDto.getMinPrice()),
                        priceLoe(searchDto.getMaxPrice()),
                        statusEq(searchDto.getStatus()),
                        categoryNameEq(searchDto.getCategoryName()),
                        inStockEq(searchDto.getInStock())
                )
                .fetch();
    }

    private BooleanExpression nameContains(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        return QProduct.product.name.contains(name);
    }

    private BooleanExpression priceGoe(Integer minPrice) {
        if (minPrice == null) {
            return null;
        }
        return QProduct.product.price.goe(minPrice);
    }

    private BooleanExpression priceLoe(Integer maxPrice) {
        if (maxPrice == null) {
            return null;
        }
        return QProduct.product.price.loe(maxPrice);
    }

    private BooleanExpression statusEq(ProductStatus status) {
        if (status == null) {
            return null;
        }
        return QProduct.product.status.eq(status);
    }

    private BooleanExpression categoryNameEq(String categoryName) {
        if (categoryName == null || categoryName.isEmpty()) {
            return null;
        }
        return QCategory.category.name.eq(categoryName);
    }

    private BooleanExpression inStockEq(Boolean inStock) {
        if (inStock == null) {
            return null;
        }
        return inStock ? QProduct.product.stockQuantity.gt(0) : QProduct.product.stockQuantity.eq(0);
    }
}