package com.fabiankevin.menu_items_service.web.dto;

import com.fabiankevin.menu_items_service.models.MenuItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemResponse {
    private UUID id;
    private UUID tenantId;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private Boolean available;
    private Instant createdAt;
    private Instant updatedAt;

    public static MenuItemResponse fromModel(MenuItem menuItem) {
        return new MenuItemResponse(
            menuItem.getId(),
            menuItem.getTenantId(),
            menuItem.getName(),
            menuItem.getDescription(),
            menuItem.getPrice(),
            menuItem.getImageUrl(),
            menuItem.isAvailable(),
            menuItem.getCreatedAt(),
            menuItem.getUpdatedAt()
        );
    }
}