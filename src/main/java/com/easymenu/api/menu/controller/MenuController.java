package com.easymenu.api.menu.controller;

import com.easymenu.api.menu.dto.MenuDTO;
import com.easymenu.api.menu.dto.MenuItemDTO;
import com.easymenu.api.menu.service.MenuService;
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
@RequestMapping("/menu")
@Tag(name = "Menu Controller", description = "Endpoints for menu-related operations and attributes")
public class MenuController {
    private final MenuService menuService;

    @Operation(
        summary = "Find all menus by enterprise",
        description = "Retrieve a list of all menus associated with a specific enterprise",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved menus"),
            @ApiResponse(responseCode = "404", description = "No menus found for the given enterprise ID")
        }
    )
    @GetMapping(value = "/enterprise/{id}/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MenuDTO>> findAllByEnterprise(@PathVariable Long id) {
        List<MenuDTO> menus = menuService.findAllByEnterprise(id);
        if (menus.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(menus);
    }

    @Operation(
        summary = "Find available menu items by menu",
        description = "Retrieve a list of available menu items for a specific menu",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved menu items"),
            @ApiResponse(responseCode = "404", description = "No menu items found for the given menu ID")
        }
    )
    @GetMapping(value = "/{id}/items", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MenuItemDTO>> findAvailableMenuItemsByMenu(@PathVariable Long id) {
        List<MenuItemDTO> menuItems = menuService.findAvailableMenuItemsByMenu(id);
        if (menuItems.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(menuItems);
    }

    @Operation(
        summary = "Find menu by ID",
        description = "Retrieve details of a menu by its ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved menu"),
            @ApiResponse(responseCode = "404", description = "Menu not found")
        }
    )
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MenuDTO> findMenuById(@PathVariable Long id) {
        MenuDTO menuDTO = menuService.findMenuById(id);
        return ResponseEntity.ok(menuDTO);
    }

    @Operation(
        summary = "Create a new menu",
        description = "Add a new menu to the system",
        responses = {
            @ApiResponse(responseCode = "201", description = "Successfully created menu"),
            @ApiResponse(responseCode = "400", description = "Invalid menu data")
        }
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MenuDTO> createUser(@Validated(MenuDTO.Creation.class) @RequestBody MenuDTO menuDTO) {
        MenuDTO createdMenu = menuService.createMenu(menuDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMenu);
    }

    @Operation(
        summary = "Update an existing menu",
        description = "Modify the details of an existing menu",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully updated menu"),
            @ApiResponse(responseCode = "400", description = "Invalid menu data"),
            @ApiResponse(responseCode = "404", description = "Menu not found")
        }
    )
    @PatchMapping(
        value = "/{id}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MenuDTO> updateMenu(
        @PathVariable Long id,
        @Validated(MenuDTO.Update.class) @RequestBody MenuDTO menuDTO) {
        MenuDTO updatedMenu = menuService.updateMenu(id, menuDTO);
        return ResponseEntity.ok(updatedMenu);
    }

    @Operation(
        summary = "Disable a menu",
        description = "Disable a menu by its ID and its enterprise ID",
        responses = {
            @ApiResponse(responseCode = "204", description = "Successfully disabled menu"),
            @ApiResponse(responseCode = "404", description = "Menu not found")
        }
    )
    @PatchMapping(value = "/{id}/disable")
    public ResponseEntity<Void> disableMenu(@PathVariable Long id, @RequestParam Long enterpriseId) {
        menuService.disableMenu(id, enterpriseId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Delete a menu",
        description = "Delete a menu by its ID and its enterprise ID",
        responses = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted menu"),
            @ApiResponse(responseCode = "404", description = "Menu not found")
        }
    )
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long id, @RequestParam Long enterpriseId) {
        menuService.deleteMenu(id, enterpriseId);
        return ResponseEntity.noContent().build();
    }
}
