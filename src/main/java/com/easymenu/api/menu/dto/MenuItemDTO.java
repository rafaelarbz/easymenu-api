package com.easymenu.api.menu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record MenuItemDTO(
        Long id,

        @NotBlank(message = "Name must not be empty.")
        String name,

        String description,

        @NotNull(message = "Price must not be empty.")
        @Size(min = 3, max = 10, message = "Price must be between 3 and 10 characters long.")
        BigDecimal price,

        String imageUrl,

        @NotBlank(message = "Menu Category ID must not be empty.")
        Long menuCategoryId,

        @NotNull(message = "Enterprise ID must not be empty.")
        Long enterpriseId
) {
    public interface Creation {}
    public interface Update {}
}
