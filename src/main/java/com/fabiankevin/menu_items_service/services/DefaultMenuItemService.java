package com.fabiankevin.menu_items_service.services;

import com.fabiankevin.menu_items_service.exceptions.MenuItemException;
import com.fabiankevin.menu_items_service.models.MenuItem;
import com.fabiankevin.menu_items_service.persistence.MenuItemRepository;
import com.fabiankevin.menu_items_service.services.commands.CreateMenuItemCommand;
import com.fabiankevin.menu_items_service.services.commands.UpdateMenuItemCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DefaultMenuItemService implements MenuItemService {

    private final MenuItemRepository menuItemRepository;

    @Override
    public MenuItem createMenuItem(CreateMenuItemCommand command) {
        MenuItem menuItem = command.toModel();
        menuItem.setCreatedAt(Instant.now());
        menuItem.setUpdatedAt(Instant.now());
        menuItem.setAvailable(Optional.ofNullable(command.getAvailable()).orElse(true));

        return menuItemRepository.save(menuItem);
    }

    @Override
    public MenuItem updateMenuItem(UUID id, UpdateMenuItemCommand command) {
        MenuItem existingItem = getMenuItem(id);

        Optional.ofNullable(command.getName())
                .ifPresent(existingItem::setName);
        Optional.ofNullable(command.getDescription())
                .ifPresent(existingItem::setDescription);
        Optional.ofNullable(command.getImageUrl())
                .ifPresent(existingItem::setImageUrl);
        Optional.ofNullable(command.getAvailable())
                .ifPresent(existingItem::setAvailable);
        Optional.ofNullable(command.getPrice())
                .ifPresent(existingItem::setPrice);

        return menuItemRepository.save(existingItem);
    }

    @Override
    public MenuItem getMenuItem(UUID id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new MenuItemException("Menu item not found with id: " + id));
    }

    @Override
    public List<MenuItem> getMenuItemsByTenant(UUID tenantId) {
        if (tenantId == null) {
            throw new MenuItemException("Tenant ID cannot be null");
        }
        return menuItemRepository.findAllByTenantId(tenantId);
    }

    @Override
    public void deleteMenuItem(UUID id) {
        menuItemRepository.findById(id).ifPresent(menuItem -> menuItemRepository.deleteById(id));
    }
}