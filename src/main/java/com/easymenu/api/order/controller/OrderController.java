package com.easymenu.api.order.controller;

import com.easymenu.api.order.dto.OrderCreationDTO;
import com.easymenu.api.order.dto.OrderDTO;
import com.easymenu.api.order.dto.OrderItemCreationDTO;
import com.easymenu.api.order.dto.OrderItemResponseDTO;
import com.easymenu.api.order.service.OrderService;
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
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
@Tag(name = "Order Controller", description = "Endpoints for order-related operations and attributes")
public class OrderController {
    private final OrderService orderService;

    @Operation(
        summary = "Find all orders by enterprise",
        description = "Retrieve a list of all orders associated with a specific enterprise",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved orders"),
            @ApiResponse(responseCode = "404", description = "No orders found for the given enterprise ID")
        }
    )
    @GetMapping(value = "/enterprise/{id}/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderDTO>> findAllByEnterprise(@PathVariable Long id) {
        List<OrderDTO> orders = orderService.findAllByEnterprise(id);
        if (orders.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(orders);
    }

    @Operation(
        summary = "Find all unpaid orders by enterprise",
        description = "Retrieve a list of all unpaid orders associated with a specific enterprise",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved unpaid orders"),
            @ApiResponse(responseCode = "404", description = "No unpaid orders found for the given enterprise ID")
        }
    )
    @GetMapping(value = "/enterprise/{id}/unpaid", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderDTO>> findAllUnpaidByEnterprise(@PathVariable Long id) {
        List<OrderDTO> orders = orderService.findAllUnpaidByEnterprise(id);
        if (orders.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(orders);
    }

    @Operation(
        summary = "Find items by order",
        description = "Retrieve a list of items associated with a specific order",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved order items"),
            @ApiResponse(responseCode = "404", description = "No items found for the given order ID")
        }
    )
    @GetMapping(value = "/{id}/items", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderItemResponseDTO>> findItemsByOrder(@PathVariable Long id) {
        List<OrderItemResponseDTO> items = orderService.findItemsByOrder(id);
        if (items.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(items);
    }

    @Operation(
        summary = "Find last order by table",
        description = "Retrieve the last order associated with a specific table",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved order by table"),
            @ApiResponse(responseCode = "404", description = "No order found for the given table ID")
        }
    )
    @GetMapping(value = "/table/{id}/last", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDTO> findLastOrderByTable(@PathVariable Long id) {
        OrderDTO order = orderService.findLastOrderByTable(id);
        if (Objects.isNull(order)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(order);
    }

    @Operation(
        summary = "Find last order by command",
        description = "Retrieve the last order associated with a specific command",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved order by command"),
            @ApiResponse(responseCode = "404", description = "No order found for the given command ID")
        }
    )
    @GetMapping(value = "/command/{id}/last", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDTO> findLastOrderByCommand(@PathVariable Long id) {
        OrderDTO order = orderService.findLastOrderByCommand(id);
        if (Objects.isNull(order)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(order);
    }

    @Operation(
        summary = "Create a new order",
        description = "Create a new order with associated items",
        responses = {
            @ApiResponse(responseCode = "201", description = "Successfully created order"),
            @ApiResponse(responseCode = "400", description = "Invalid order data")
        }
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDTO> createOrder(@Validated @RequestBody OrderCreationDTO orderCreationDTO) {
        OrderDTO createdOrder = orderService.createOrder(orderCreationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @Operation(
        summary = "Add items to an order",
        description = "Add items to an existing order",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully updated order items"),
            @ApiResponse(responseCode = "400", description = "Invalid order items data"),
            @ApiResponse(responseCode = "404", description = "Order not found")
        }
    )
    @PostMapping(
        value = "/{id}/items",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<OrderDTO> addOrderItems(
        @PathVariable Long id,
        @Validated @RequestBody List<OrderItemCreationDTO> items) {
        OrderDTO updatedOrder = orderService.addOrderItems(id, items);
        return ResponseEntity.ok(updatedOrder);
    }

    @Operation(
        summary = "Remove items from an order",
        description = "Remove items from an existing order",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully removed order items"),
            @ApiResponse(responseCode = "400", description = "Invalid order items data"),
            @ApiResponse(responseCode = "404", description = "Order not found")
        }
    )
    @DeleteMapping(
        value = "/{id}/items",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<OrderDTO> removeOrderItems(
        @PathVariable Long id,
        @RequestBody List<OrderItemCreationDTO> items) {
        OrderDTO updatedOrder = orderService.removeOrderItems(id, items);
        return ResponseEntity.ok(updatedOrder);
    }

    @Operation(
        summary = "Update order status",
        description = "Update the status of an order",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully updated order status"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "404", description = "Order not found")
        }
    )
    @PatchMapping(value = "/{id}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDTO> updateOrderStatus(
        @PathVariable Long id,
        @RequestParam String status) {
        OrderDTO updatedOrder = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(updatedOrder);
    }

    @Operation(
        summary = "Post order payment",
        description = "Mark an order as paid",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully updated order payment"),
            @ApiResponse(responseCode = "404", description = "Order not found")
        }
    )
    @PostMapping(value = "/{id}/payment", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDTO> postOrderPayment(@PathVariable Long id) {
        OrderDTO paidOrder = orderService.postOrderPayment(id);
        return ResponseEntity.ok(paidOrder);
    }
}
