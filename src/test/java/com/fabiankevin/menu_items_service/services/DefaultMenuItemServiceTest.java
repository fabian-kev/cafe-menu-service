package com.fabiankevin.menu_items_service.services;

import com.fabiankevin.menu_items_service.exceptions.MenuItemException;
import com.fabiankevin.menu_items_service.models.MenuItem;
import com.fabiankevin.menu_items_service.persistence.MenuItemRepository;
import com.fabiankevin.menu_items_service.services.commands.CreateMenuItemCommand;
import com.fabiankevin.menu_items_service.services.commands.UpdateMenuItemCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultMenuItemServiceTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @InjectMocks
    private DefaultMenuItemService menuItemService;

    private UUID tenantId;
    private UUID itemId;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        itemId = UUID.randomUUID();
    }

    @Test
    void createMenuItem_givenValidCommand_thenShouldSave() {
        CreateMenuItemCommand command = new CreateMenuItemCommand(
            tenantId, "Test Item", "Description", BigDecimal.TEN, "image.jpg", true
        );
        MenuItem expectedItem = command.toModel();
        expectedItem.setId(itemId);
        expectedItem.setCreatedAt(Instant.now());
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(expectedItem);

        MenuItem result = menuItemService.createMenuItem(command);

        assertNotNull(result.getId(), "ID should be generated");
        assertEquals(tenantId, result.getTenantId(), "Tenant ID should match");
        assertEquals("Test Item", result.getName(), "Name should match");
        assertNotNull(result.getCreatedAt(), "CreatedAt should be set");
        verify(menuItemRepository, times(1)).save(any(MenuItem.class));
    }

    @Test
    void updateMenuItem_givenValidCommand_thenShouldUpdate() {
        UpdateMenuItemCommand command = new UpdateMenuItemCommand(
            "Updated Item", "New Desc", BigDecimal.valueOf(15), "new-image.jpg", false
        );
        MenuItem existingItem = new MenuItem(itemId, tenantId, "Old Item", "Old Desc",
            BigDecimal.TEN, "old.jpg", true, Instant.now(), Instant.now());
        when(menuItemRepository.findById(itemId)).thenReturn(Optional.of(existingItem));
        when(menuItemRepository.save(any(MenuItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MenuItem result = menuItemService.updateMenuItem(itemId, command);

        assertEquals("Updated Item", result.getName(), "Name should be updated");
        assertEquals(tenantId, result.getTenantId(), "Tenant ID should be preserved");
        assertEquals(existingItem.getCreatedAt(), result.getCreatedAt(), "CreatedAt should be preserved");
        assertEquals(false, result.isAvailable(), "Available should be updated");
        verify(menuItemRepository, times(1)).save(any(MenuItem.class));
    }

    @Test
    void updateMenuItem_givenNonExistingId_thenShouldThrowException() {
        UpdateMenuItemCommand command = new UpdateMenuItemCommand(
            "Updated Item", null, null, null, null
        );
        when(menuItemRepository.findById(itemId)).thenReturn(Optional.empty());

        MenuItemException exception = assertThrows(MenuItemException.class,
            () -> menuItemService.updateMenuItem(itemId, command));
        
        assertEquals("Menu item not found with id: " + itemId, exception.getMessage(),
            "Exception message should match");
        verify(menuItemRepository, never()).save(any());
    }

    @Test
    void getMenuItem_givenExistingId_thenShouldReturnItem() {
        MenuItem expectedItem = new MenuItem(itemId, tenantId, "Test Item", "Desc",
            BigDecimal.TEN, "image.jpg", true, Instant.now(), Instant.now());
        when(menuItemRepository.findById(itemId)).thenReturn(Optional.of(expectedItem));

        MenuItem result = menuItemService.getMenuItem(itemId);

        assertEquals(expectedItem, result, "Returned item should match expected");
        verify(menuItemRepository, times(1)).findById(itemId);
    }

    @Test
    void getMenuItem_givenNonExistingId_thenShouldThrowException() {
        when(menuItemRepository.findById(itemId)).thenReturn(Optional.empty());

        MenuItemException exception = assertThrows(MenuItemException.class,
            () -> menuItemService.getMenuItem(itemId));
        
        assertEquals("Menu item not found with id: " + itemId, exception.getMessage(),
            "Exception message should match");
    }

    @Test
    void getMenuItemsByTenant_givenValidTenantId_thenShouldReturnList() {
        MenuItem item = new MenuItem(itemId, tenantId, "Test Item", "Desc",
            BigDecimal.TEN, "image.jpg", true, Instant.now(), Instant.now());
        List<MenuItem> expectedList = List.of(item);
        when(menuItemRepository.findAllByTenantId(tenantId)).thenReturn(expectedList);

        List<MenuItem> result = menuItemService.getMenuItemsByTenant(tenantId);

        assertEquals(1, result.size(), "List size should be 1");
        assertEquals(item, result.get(0), "Item should match expected");
        verify(menuItemRepository, times(1)).findAllByTenantId(tenantId);
    }

    @Test
    void getMenuItemsByTenant_givenNullTenantId_thenShouldThrowException() {
        MenuItemException exception = assertThrows(MenuItemException.class,
            () -> menuItemService.getMenuItemsByTenant(null));
        
        assertEquals("Tenant ID cannot be null", exception.getMessage(),
            "Exception message should match");
        verify(menuItemRepository, never()).findAllByTenantId(any());
    }

    @Test
    void deleteMenuItem_givenExistingId_thenShouldDelete() {
        MenuItem existingItem = new MenuItem(itemId, tenantId, "Test Item", "Desc",
            BigDecimal.TEN, "image.jpg", true, Instant.now(), Instant.now());
        when(menuItemRepository.findById(itemId)).thenReturn(Optional.of(existingItem));

        menuItemService.deleteMenuItem(itemId);

        verify(menuItemRepository, times(1)).deleteById(itemId);
    }

    @Test
    void deleteMenuItem_givenNonExistingId_thenShouldDoNothing() {
        when(menuItemRepository.findById(itemId)).thenReturn(Optional.empty());

        menuItemService.deleteMenuItem(itemId);

        verify(menuItemRepository, never()).deleteById(any());
    }
}