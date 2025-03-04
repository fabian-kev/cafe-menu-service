package com.fabiankevin.menu_items_service.web.dto;

import com.fabiankevin.menu_items_service.services.commands.UpdateMenuItemCommand;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemUpdateRequest {
    @NotBlank(message = "Name cannot be blank if provided")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be non-negative")
    private BigDecimal price;

    @Size(max = 2000, message = "Image URL must not exceed 2000 characters")
    private String imageUrl;

    private Boolean available;

    public UpdateMenuItemCommand toCommand() {
        return new UpdateMenuItemCommand(
                name,
                description,
                price,
                imageUrl,
                available
        );
    }
}