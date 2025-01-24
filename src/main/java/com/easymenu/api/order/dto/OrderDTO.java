package com.easymenu.api.order.dto;

import java.math.BigDecimal;

public record OrderDTO (
    Long id,

    String status,

    boolean paid,

    Long commandId,

    Long tableId,

    BigDecimal totalAmount
) {
}