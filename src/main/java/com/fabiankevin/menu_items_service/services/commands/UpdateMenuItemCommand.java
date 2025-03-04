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
public class UpdateMenuItemCommand {
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private Boolean available;

    public MenuItem toModel(UUID id) {
        return MenuItem.builder()
                .id(id)
                .name(this.name)
                .description(this.description)
                .price(this.price)
                .imageUrl(this.imageUrl)
                .build();
    }
}