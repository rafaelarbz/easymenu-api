package com.easymenu.api.rabbitmq.listener;

import com.easymenu.api.configuration.RabbitMQConfig;
import com.easymenu.api.menu.dto.MenuItemDTO;
import com.easymenu.api.shared.service.WebSocketService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

public class MenuItemListener {
    @Autowired private ObjectMapper objectMapper;
    @Autowired private WebSocketService webSocketService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_MENU_ITEMS_UPDATED)
    public void handlerMenuItemUpdate(String message) throws JsonProcessingException {
        MenuItemDTO menuItemDTO = objectMapper.readValue(message, MenuItemDTO.class);
        webSocketService.sendMenuItemUpdated(menuItemDTO);
    }

}
