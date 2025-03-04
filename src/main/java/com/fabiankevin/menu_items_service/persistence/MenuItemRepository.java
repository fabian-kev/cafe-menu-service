package com.fabiankevin.menu_items_service.persistence;

import com.fabiankevin.menu_items_service.models.MenuItem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MenuItemRepository {
    MenuItem save(MenuItem menuItem);
    Optional<MenuItem> findById(UUID id);
    List<MenuItem> findAll();
    List<MenuItem> findAllByTenantId(UUID id);
    void deleteById(UUID id);
}