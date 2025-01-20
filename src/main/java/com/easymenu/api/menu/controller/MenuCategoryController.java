package com.easymenu.api.menu.controller;

import com.easymenu.api.menu.dto.MenuCategoryDTO;
import com.easymenu.api.menu.service.MenuCategoryService;
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
@RequestMapping("/category")
@Tag(name = "MenuCategory Controller", description = "Endpoints for menu category-related operations and attributes")
public class MenuCategoryController {
    private final MenuCategoryService menuCategoryService;

    @Operation(
        summary = "Find all menu categories by enterprise ID",
        description = "Retrieve all menu categories associated with a specific enterprise ID.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of menu categories"),
            @ApiResponse(responseCode = "404", description = "No menu categories found for the given enterprise ID")
        }
    )
    @GetMapping(value = "/enterprise/{id}/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MenuCategoryDTO>> findAllByEnterprise(@PathVariable Long id) {
        List<MenuCategoryDTO> categories = menuCategoryService.findAllByEnterprise(id);
        if (categories.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(categories);
    }

    @Operation(
        summary = "Find menu category by ID",
        description = "Retrieve a specific menu category by its ID.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the menu category"),
            @ApiResponse(responseCode = "404", description = "Menu category not found")
        }
    )
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MenuCategoryDTO> findMenuCategoryById(@PathVariable Long id) {
        MenuCategoryDTO menuCategoryDTO = menuCategoryService.findMenuCategoryById(id);
        return ResponseEntity.ok(menuCategoryDTO);
    }

    @Operation(
        summary = "Create a new menu category",
        description = "Create a new menu category with the provided details.",
        responses = {
            @ApiResponse(responseCode = "201", description = "Successfully created the menu category"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
        }
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MenuCategoryDTO> createMenuCategory(
        @Validated(MenuCategoryDTO.Creation.class) @RequestBody MenuCategoryDTO menuCategoryDTO) {
        MenuCategoryDTO createdMenuCategory = menuCategoryService.createMenuCategory(menuCategoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMenuCategory);
    }

    @Operation(
        summary = "Update an existing menu category",
        description = "Update the details of an existing menu category by its ID.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the menu category"),
            @ApiResponse(responseCode = "404", description = "Menu category not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
        }
    )
    @PatchMapping(
        value = "/{id}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MenuCategoryDTO> updateMenuCategory(
            @PathVariable Long id,
            @Validated(MenuCategoryDTO.Update.class) @RequestBody MenuCategoryDTO menuCategoryDTO) {
        MenuCategoryDTO updatedMenuCategory = menuCategoryService.updateMenuCategory(id, menuCategoryDTO);
        return ResponseEntity.ok(updatedMenuCategory);
    }

    @Operation(
        summary = "Delete a menu category",
        description = "Delete a menu category by its ID for a specific enterprise.",
        responses = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted the menu category"),
            @ApiResponse(responseCode = "404", description = "Menu category not found")
        }
    )
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteMenuCategory(@PathVariable Long id, @RequestParam Long enterpriseId) {
        menuCategoryService.deleteMenuCategory(id, enterpriseId);
        return ResponseEntity.noContent().build();
    }
}
