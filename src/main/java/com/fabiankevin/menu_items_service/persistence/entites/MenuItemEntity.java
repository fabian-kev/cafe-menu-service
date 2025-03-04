package com.fabiankevin.menu_items_service.persistence.entites;

import com.fabiankevin.menu_items_service.models.MenuItem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity(name = "menu_items")
@Table(name = "menu_items")
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemEntity {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID")
    private UUID id;

    @Column(name = "tenant_id", nullable = false, columnDefinition = "UUID")
    private UUID tenantId;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "available", nullable = false)
    private Boolean available = Boolean.TRUE;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    public MenuItem toModel() {
        return new MenuItem(
                id,
                tenantId,
                name,
                description,
                price,
                imageUrl,
                available,
                createdAt,
                updatedAt
        );
    }

    public static MenuItemEntity fromModel(MenuItem model) {
        return new MenuItemEntity(
                model.getId(),
                model.getTenantId(),
                model.getName(),
                model.getDescription(),
                model.getPrice(),
                model.getImageUrl(),
                model.isAvailable(),
                model.getCreatedAt(),
                model.getUpdatedAt()
        );
    }
}