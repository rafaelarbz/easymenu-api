package com.easymenu.api.user.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO (
        @NotBlank(message = "CPF must not be empty.")
        String cpf,

        @NotBlank(message = "Password must not be empty.")
        String password
) {
}
