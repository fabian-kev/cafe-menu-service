package com.fabiankevin.menu_items_service.web.dto;

import com.fabiankevin.menu_items_service.services.commands.CreateMenuItemCommand;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemRequest {
    @NotNull(message = "Tenant ID is required")
    private UUID tenantId;

    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be non-negative")
    private BigDecimal price;

    @Size(max = 2000, message = "Image URL must not exceed 2000 characters")
    private String imageUrl;

    private Boolean available;


    public CreateMenuItemCommand toCommand() {
        return new CreateMenuItemCommand(
            tenantId,
            name,
            description,
            price,
            imageUrl,
            available
        );
    }
}