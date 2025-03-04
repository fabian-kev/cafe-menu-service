package com.fabiankevin.menu_items_service.persistence.jpa;

import com.fabiankevin.menu_items_service.persistence.entites.MenuItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.util.Streamable;

import java.util.UUID;

public interface JpaMenuItemRepository extends JpaRepository<MenuItemEntity, UUID> {
    Streamable<MenuItemEntity> findByTenantId(UUID tenantId);
}