package com.easymenu.api.menu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MenuCategoryDTO(
        Long id,

        @NotBlank(message = "Name must not be empty.")
        String name,

        String description,

        @NotNull(message = "Enterprise ID must not be empty.")
        Long enterpriseId
) {
        public interface Creation {}
        public interface Update {}
}
