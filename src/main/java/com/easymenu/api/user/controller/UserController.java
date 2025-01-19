package com.easymenu.api.user.controller;

import com.easymenu.api.user.dto.UserDTO;
import com.easymenu.api.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "User Controller", description = "Endpoints for user-related operations and attributes")
public class UserController {
    private final UserService userService;

    @Operation(
        summary = "Find all users by enterprise ID",
        description = "Retrieve all users associated with a given enterprise ID.",
        responses = {
            @ApiResponse(responseCode = "200", description = "List of users retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No users found for the specified enterprise ID"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
        }
    )
    @GetMapping(value = "/{id}/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> findAllByEnterprise(@PathVariable Long id) {
        List<UserDTO> users = userService.findAllByEnterprise(id);
        if (users.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(users);
    }

    @Operation(
        summary = "Find user by ID",
        description = "Retrieve a user's details by their ID.",
        responses = {
            @ApiResponse(responseCode = "200", description = "User details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
        }
    )
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO>  findUserById(@PathVariable Long id) {
        UserDTO userDTO = userService.findUserById(id);
        return ResponseEntity.ok(userDTO);
    }

    @Operation(
        summary = "Create a new user",
        description = "Create a new user with the provided details.",
        responses = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
        }
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> createUser(@Validated(UserDTO.Creation.class) @RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @Operation(
        summary = "Update an existing user",
        description = "Update the details of an existing user by their ID.",
        responses = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
        }
    )
    @PatchMapping(
        value = "/{id}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> updateUser(
        @PathVariable Long id,
        @Validated(UserDTO.Update.class) @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(
        summary = "Disable a user",
        description = "Disable a user by its ID and associated enterprise ID",
        responses = {
            @ApiResponse(responseCode = "204", description = "User disabled successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
        }
    )
    @PatchMapping(value = "/{id}/disable")
    public ResponseEntity<Void> disableUser(@PathVariable Long id, @RequestParam Long enterpriseId) {
        userService.disableUser(id, enterpriseId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Delete a user",
        description = "Delete a user by their ID and associated enterprise ID.",
        responses = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
        }
    )
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id, @RequestParam Long enterpriseId) {
        userService.deleteUser(id, enterpriseId);
        return ResponseEntity.noContent().build();
    }
}
