package com.easymenu.api.order.service;

import com.easymenu.api.order.dto.OrderCreationDTO;
import com.easymenu.api.order.dto.OrderDTO;
import com.easymenu.api.order.dto.OrderItemCreationDTO;
import com.easymenu.api.order.dto.OrderItemResponseDTO;

import java.util.List;

public interface OrderService {
    List<OrderDTO> findAllByEnterprise(Long id);
    List<OrderDTO> findAllUnpaidByEnterprise(Long id);
    List<OrderItemResponseDTO> findItemsByOrder(Long id);
    OrderDTO findLastOrderByTable(Long tableId);
    OrderDTO findLastOrderByCommand(Long commandId);
    OrderDTO createOrder(OrderCreationDTO orderCreationDTO);
    OrderDTO addOrderItems(Long id, List<OrderItemCreationDTO> orderItemCreationDTOS);
    OrderDTO removeOrderItems(Long id, List<OrderItemCreationDTO> orderItemCreationDTOS);
    OrderDTO updateOrderStatus(Long id, String orderStatus);
    OrderDTO postOrderPayment(Long id);
}
