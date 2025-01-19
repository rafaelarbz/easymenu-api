package com.easymenu.api.enterprise.controller;

import com.easymenu.api.enterprise.dto.EnterpriseDTO;
import com.easymenu.api.enterprise.service.EnterpriseService;
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
@RequestMapping("/enterprise")
@Tag(name = "Enterprise Controller", description = "Endpoints for enterprise-related operations and attributes")
public class EnterpriseController {
    private final EnterpriseService enterpriseService;

    @Operation(
        summary = "Retrieve all enterprises",
        description = "Retrieve a list of all enterprises in the system",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of enterprises"),
            @ApiResponse(responseCode = "404", description = "No enterprises found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
        }
    )
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EnterpriseDTO>> findAllEnterprises() {
        List<EnterpriseDTO> enterprises = enterpriseService.findAllEnterprises();
        if (enterprises.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(enterprises);
    }

    @Operation(
        summary = "Retrieve enterprises by parent ID",
        description = "Retrieve a list of enterprises associated with a specific parent ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of enterprises"),
            @ApiResponse(responseCode = "404", description = "No enterprises found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
        }
    )
    @GetMapping(value = "/{id}/associates", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EnterpriseDTO>> findAllByParentId(@PathVariable Long id) {
        List<EnterpriseDTO> enterprises = enterpriseService.findAllByParentId(id);
        if (enterprises.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(enterprises);
    }

    @Operation(
        summary = "Retrieve an enterprise by ID",
        description = "Retrieve a specific enterprise by its ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the enterprise"),
            @ApiResponse(responseCode = "404", description = "Enterprise not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
        }
    )
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EnterpriseDTO> findEnterpriseById(@PathVariable Long id) {
        EnterpriseDTO enterpriseDTO = enterpriseService.findEnterpriseById(id);
        return ResponseEntity.ok(enterpriseDTO);
    }

    @Operation(
        summary = "Create a new enterprise",
        description = "Create a new enterprise in the system",
        responses = {
            @ApiResponse(responseCode = "201", description = "Successfully created the enterprise"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
        }
    )
    @PostMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EnterpriseDTO> createEnterprise(
        @Validated(EnterpriseDTO.Creation.class) @RequestBody EnterpriseDTO enterpriseDTO) {
        EnterpriseDTO createdEnterprise = enterpriseService.createEnterprise(enterpriseDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEnterprise);
    }

    @Operation(
        summary = "Update an existing enterprise",
        description = "Update an enterprise by its ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the enterprise"),
            @ApiResponse(responseCode = "404", description = "Enterprise not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
        }
    )
    @PatchMapping(
        value = "/{id}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EnterpriseDTO> updateEnterprise(
        @PathVariable Long id,
        @Validated(EnterpriseDTO.Update.class) @RequestBody EnterpriseDTO enterpriseDTO) {
        EnterpriseDTO updatedEnterprise = enterpriseService.updateEnterprise(id, enterpriseDTO);
        return ResponseEntity.ok(updatedEnterprise);
    }

    @Operation(
        summary = "Disable an enterprise",
        description = "Disable an enterprise by its ID",
        responses = {
            @ApiResponse(responseCode = "204", description = "Enterprise disabled successfully"),
            @ApiResponse(responseCode = "404", description = "Enterprise not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
        }
    )
    @PatchMapping(value = "/{id}/disable")
    public ResponseEntity<Void> disableEnterprise(@PathVariable Long id) {
        enterpriseService.disableEnterprise(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Delete an enterprise",
        description = "Delete an enterprise by its ID",
        responses = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted the enterprise"),
            @ApiResponse(responseCode = "404", description = "Enterprise not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
        }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnterprise(@PathVariable Long id) {
        enterpriseService.deleteEnterprise(id);
        return ResponseEntity.noContent().build();
    }
}
