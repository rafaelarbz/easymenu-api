package com.easymenu.api.shared.service.implementation;

import com.easymenu.api.menu.dto.MenuItemDTO;
import com.easymenu.api.order.dto.CommonResponseDTO;
import com.easymenu.api.order.dto.OrderDTO;
import com.easymenu.api.shared.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketServiceImpl implements WebSocketService {
    @Autowired private SimpMessagingTemplate messagingTemplate;

    private static final String CREATED_PREFIX = "/created";
    private static final String UPDATED_PREFIX = "/updated";

    private static final String ORDER_PREFIX = "/topic/order";
    private static final String MENU_ITEM_PREFIX = "/topic/menu_item";
    private static final String TABLE_PREFIX = "/topic/table";
    private static final String COMMAND_PREFIX = "/topic/command";

    @Override
    public void sendOrderCreated(OrderDTO orderDTO) {
        messagingTemplate.convertAndSend(ORDER_PREFIX + CREATED_PREFIX, orderDTO);
    }

    @Override
    public void sendOrderUpdate(OrderDTO orderDTO) {
        messagingTemplate.convertAndSend(ORDER_PREFIX + UPDATED_PREFIX, orderDTO);
    }

    @Override
    public void sendMenuItemUpdated(MenuItemDTO menuItemDTO) {
        messagingTemplate.convertAndSend(MENU_ITEM_PREFIX + UPDATED_PREFIX, menuItemDTO);
    }

    @Override
    public void sendTableUpdated(CommonResponseDTO commonResponseDTO) {
        messagingTemplate.convertAndSend(TABLE_PREFIX + UPDATED_PREFIX, commonResponseDTO);
    }

    @Override
    public void sendCommandUpdated(CommonResponseDTO commonResponseDTO) {
        messagingTemplate.convertAndSend(COMMAND_PREFIX + UPDATED_PREFIX, commonResponseDTO);
    }
}
