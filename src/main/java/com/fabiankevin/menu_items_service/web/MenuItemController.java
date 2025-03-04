package com.fabiankevin.menu_items_service.web;

import com.fabiankevin.menu_items_service.services.MenuItemService;
import com.fabiankevin.menu_items_service.web.dto.ErrorResponse;
import com.fabiankevin.menu_items_service.web.dto.MenuItemRequest;
import com.fabiankevin.menu_items_service.web.dto.MenuItemResponse;
import com.fabiankevin.menu_items_service.web.dto.MenuItemUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/menu-items")
@RequiredArgsConstructor
@Tag(name = "Menu Items", description = "Operations for managing menu items")
public class MenuItemController {

    private final MenuItemService menuItemService;

    @PostMapping
    @Operation(summary = "Create a new menu item", description = "Creates a new menu item for a specific tenant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Menu item created successfully",
                    content = @Content(schema = @Schema(implementation = MenuItemResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public MenuItemResponse createMenuItem(
            @Valid @RequestBody
            @Parameter(description = "Menu item creation request") MenuItemRequest request) {
        return MenuItemResponse.fromModel(
                menuItemService.createMenuItem(request.toCommand())
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing menu item", description = "Updates a menu item by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Menu item updated successfully",
                    content = @Content(schema = @Schema(implementation = MenuItemResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Menu item not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public MenuItemResponse updateMenuItem(
            @PathVariable
            @Parameter(description = "ID of the menu item to update") UUID id,
            @Valid @RequestBody
            @Parameter(description = "Menu item update request") MenuItemUpdateRequest request) {
        return MenuItemResponse.fromModel(
                menuItemService.updateMenuItem(id, request.toCommand())
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a menu item by ID", description = "Retrieves a specific menu item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Menu item retrieved successfully",
                    content = @Content(schema = @Schema(implementation = MenuItemResponse.class))),
            @ApiResponse(responseCode = "404", description = "Menu item not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public MenuItemResponse getMenuItem(
            @PathVariable
            @Parameter(description = "ID of the menu item to retrieve") UUID id) {
        return MenuItemResponse.fromModel(
                menuItemService.getMenuItem(id)
        );
    }

    @GetMapping("/tenant/{tenantId}")
    @Operation(summary = "Get menu items by tenant", description = "Retrieves all menu items for a tenant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Menu items retrieved successfully",
                    content = @Content(schema = @Schema(implementation = MenuItemResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid tenant ID",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public List<MenuItemResponse> getMenuItemsByTenant(
            @PathVariable
            @Parameter(description = "ID of the tenant") UUID tenantId) {
        return menuItemService.getMenuItemsByTenant(tenantId)
                .stream()
                .map(MenuItemResponse::fromModel)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a menu item", description = "Deletes a menu item by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Menu item deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Menu item not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public void deleteMenuItem(
            @PathVariable
            @Parameter(description = "ID of the menu item to delete") UUID id) {
        menuItemService.deleteMenuItem(id);
    }
}