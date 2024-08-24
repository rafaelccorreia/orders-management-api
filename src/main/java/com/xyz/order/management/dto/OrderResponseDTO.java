package com.xyz.order.management.dto;

import com.xyz.order.management.domain.models.OrderStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class OrderResponseDTO {
    private Integer id;
    private LocalDate orderDate;
    private List<OrderItemDTO> orderItems;
    private OrderStatus status;
}
