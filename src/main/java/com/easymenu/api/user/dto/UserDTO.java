package com.easymenu.api.user.dto;

import com.easymenu.api.user.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Arrays;
import java.util.Objects;

public record UserDTO(
        Long id,

        @NotBlank(message = "Full name must not be empty.")
        String fullName,

        @NotBlank(message = "CPF must not be empty.")
        @Size(min = 11, max = 11, message = "CPF must be 11 characters long.")
        String cpf,

        @NotBlank(message = "Role must not be empty.")
        String role,

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @NotBlank(message = "Password must not be empty.")
        String password,

        Long enterpriseId
) {
    public interface Creation {}
    public interface Update {}

    public UserDTO {
        if (Arrays.stream(UserRole.values()).noneMatch(e -> e.name().equals(role))) {
            throw new IllegalArgumentException("Invalid role: " + role);
        }
        if (!role.equals(UserRole.SYSTEM.name()) && Objects.isNull(enterpriseId)) {
            throw new IllegalArgumentException("Enterprise ID must not be empty.");
        }
    }
}
