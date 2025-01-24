package com.easymenu.api.shared.service;

import com.easymenu.api.menu.dto.MenuItemDTO;
import com.easymenu.api.order.dto.CommonResponseDTO;
import com.easymenu.api.order.dto.OrderDTO;

public interface WebSocketService {
    void sendOrderCreated(OrderDTO orderDTO);
    void sendOrderUpdate(OrderDTO orderDTO);
    void sendMenuItemUpdated(MenuItemDTO menuItemDTO);
    void sendTableUpdated(CommonResponseDTO commonResponseDTO);
    void sendCommandUpdated(CommonResponseDTO commonResponseDTO);
}
