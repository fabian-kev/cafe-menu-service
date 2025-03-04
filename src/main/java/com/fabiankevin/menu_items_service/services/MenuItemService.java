// src/main/java/com/example/menuservice/domain/service/MenuItemService.java
package com.fabiankevin.menu_items_service.services;


import com.fabiankevin.menu_items_service.models.MenuItem;
import com.fabiankevin.menu_items_service.services.commands.CreateMenuItemCommand;
import com.fabiankevin.menu_items_service.services.commands.UpdateMenuItemCommand;

import java.util.List;
import java.util.UUID;

public interface MenuItemService {
    MenuItem createMenuItem(CreateMenuItemCommand command);
    MenuItem updateMenuItem(UUID id, UpdateMenuItemCommand command);
    MenuItem getMenuItem(UUID id);
    List<MenuItem> getMenuItemsByTenant(UUID tenantId);
    void deleteMenuItem(UUID id);
}