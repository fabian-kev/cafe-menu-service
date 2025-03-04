package com.fabiankevin.menu_items_service.services.commands;

import com.fabiankevin.menu_items_service.models.MenuItem;
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
public class CreateMenuItemCommand {
    private UUID tenantId;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private Boolean available;

    public MenuItem toModel() {
        return MenuItem.builder()
                .tenantId(this.tenantId)
                .name(this.name)
                .description(this.description)
                .price(this.price)
                .imageUrl(this.imageUrl)
                .build();
    }

    public static CreateMenuItemCommand fromModel(MenuItem menuItem) {
        return new CreateMenuItemCommand(
            menuItem.getTenantId(),
            menuItem.getName(),
            menuItem.getDescription(),
            menuItem.getPrice(),
            menuItem.getImageUrl(),
            menuItem.isAvailable()
        );
    }
}