package com.easymenu.api.rabbitmq.listener;

import com.easymenu.api.configuration.RabbitMQConfig;
import com.easymenu.api.order.dto.OrderDTO;
import com.easymenu.api.shared.service.WebSocketService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderListener {
    @Autowired private ObjectMapper objectMapper;
    @Autowired private WebSocketService webSocketService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_ORDERS_CREATED)
    public void handleOrderCreation(String message) throws JsonProcessingException {
        OrderDTO orderDTO = objectMapper.readValue(message, OrderDTO.class);
        webSocketService.sendOrderCreated(orderDTO);
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_ORDERS_UPDATED)
    public void handleOrderUpdate(String message) throws JsonProcessingException {
        OrderDTO orderDTO = objectMapper.readValue(message, OrderDTO.class);
        webSocketService.sendOrderUpdate(orderDTO);
    }
}
