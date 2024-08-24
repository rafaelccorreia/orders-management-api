package com.xyz.order.management.dto;

import com.xyz.order.management.domain.models.Product;
import lombok.Data;

@Data
public class OrderItemDTO {
    private Integer id;
    private Integer quantity;
    private Double price;
    private Product product;
}
