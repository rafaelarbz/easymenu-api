package com.easymenu.api.order.service.implementation;

import com.easymenu.api.menu.entity.MenuItem;
import com.easymenu.api.menu.repository.MenuItemRepository;
import com.easymenu.api.order.dto.OrderCreationDTO;
import com.easymenu.api.order.dto.OrderDTO;
import com.easymenu.api.order.dto.OrderItemCreationDTO;
import com.easymenu.api.order.dto.OrderItemResponseDTO;
import com.easymenu.api.order.entity.Order;
import com.easymenu.api.order.entity.OrderItemRelation;
import com.easymenu.api.order.enums.OrderStatus;
import com.easymenu.api.order.mapper.OrderMapper;
import com.easymenu.api.order.repository.OrderItemRelationRepository;
import com.easymenu.api.order.repository.OrderRepository;
import com.easymenu.api.order.service.CommonService;
import com.easymenu.api.order.service.OrderService;
import com.easymenu.api.shared.service.WebSocketService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {
    @Autowired private OrderRepository orderRepository;
    @Autowired private MenuItemRepository menuItemRepository;
    @Autowired private OrderItemRelationRepository orderItemRelationRepository;
    @Autowired private OrderMapper orderMapper;
    @Autowired private WebSocketService webSocketService;

    @Qualifier("tableService")
    @Autowired private CommonService tableService;
    @Qualifier("commandService")
    @Autowired private CommonService commandService;

    @Override
    public List<OrderDTO> findAllByEnterprise(Long id) {
        return orderRepository.findAllByEnterpriseId(id)
            .stream()
            .map(orderMapper::toDTO)
            .toList();
    }

    @Override
    public List<OrderDTO> findAllNotPaidByEnterprise(Long id) {
        return orderRepository.findAllByEnterpriseIdAndPaidFalse(id)
            .stream()
            .map(orderMapper::toDTO)
            .toList();
    }

    @Override
    public List<OrderItemResponseDTO> findItemsByOrder(Long id) {
        return orderRepository.findItemsByOrder(id);
    }

    @Override
    public OrderDTO findLastOrderByTable(Long tableId) {
        Order order = orderRepository.findFirstByTableIdOrderByIdDesc(tableId);
        return orderMapper.toDTO(order);
    }

    @Override
    public OrderDTO findLastOrderByCommand(Long commandId) {
        Order order = orderRepository.findFirstByCommandIdOrderByIdDesc(commandId);
        return orderMapper.toDTO(order);
    }

    @Override
    @Transactional
    public OrderDTO createOrder(OrderCreationDTO orderCreationDTO) {
        Order order = buildOrder(orderCreationDTO);
        Order createdOrder = orderRepository.save(order);

        List<OrderItemRelation> orderItems = buildOrderItems(createdOrder.getId(), orderCreationDTO.items());
        orderItems.forEach(orderItemRelationRepository::save);

        createdOrder.setTotalAmount(calculateTotalAmount(orderItems));

        orderRepository.save(createdOrder);
        OrderDTO createdOrderDTO = orderMapper.toDTO(createdOrder);

        updateTableOrCommandAvailability(createdOrder);
        webSocketService.sendOrderCreated(createdOrderDTO);

        return createdOrderDTO;
    }

    @Override
    @Transactional
    public OrderDTO addOrderItems(Long id, List<OrderItemCreationDTO> orderItemCreationDTOS) {
        Order order = findById(id);

        List<OrderItemRelation> newItems = buildOrderItems(order.getId(), orderItemCreationDTOS);
        List<OrderItemRelation> existingItems = orderItemRelationRepository.findAllByOrderId(order.getId());

        newItems.forEach(orderItemRelationRepository::save);

        existingItems.addAll(newItems);
        order.setTotalAmount(calculateTotalAmount(existingItems));

        Order updatedOrder = orderRepository.save(order);
        OrderDTO updatedOrderDTO = orderMapper.toDTO(updatedOrder);

        webSocketService.sendOrderUpdate(updatedOrderDTO);

        return updatedOrderDTO;
    }

    @Override
    @Transactional
    public OrderDTO removeOrderItems(Long id, List<OrderItemCreationDTO> orderItemCreationDTOS) {
        Order order = findById(id);

        for (OrderItemCreationDTO itemDTO : orderItemCreationDTOS) {
            orderItemRelationRepository.deleteByOrderIdAndMenuItemId(id, itemDTO.itemId());
        }

        List<OrderItemRelation> remainingItems =
            orderItemRelationRepository.findAllByOrderId(id);

        order.setTotalAmount(calculateTotalAmount(remainingItems));

        Order updatedOrder = orderRepository.save(order);
        OrderDTO updatedOrderDTO = orderMapper.toDTO(updatedOrder);

        webSocketService.sendOrderUpdate(updatedOrderDTO);

        return updatedOrderDTO;
    }

    @Override
    @Transactional
    public OrderDTO updateOrderStatus(Long id, String orderStatus) {
        Order order = findById(id);
        order.setStatus(orderMapper.mapStringToEnum(orderStatus));

        Order updatedOrder = orderRepository.save(order);
        OrderDTO updatedOrderDTO = orderMapper.toDTO(updatedOrder);

        webSocketService.sendOrderUpdate(updatedOrderDTO);

        return updatedOrderDTO;
    }

    @Override
    @Transactional
    public OrderDTO postOrderPayment(Long id) {
        Order order = findById(id);

        order.setPaid(true);
        order.setStatus(OrderStatus.COMPLETED);

        Order paidOrder = orderRepository.save(order);
        OrderDTO paidOrderDTO = orderMapper.toDTO(paidOrder);

        updateTableOrCommandAvailability(paidOrder);
        webSocketService.sendOrderUpdate(paidOrderDTO);

        return paidOrderDTO;
    }

    void updateTableOrCommandAvailability(Order order) {
        if (Objects.nonNull(order.getTableId())) {
            tableService.changeAvailability(order.getTableId(), order.getEnterpriseId());
        }

        if (Objects.nonNull(order.getCommandId())) {
            commandService.changeAvailability(order.getCommandId(), order.getEnterpriseId());
        }
    }

    private Order findById(Long id) {
        return orderRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: "  + id));
    }

    private static BigDecimal calculateTotalAmount(List<OrderItemRelation> orderItems) {
        return orderItems.stream()
            .map(item ->
                    item.getItemPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Order buildOrder(OrderCreationDTO orderCreationDTO) {
        return Order.builder()
            .enterpriseId(orderCreationDTO.enterpriseId())
            .tableId(orderCreationDTO.tableId())
            .commandId(orderCreationDTO.commandId())
            .status(OrderStatus.PENDING)
            .paid(false)
            .totalAmount(BigDecimal.ZERO)
            .build();
    }

    private List<OrderItemRelation> buildOrderItems(
        Long orderId,
        List<OrderItemCreationDTO> orderItemsCreationDTO) {

        return orderItemsCreationDTO.stream()
            .map(itemDTO -> {
                    MenuItem menuItem = menuItemRepository.findById(itemDTO.itemId())
                        .orElseThrow(() -> new EntityNotFoundException("Menu item not found with ID: " + itemDTO.itemId()));

                    return OrderItemRelation.builder()
                        .orderId(orderId)
                        .menuItemId(menuItem.getId())
                        .quantity(itemDTO.quantity())
                        .itemPrice(menuItem.getPrice())
                        .build();
            })
            .toList();
    }
}
