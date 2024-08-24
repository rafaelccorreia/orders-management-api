package com.xyz.order.management.domain.repository;

import com.xyz.order.management.domain.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
