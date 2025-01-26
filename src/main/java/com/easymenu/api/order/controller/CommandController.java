package com.easymenu.api.order.controller;

import com.easymenu.api.order.dto.CommonQRCodeDTO;
import com.easymenu.api.order.dto.CommonRequestBatchDTO;
import com.easymenu.api.order.dto.CommonRequestDTO;
import com.easymenu.api.order.dto.CommonResponseDTO;
import com.easymenu.api.order.service.CommonService;
import com.google.zxing.WriterException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/command")
@Tag(name = "Order Controller", description = "Endpoints for command-related operations and attributes")
public class CommandController {
    private final CommonService commandService;

    @Operation(
        summary = "Find command by ID",
        description = "Retrieve a command by its ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved command"),
            @ApiResponse(responseCode = "404", description = "Command not found")
        }
    )
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponseDTO> findCommandById(@PathVariable Long id) {
        return ResponseEntity.ok(commandService.findEntityById(id));
    }

    @Operation(
        summary = "Find all commands by enterprise ID",
        description = "Retrieve a list of all commands associated with a specific enterprise",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved commands"),
            @ApiResponse(responseCode = "404", description = "No commands found for the given enterprise ID")
        }
    )
    @GetMapping(value = "/enterprise/{id}/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CommonResponseDTO>> findAllByEnterprise(@PathVariable Long id) {
        List<CommonResponseDTO> commands = commandService.findAllByEnterprise(id);
        if (commands.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(commands);
    }

    @Operation(
        summary = "Create multiple commands",
        description = "Create a batch of commands",
        responses = {
            @ApiResponse(responseCode = "201", description = "Successfully created commands"),
            @ApiResponse(responseCode = "400", description = "Invalid commands data")
        }
    )
    @PostMapping(
        value = "/batch",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<CommonResponseDTO>> createCommandBatch(
            @Validated @RequestBody CommonRequestBatchDTO commonRequestBatchDTO) {
        List<CommonResponseDTO> createdCommands = commandService.createEntityBatch(commonRequestBatchDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCommands);
    }

    @Operation(
        summary = "Update a command",
        description = "Update details of a command by ID and enterprise ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully updated command"),
            @ApiResponse(responseCode = "400", description = "Invalid command data"),
            @ApiResponse(responseCode = "404", description = "Command not found")
        }
    )
    @PatchMapping(
        value = "/{id}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponseDTO> updateCommand(
        @PathVariable Long id,
        @RequestParam Long enterpriseId,
        @Validated @RequestBody CommonRequestDTO commonRequestDTO) {
        return ResponseEntity.ok(commandService.updateEntity(id, enterpriseId, commonRequestDTO));
    }

    @Operation(
        summary = "Change command availability",
        description = "Toggle availability status of a command by ID and enterprise ID",
        responses = {
            @ApiResponse(responseCode = "204", description = "Successfully changed availability"),
            @ApiResponse(responseCode = "404", description = "command not found")
        }
    )
    @PatchMapping(value = "/{id}/availability")
    public ResponseEntity<Void> changeAvailability(@PathVariable Long id, @RequestParam Long enterpriseId) {
        commandService.changeAvailability(id, enterpriseId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Generate QR Code for command",
        description = "Generate a QR Code for a specific command by its ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully generated QR Code"),
            @ApiResponse(responseCode = "404", description = "Command not found"),
            @ApiResponse(responseCode = "500", description = "Error generating QR Code")
        }
    )
    @GetMapping(value = "/{id}/qrcode", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> generateQRCode(
        @PathVariable Long id,
        @Validated @RequestBody CommonQRCodeDTO commonQRCodeDTO) throws IOException, WriterException {
        byte[] qrCode = commandService.generateQRCode(id, commonQRCodeDTO);
        return ResponseEntity.ok(qrCode);
    }
}
