package com.easymenu.api.order.dto;

import java.math.BigDecimal;

public record OrderItemResponseDTO(
        Long id,
        Long itemId,
        String itemName,
        int quantity,
        BigDecimal price
) {
}
