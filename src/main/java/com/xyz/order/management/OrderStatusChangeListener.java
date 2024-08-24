package com.xyz.order.management;

import com.xyz.order.management.domain.models.OrderStatus;
import com.xyz.order.management.dto.OrderStatusUpdateDTO;
import com.xyz.order.management.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class OrderStatusChangeListener {

    @Autowired
    private OrderService orderService;

    @RabbitListener(queues = "orders.v1.order-status-change")
    public void onOrderStatusChanged(String message) {
        System.out.println("Mensagem recebida: " + message);

        Pattern pattern = Pattern.compile("Status do pedido (\\d+) alterado para (\\w+)");
        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            int orderId = Integer.parseInt(matcher.group(1));
            String status = matcher.group(2);

            OrderStatus statusEnum;
            statusEnum = OrderStatus.valueOf(status);

            orderService.updateOrderStatus(orderId, statusEnum);
            System.out.printf("orderId: %d , status: %s%n", orderId, status);
        } else {

            System.out.println("Formato de mensagem inesperado: " + message);
        }

    }
}
