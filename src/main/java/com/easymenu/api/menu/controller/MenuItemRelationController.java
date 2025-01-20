package com.easymenu.api.menu.controller;

import com.easymenu.api.menu.service.MenuItemRelationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/menu/item/relation")
@Tag(name = "MenuItemRelation Controller", description = "Endpoints for menu item relation operations")
public class MenuItemRelationController {
    private final MenuItemRelationService menuItemRelationService;

    @Operation(
        summary = "Add a relation between a menu and a menu item",
        description = "Creates a relation between a menu and a menu item, linking them together.",
        responses = {
            @ApiResponse(responseCode = "204", description = "Relation successfully added"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        }
    )
    @PostMapping(value = "/add")
    public ResponseEntity<Void> addRelation(
        @RequestParam Long menuId,
        @RequestParam Long menuItemId) {
        menuItemRelationService.addRelation(menuId, menuItemId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Change menu item availability",
        description = "Updates the availability status of a menu item in a specific menu.",
        responses = {
            @ApiResponse(responseCode = "204", description = "Menu item availability successfully changed"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
            @ApiResponse(responseCode = "404", description = "Menu or menu item not found"),
        }
    )
    @PostMapping(value = "/change-availability")
    public ResponseEntity<Void> changeMenuItemAvailability(
            @RequestParam Long menuId,
            @RequestParam Long menuItemId) {
        menuItemRelationService.changeMenuItemAvailability(menuId, menuItemId);
        return ResponseEntity.noContent().build();
    }
}
