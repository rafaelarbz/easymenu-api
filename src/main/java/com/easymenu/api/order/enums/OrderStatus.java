package com.easymenu.api.order.enums;

import java.util.Arrays;

public enum OrderStatus {
    PENDING,
    PRODUCING,
    COMPLETED,
    CANCELED_KITCHEN,
    CANCELED_CUSTOMER;

    public static boolean isValidStatus(String status) {
        return Arrays.stream(OrderStatus.values())
            .anyMatch(orderStatus -> orderStatus.name().equalsIgnoreCase(status));
    }
}
