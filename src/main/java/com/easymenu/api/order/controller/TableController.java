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
@RequestMapping("/table")
@Tag(name = "Table Controller", description = "Endpoints for table-related operations and attributes")
public class TableController {
    private final CommonService tableService;

    @Operation(
        summary = "Find table by ID",
        description = "Retrieve a table by its ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved table"),
            @ApiResponse(responseCode = "404", description = "Table not found")
        }
    )
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponseDTO> findTableById(@PathVariable Long id) {
        return ResponseEntity.ok(tableService.findEntityById(id));
    }

    @Operation(
        summary = "Find all tables by enterprise ID",
        description = "Retrieve a list of all tables associated with a specific enterprise",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved tables"),
            @ApiResponse(responseCode = "404", description = "No tables found for the given enterprise ID")
        }
    )
    @GetMapping(value = "/enterprise/{id}/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CommonResponseDTO>> findAllByEnterprise(@PathVariable Long id) {
        List<CommonResponseDTO> tables = tableService.findAllByEnterprise(id);
        if (tables.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tables);
    }

    @Operation(
        summary = "Create multiple tables",
        description = "Create a batch of tables",
        responses = {
            @ApiResponse(responseCode = "201", description = "Successfully created tables"),
            @ApiResponse(responseCode = "400", description = "Invalid table data")
        }
    )
    @PostMapping(
        value = "/batch",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<CommonResponseDTO>> createTableBatch(
            @Validated @RequestBody CommonRequestBatchDTO commonRequestBatchDTO) {
        List<CommonResponseDTO> createdTables = tableService.createEntityBatch(commonRequestBatchDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTables);
    }

    @Operation(
        summary = "Update a table",
        description = "Update details of a table by ID and enterprise ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully updated table"),
            @ApiResponse(responseCode = "400", description = "Invalid table data"),
            @ApiResponse(responseCode = "404", description = "Table not found")
        }
    )
    @PatchMapping(
        value = "/{id}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponseDTO> updateTable(
        @PathVariable Long id,
        @RequestParam Long enterpriseId,
        @Validated @RequestBody CommonRequestDTO commonRequestDTO) {
        return ResponseEntity.ok(tableService.updateEntity(id, enterpriseId, commonRequestDTO));
    }

    @Operation(
        summary = "Change table availability",
        description = "Toggle availability status of a table by ID and enterprise ID",
        responses = {
            @ApiResponse(responseCode = "204", description = "Successfully changed availability"),
            @ApiResponse(responseCode = "404", description = "Table not found")
        }
    )
    @PatchMapping(value = "/{id}/availability")
    public ResponseEntity<Void> changeAvailability(@PathVariable Long id, @RequestParam Long enterpriseId) {
        tableService.changeAvailability(id, enterpriseId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Generate QR Code for table",
        description = "Generate a QR Code for a specific table by its ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully generated QR Code"),
            @ApiResponse(responseCode = "404", description = "Table not found"),
            @ApiResponse(responseCode = "500", description = "Error generating QR Code")
        }
    )
    @GetMapping(value = "/{id}/qrcode", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> generateQRCode(
        @PathVariable Long id,
        @Validated @RequestBody CommonQRCodeDTO commonQRCodeDTO) throws IOException, WriterException {
        byte[] qrCode = tableService.generateQRCode(id, commonQRCodeDTO);
        return ResponseEntity.ok(qrCode);
    }
}
