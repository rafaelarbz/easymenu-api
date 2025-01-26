package com.easymenu.api.order.mapper;

import com.easymenu.api.order.dto.OrderDTO;
import com.easymenu.api.order.entity.Order;
import com.easymenu.api.order.enums.OrderStatus;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDTO toDTO(Order order);

    default OrderStatus mapStringToEnum(String orderStatus) {
        return OrderStatus.valueOf(orderStatus.toUpperCase());
    }
    default String mapEnumToString(OrderStatus orderStatus) {
        return orderStatus.name();
    }
}
