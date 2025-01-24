package com.easymenu.api.order.dto;

public record CommonResponseDTO(
        Long id,
        String code,
        boolean available
) {
}
