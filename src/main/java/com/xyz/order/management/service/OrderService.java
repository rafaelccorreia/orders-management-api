package com.xyz.order.management.service;

import com.xyz.order.management.domain.models.Order;
import com.xyz.order.management.domain.models.OrderItem;
import com.xyz.order.management.domain.models.OrderStatus;
import com.xyz.order.management.domain.models.Product;
import com.xyz.order.management.domain.repository.OrderItemRepository;
import com.xyz.order.management.domain.repository.OrderRepository;
import com.xyz.order.management.domain.repository.ProductRepository;
import com.xyz.order.management.dto.OrderDTO;
import com.xyz.order.management.dto.OrderItemDTO;
import com.xyz.order.management.dto.OrderResponseDTO;
import com.xyz.order.management.dto.ProductItemDTO;
import lombok.Data;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public List<OrderResponseDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(order -> {
            OrderResponseDTO dto = new OrderResponseDTO();
            dto.setId(order.getId());
            dto.setOrderDate(order.getOrderDate());
            dto.setStatus(order.getStatus());

            List<OrderItemDTO> orderItemDTOs = order.getOrderItems().stream().map(orderItem -> {
                OrderItemDTO itemDTO = new OrderItemDTO();
                itemDTO.setId(orderItem.getId());
                itemDTO.setQuantity(orderItem.getQuantity());
                itemDTO.setPrice(orderItem.getPrice());
                itemDTO.setProduct(orderItem.getProduct());
                return itemDTO;
            }).collect(Collectors.toList());

            dto.setOrderItems(orderItemDTOs);
            return dto;
        }).collect(Collectors.toList());
    }

    public OrderResponseDTO getOrderById(Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id " + id));

        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());

        List<OrderItemDTO> orderItemDTOs = order.getOrderItems().stream().map(orderItem -> {
            OrderItemDTO itemDTO = new OrderItemDTO();
            itemDTO.setId(orderItem.getId());
            itemDTO.setQuantity(orderItem.getQuantity());
            itemDTO.setPrice(orderItem.getPrice());
            itemDTO.setProduct(orderItem.getProduct());
            return itemDTO;
        }).collect(Collectors.toList());

        dto.setOrderItems(orderItemDTOs);
        return dto;
    }

    public Order saveOrder(OrderDTO orderDTO) {
        Order order = new Order();
        order.setOrderDate(orderDTO.getOrderDate());
        order.setStatus(OrderStatus.valueOf(orderDTO.getStatus()));

        Order savedOrder = orderRepository.save(order);

        List<OrderItem> orderItems = new ArrayList<>();

        for (ProductItemDTO productItemDTO : orderDTO.getProductList()) {
            Product product = productRepository.findById(productItemDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id " + productItemDTO.getProductId()));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(product);
            orderItem.setQuantity(productItemDTO.getQuantity());
            orderItem.setPrice(product.getPrice());

            orderItemRepository.save(orderItem);
            orderItems.add(orderItem);
        }

        savedOrder.setOrderItems(orderItems);

        return savedOrder;
    }

    @Transactional
    public Order updateOrder(Integer id, OrderDTO orderDTO) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id " + id));

        Order newOrder = new Order();

        newOrder.setOrderDate(orderDTO.getOrderDate());
        newOrder.setStatus(OrderStatus.valueOf(orderDTO.getStatus()));

        if (existingOrder.getOrderItems() != null) {
            orderItemRepository.deleteByOrderId(existingOrder.getId());
        }

        List<OrderItem> updatedOrderItems = new ArrayList<>();

        for (ProductItemDTO productItemDTO : orderDTO.getProductList()) {
            Product product = productRepository.findById(productItemDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id " + productItemDTO.getProductId()));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(existingOrder);
            orderItem.setProduct(product);
            orderItem.setQuantity(productItemDTO.getQuantity());
            orderItem.setPrice(product.getPrice());


            updatedOrderItems.add(orderItem);
        }

        newOrder.setOrderItems(updatedOrderItems);
        Order savedOrder = orderRepository.save(newOrder);

        savedOrder.setOrderItems(updatedOrderItems);

        return savedOrder;
    }

    @Transactional
    public void deleteOrder(Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id " + id));

        if (order.getOrderItems() != null) {
            orderItemRepository.deleteByOrderId(id);
        }

        orderRepository.deleteById(id);
    }

    @Transactional
    public Order updateOrderStatus(Integer id, OrderStatus status) {
        Order order = orderRepository.getReferenceById(id);
        order.setStatus(status);

        String routingKey = "orders.v1.order-status-change";

        Message message = new Message(String.format("Status do pedido %d alterado para %s", order.getId(), status).getBytes());

        rabbitTemplate.convertAndSend(routingKey, message);
        return orderRepository.save(order);
    }
}