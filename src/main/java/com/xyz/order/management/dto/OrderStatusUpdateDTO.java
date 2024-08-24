package com.xyz.order.management.dto;

import com.xyz.order.management.domain.models.OrderStatus;
import lombok.Data;

@Data
public class OrderStatusUpdateDTO {
    private OrderStatus status;
}
