package com.currenjin.inventory.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.currenjin.inventory.domain.Product;
import com.currenjin.inventory.domain.repository.ProductRepository;

public interface ProductJPARepository extends ProductRepository, JpaRepository<Product, Long> {
}
