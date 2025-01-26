package com.easymenu.api.order.dto;

import jakarta.validation.constraints.NotNull;

public record OrderItemCreationDTO (
        @NotNull(message = "Item ID must not be empty")
        Long itemId,

        @NotNull(message = "Quantity must not be empty")
        int quantity
) {
}
