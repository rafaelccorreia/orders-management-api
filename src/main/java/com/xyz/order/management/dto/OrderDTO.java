package com.xyz.order.management.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class OrderDTO {
    private LocalDate orderDate;
    private List<ProductItemDTO> productList;
    private String status;
}
