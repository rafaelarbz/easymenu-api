package com.easymenu.api.order.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderCreationDTO(
        Long tableId,

        Long commandId,

        @NotNull(message = "Enterprise ID must not be empty")
        Long enterpriseId,

        @NotNull(message = "Menu item must not be empty")
        List<OrderItemCreationDTO> items
) {
    @AssertTrue(message = "Either table or command must be provided")
    public boolean isTableIdOrCommandIdPresent() {
        return tableId != null || commandId != null;
    }
}
