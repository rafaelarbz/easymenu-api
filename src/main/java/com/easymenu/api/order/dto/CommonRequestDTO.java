package com.easymenu.api.order.dto;

import jakarta.validation.constraints.NotNull;

public record CommonRequestDTO(
        @NotNull(message = "Code must not be empty")
        String code
) {
}
