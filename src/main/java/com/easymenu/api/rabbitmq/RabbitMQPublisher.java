package com.easymenu.api.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQPublisher {
    @Autowired private RabbitTemplate rabbitTemplate;
    @Autowired ObjectMapper objectMapper;

    public void publish(String exchange, String routingKey, Object message) throws JsonProcessingException {
        String jsonMessage = objectMapper.writeValueAsString(message);
        rabbitTemplate.convertAndSend(exchange, routingKey, jsonMessage);
    }
}
