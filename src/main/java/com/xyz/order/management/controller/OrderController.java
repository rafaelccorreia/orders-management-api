package com.xyz.order.management.controller;

import com.xyz.order.management.domain.models.Order;
import com.xyz.order.management.domain.models.OrderStatus;
import com.xyz.order.management.dto.OrderDTO;
import com.xyz.order.management.dto.OrderResponseDTO;
import com.xyz.order.management.dto.OrderStatusUpdateDTO;
import com.xyz.order.management.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/xyz-orders/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public List<OrderResponseDTO> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public OrderResponseDTO getOrderById(@PathVariable Integer id) {
        return orderService.getOrderById(id);
    }

    @PostMapping
    public Order createOrder(@RequestBody OrderDTO orderDTO) {
        return orderService.saveOrder(orderDTO);
    }

    @PutMapping("/{id}")
    public Order updateOrder(@PathVariable Integer id, @RequestBody OrderDTO orderDTO) {
        return orderService.updateOrder(id, orderDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Integer id) {
        orderService.deleteOrder(id);
    }

    @PutMapping("/{id}/status")
    public Order updateOrderStatus(@PathVariable Integer id, @RequestBody OrderStatusUpdateDTO statusUpdateDTO) {
        return orderService.updateOrderStatus(id, statusUpdateDTO.getStatus());
    }
}