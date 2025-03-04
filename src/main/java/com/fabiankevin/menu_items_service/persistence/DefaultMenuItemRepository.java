package com.fabiankevin.menu_items_service.persistence;


import com.fabiankevin.menu_items_service.models.MenuItem;
import com.fabiankevin.menu_items_service.persistence.entites.MenuItemEntity;
import com.fabiankevin.menu_items_service.persistence.jpa.JpaMenuItemRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class DefaultMenuItemRepository implements MenuItemRepository {

    private final JpaMenuItemRepository jpaRepository;

    public DefaultMenuItemRepository(JpaMenuItemRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public MenuItem save(MenuItem menuItem) {
        MenuItemEntity entity = MenuItemEntity.fromModel(menuItem);
        return jpaRepository.save(entity).toModel();
    }

    @Override
    public Optional<MenuItem> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(MenuItemEntity::toModel);
    }

    @Override
    public List<MenuItem> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(MenuItemEntity::toModel)
                .toList();
    }

    @Override
    public List<MenuItem> findAllByTenantId(UUID id) {
        return jpaRepository.findByTenantId(id)
                .map(MenuItemEntity::toModel)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}