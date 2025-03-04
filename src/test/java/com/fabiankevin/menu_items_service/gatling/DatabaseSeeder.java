package com.fabiankevin.menu_items_service.gatling;

import com.fabiankevin.menu_items_service.models.MenuItem;
import com.fabiankevin.menu_items_service.services.MenuItemService;
import com.fabiankevin.menu_items_service.services.commands.CreateMenuItemCommand;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatabaseSeeder {

    private static final UUID TENANT_ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");

    public static List<UUID> seedDatabase(int itemCount) {
        ApplicationContext context = new AnnotationConfigApplicationContext("com.example.menuservice");
        MenuItemService menuItemService = context.getBean(MenuItemService.class);
        List<UUID> itemIds = new ArrayList<>();

        for (int i = 0; i < itemCount; i++) {
            CreateMenuItemCommand command = new CreateMenuItemCommand(
                TENANT_ID,
                "Item " + i,
                "Description " + i,
                BigDecimal.valueOf(5.99 + i),
                "item" + i + ".jpg",
                true
            );
            MenuItem menuItem = menuItemService.createMenuItem(command);
            itemIds.add(menuItem.getId());
        }

        return itemIds;
    }
}