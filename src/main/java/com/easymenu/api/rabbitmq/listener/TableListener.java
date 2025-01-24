package com.easymenu.api.rabbitmq.listener;

import com.easymenu.api.configuration.RabbitMQConfig;
import com.easymenu.api.order.dto.CommonResponseDTO;
import com.easymenu.api.shared.service.WebSocketService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

public class TableListener {
    @Autowired private ObjectMapper objectMapper;
    @Autowired private WebSocketService webSocketService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_TABLES_UPDATED)
    public void handleTableUpdate(String message) throws JsonProcessingException {
        CommonResponseDTO commonResponseDTO = objectMapper.readValue(message, CommonResponseDTO.class);
        webSocketService.sendTableUpdated(commonResponseDTO);
    }
}
