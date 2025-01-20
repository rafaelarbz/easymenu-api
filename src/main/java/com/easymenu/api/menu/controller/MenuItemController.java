package com.easymenu.api.menu.controller;

import com.easymenu.api.menu.dto.MenuItemDTO;
import com.easymenu.api.menu.service.MenuItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/menu/item")
@Tag(name = "MenuItem Controller", description = "Endpoints for menu item-related operations and attributes")
public class MenuItemController {
    private final MenuItemService menuItemService;

    @Operation(
        summary = "Find all menu items by enterprise ID",
        description = "Retrieve all menu items associated with a specific enterprise ID.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of menu items"),
            @ApiResponse(responseCode = "404", description = "No menu items found for the given enterprise ID")
        }
    )
    @GetMapping(value = "/enterprise/{id}/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MenuItemDTO>> findAllByEnterprise(@PathVariable Long id) {
        List<MenuItemDTO> menuItems = menuItemService.findAllByEnterprise(id);
        if (menuItems.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(menuItems);
    }


    @Operation(
        summary = "Find all menu items by category ID",
        description = "Retrieve all menu items associated with a specific category ID.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of menu items"),
            @ApiResponse(responseCode = "404", description = "No menu items found for the given category ID")
        }
    )
    @GetMapping(value = "/category/{id}/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MenuItemDTO>> findAllByMenuCategory(@PathVariable Long id) {
        List<MenuItemDTO> menuItems = menuItemService.findAllByMenuCategory(id);
        if (menuItems.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(menuItems);
    }

    @Operation(
        summary = "Find all menu items by category and enterprise",
        description = "Retrieve all menu items associated with a specific category and enterprise.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of menu items"),
            @ApiResponse(responseCode = "404", description = "No menu items found for the given category and enterprise")
        }
    )
    @GetMapping(value = "/enterprise/{enterpriseId}/category/{menuCategoryId}/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MenuItemDTO>> findAllByMenuCategoryAndEnterprise(
        @PathVariable Long enterpriseId,
        @PathVariable Long menuCategoryId) {
        List<MenuItemDTO> menuItems = menuItemService.findAllByMenuCategoryAndEnterprise(menuCategoryId, enterpriseId);
        if (menuItems.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(menuItems);
    }

    @Operation(
        summary = "Find menu item by ID",
        description = "Retrieve a specific menu item by its ID.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the menu item"),
            @ApiResponse(responseCode = "404", description = "Menu item not found")
        }
    )
    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MenuItemDTO> findMenuItemById(@PathVariable Long id) {
        MenuItemDTO menuItemDTO = menuItemService.findMenuItemById(id);
        return ResponseEntity.ok(menuItemDTO);
    }

    @Operation(
        summary = "Create a new menu item",
        description = "Create a new menu item with the provided details.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully created the menu item"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
        }
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MenuItemDTO> createMenuItem(@Validated(MenuItemDTO.Creation.class) @RequestBody MenuItemDTO menuItemDTO) {
        MenuItemDTO createdMenuItem = menuItemService.createMenuItem(menuItemDTO);
        return ResponseEntity.ok(createdMenuItem);
    }

    @Operation(
        summary = "Update an existing menu item",
        description = "Update the details of an existing menu item by its ID.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the menu item"),
            @ApiResponse(responseCode = "404", description = "Menu item not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
        }
    )
    @PatchMapping(
        value = "/{id}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MenuItemDTO> updateMenuItem(
        @PathVariable Long id,
        @Validated(MenuItemDTO.Creation.class) @RequestBody MenuItemDTO menuItemDTO) {
        MenuItemDTO updatedMenuItem = menuItemService.updateMenuItem(id, menuItemDTO);
        return ResponseEntity.ok(updatedMenuItem);
    }

    @Operation(
        summary = "Disable a menu item",
        description = "Disable a menu item by its ID for a specific enterprise.",
        responses = {
            @ApiResponse(responseCode = "204", description = "Successfully disabled the menu item"),
            @ApiResponse(responseCode = "404", description = "Menu item not found")
        }
    )
    @PatchMapping(value = "/{id}/disable")
    public ResponseEntity<Void> disableMenuItem(@PathVariable Long id, @RequestParam Long enterpriseId) {
        menuItemService.disableMenuItem(id, enterpriseId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Delete a menu item",
        description = "Delete a menu item by its ID for a specific enterprise.",
        responses = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted the menu item"),
            @ApiResponse(responseCode = "404", description = "Menu item not found")
        }
    )
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id, @RequestParam Long enterpriseId) {
        menuItemService.deleteMenuItem(id, enterpriseId);
        return ResponseEntity.noContent().build();
    }
}
