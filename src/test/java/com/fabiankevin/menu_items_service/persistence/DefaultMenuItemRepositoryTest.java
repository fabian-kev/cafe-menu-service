package com.fabiankevin.menu_items_service.persistence;


import com.fabiankevin.menu_items_service.models.MenuItem;
import com.fabiankevin.menu_items_service.persistence.entites.MenuItemEntity;
import com.fabiankevin.menu_items_service.persistence.jpa.JpaMenuItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJpaTest
class DefaultMenuItemRepositoryTest {
    @Autowired
    @MockitoSpyBean
    private JpaMenuItemRepository jpaRepository;
    @Autowired
    private DefaultMenuItemRepository defaultMenuItemRepository;

    private MenuItem testMenuItem;
    private MenuItemEntity testEntity;

    @TestConfiguration
    public static class BeanConfiguration {
        @Bean
        public DefaultMenuItemRepository defaultMenuItemRepository(JpaMenuItemRepository jpaMenuItemRepository) {
            return new DefaultMenuItemRepository(jpaMenuItemRepository);
        }
    }

    @BeforeEach
    void setUp() {
        testMenuItem = generateMenuItem();
        testEntity = MenuItemEntity.fromModel(testMenuItem);
        jpaRepository.deleteAll();
    }

    @Test
    void save_givenValidMenuItem_thenShouldSave() {
        // When
        MenuItem result = defaultMenuItemRepository.save(testMenuItem);

        // Then
        verify(jpaRepository, times(1)).save(any());
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(testMenuItem.getName(), result.getName(), "name");
        assertEquals(testMenuItem.getPrice(), result.getPrice(), "price");
        assertEquals(testMenuItem.getDescription(), result.getDescription(), "description");
        assertEquals(testMenuItem.getImageUrl(), result.getImageUrl(), "imageUrl");
        assertEquals(testMenuItem.getTenantId(), result.getTenantId(), "tenantId");
        assertNotNull(result.getCreatedAt(), "createdAt");
        assertNotNull(result.getUpdatedAt(), "updatedAt");
    }

    @Test
    void save_givenNullMenuItem_thenShouldThrowException() {
        // Given & When & Then
        assertThrows(NullPointerException.class, () -> defaultMenuItemRepository.save(null));
        verify(jpaRepository, never()).save(any());
    }

    @Test
    void findById_givenExistingId_thenShouldReturnMenuItem() {
        MenuItemEntity savedMenuItemEntity = jpaRepository.save(testEntity);

        // When
        Optional<MenuItem> optionalMenuItem = defaultMenuItemRepository.findById(savedMenuItemEntity.getId());

        // Then
        assertTrue(optionalMenuItem.isPresent());
        MenuItem result = optionalMenuItem.get();
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(testMenuItem.getName(), result.getName(), "name");
        assertEquals(testMenuItem.getPrice(), result.getPrice(), "price");
        assertEquals(testMenuItem.getDescription(), result.getDescription(), "description");
        assertEquals(testMenuItem.getImageUrl(), result.getImageUrl(), "imageUrl");
        assertEquals(testMenuItem.getTenantId(), result.getTenantId(), "tenantId");
        assertNotNull(result.getCreatedAt(), "createdAt");
        assertNotNull(result.getUpdatedAt(), "updatedAt");
    }

    @Test
    void findById_givenNonExistingId_thenShouldReturnEmpty() {
        // Given
        UUID nonExistingId = UUID.randomUUID();

        // When
        Optional<MenuItem> result = defaultMenuItemRepository.findById(nonExistingId);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_givenMultipleItems_thenShouldReturnAll() {
        // Given
        MenuItem menuItem2 = generateMenuItem();

        jpaRepository.save(testEntity);
        jpaRepository.save(MenuItemEntity.fromModel(menuItem2));

        // When
        List<MenuItem> result = defaultMenuItemRepository.findAll();

        // Then
        assertEquals(2, result.size());
    }

    @Test
    void findAll_givenEmptyDatabase_thenShouldReturnEmptyList() {
        // Given (empty database)

        // When
        List<MenuItem> result = defaultMenuItemRepository.findAll();

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void delete_givenExistingId_thenShouldDelete() {
        // Given
        MenuItemEntity savedMenuItem = jpaRepository.save(testEntity);

        // When
        defaultMenuItemRepository.deleteById(savedMenuItem.getId());

        // Then
        assertFalse(jpaRepository.existsById(savedMenuItem.getId()), "should not exist");
        verify(jpaRepository, times(1)).deleteById(savedMenuItem.getId());
    }

    @Test
    void delete_givenNonExistingId_thenShouldDoNothing() {
        // Given
        UUID nonExistingId = UUID.randomUUID();
        // When
        defaultMenuItemRepository.deleteById(nonExistingId);
        // Then
        assertFalse(jpaRepository.existsById(nonExistingId), "should not exist");
        verify(jpaRepository, times(1)).deleteById(nonExistingId);
    }

    private static MenuItem generateMenuItem() {
        return new MenuItem(
                null,
                UUID.randomUUID(),
                "Test Item",
                "Test Description",
                BigDecimal.valueOf(10.99),
                "http://test.com/image.jpg",
                true,
                Instant.now(),
                Instant.now()
        );
    }
}