package com.xyz.order.management.domain.repository;

import com.xyz.order.management.domain.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
