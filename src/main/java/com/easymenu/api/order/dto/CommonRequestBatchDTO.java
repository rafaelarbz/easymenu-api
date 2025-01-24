package com.easymenu.api.order.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CommonRequestBatchDTO(
        @NotNull(message = "Code must not be empty")
        List<String> codes,

        @NotNull(message = "Enterprise ID must not be empty")
        Long enterpriseId
) {
}
