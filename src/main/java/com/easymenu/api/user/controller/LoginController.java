package com.easymenu.api.user.controller;

import com.easymenu.api.security.JwtUtil;
import com.easymenu.api.user.dto.LoginRequestDTO;
import com.easymenu.api.user.dto.LoginResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Login Controller", description = "Endpoint for user login")
public class LoginController {
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtUtil jwtUtil;

    @Value("${security.jwt-ttl}")
    Integer jwtTtl;

    @Operation(
        summary = "User login",
        description = "Allows login with user authentication in the system",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successful login"),
            @ApiResponse(responseCode = "401", description = "Unauthorized user"),
        }
    )
    @PostMapping(
        value = "/login",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO requestDTO) {
        UsernamePasswordAuthenticationToken authenticationToken =
            UsernamePasswordAuthenticationToken.unauthenticated(requestDTO.cpf(), requestDTO.password());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = jwtUtil.generate(requestDTO.cpf(), jwtTtl);

        String role = authentication.getAuthorities().stream()
            .findFirst()
            .map(GrantedAuthority::getAuthority)
            .orElse("");

        LoginResponseDTO loginResponseDTO =
            new LoginResponseDTO(
                token,
                role,
                jwtUtil.extractCreatedAt(token),
                jwtUtil.extractExpirationDate(token));

        return ResponseEntity.ok().body(loginResponseDTO);
    }
}
