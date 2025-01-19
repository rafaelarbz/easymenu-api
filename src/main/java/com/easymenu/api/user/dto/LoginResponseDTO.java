package com.easymenu.api.user.dto;

import java.util.Date;

public record LoginResponseDTO(
        String token,
        String role,
        Date createdAt,
        Date expiresAt
) {
}
