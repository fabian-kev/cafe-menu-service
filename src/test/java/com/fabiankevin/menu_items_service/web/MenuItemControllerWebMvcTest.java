package com.fabiankevin.menu_items_service.web;

import com.fabiankevin.menu_items_service.exceptions.MenuItemException;
import com.fabiankevin.menu_items_service.models.MenuItem;
import com.fabiankevin.menu_items_service.services.MenuItemService;
import com.fabiankevin.menu_items_service.web.dto.ErrorResponse;
import com.fabiankevin.menu_items_service.web.dto.MenuItemRequest;
import com.fabiankevin.menu_items_service.web.dto.MenuItemResponse;
import com.fabiankevin.menu_items_service.web.dto.MenuItemUpdateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(MenuItemController.class)
class MenuItemControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MenuItemService menuItemService;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID tenantId;
    private UUID itemId;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        itemId = UUID.randomUUID();
    }

    @Test
    void createMenuItem_givenValidRequest_thenShouldReturnResponse() throws Exception {
        MenuItemRequest request = new MenuItemRequest(tenantId, "Test Item", "Desc", 
            BigDecimal.TEN, "image.jpg", true);
        MenuItem menuItem = new MenuItem(itemId, tenantId, "Test Item", "Desc", 
            BigDecimal.TEN, "image.jpg", true, Instant.now(), Instant.now());
        when(menuItemService.createMenuItem(any())).thenReturn(menuItem);

        MvcResult result = mockMvc.perform(post("/api/menu-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andReturn();

        MenuItemResponse response = objectMapper.readValue(
            result.getResponse().getContentAsString(), MenuItemResponse.class);
        
        assertEquals(itemId, response.getId(), "ID should match");
        assertEquals("Test Item", response.getName(), "Name should match");
        verify(menuItemService, times(1)).createMenuItem(any());
    }

    @Test
    void createMenuItem_givenInvalidRequest_thenShouldReturnBadRequest() throws Exception {
        MenuItemRequest request = new MenuItemRequest(null, "", null, 
            BigDecimal.valueOf(-1), "image.jpg", true);

        MvcResult result = mockMvc.perform(post("/api/menu-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andReturn();

        ErrorResponse error = objectMapper.readValue(
            result.getResponse().getContentAsString(), ErrorResponse.class);
        
        assertEquals("Validation Failed", error.getMessage(), "Error message should match");
        assertTrue(error.getErrors().contains("Tenant ID is required"), "Should include tenant ID error");
        assertTrue(error.getErrors().contains("Name is required"), "Should include name error");
        assertTrue(error.getErrors().contains("Price must be non-negative"), "Should include price error");
        verify(menuItemService, never()).createMenuItem(any());
    }

    @Test
    void updateMenuItem_givenValidRequest_thenShouldReturnUpdatedResponse() throws Exception {
        MenuItemUpdateRequest request = new MenuItemUpdateRequest("Updated Item", "New Desc", 
            BigDecimal.valueOf(15), "new.jpg", false);
        MenuItem updatedItem = new MenuItem(itemId, tenantId, "Updated Item", "New Desc", 
            BigDecimal.valueOf(15), "new.jpg", false, Instant.now(), Instant.now());
        when(menuItemService.updateMenuItem(eq(itemId), any())).thenReturn(updatedItem);

        MvcResult result = mockMvc.perform(put("/api/menu-items/{id}", itemId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andReturn();

        MenuItemResponse response = objectMapper.readValue(
            result.getResponse().getContentAsString(), MenuItemResponse.class);
        
        assertEquals("Updated Item", response.getName(), "Name should be updated");
        assertEquals(BigDecimal.valueOf(15), response.getPrice(), "Price should be updated");
        verify(menuItemService, times(1)).updateMenuItem(eq(itemId), any());
    }

    @Test
    void updateMenuItem_givenInvalidPrice_thenShouldReturnBadRequest() throws Exception {
        MenuItemUpdateRequest request = new MenuItemUpdateRequest("Updated Item", "Desc", 
            BigDecimal.valueOf(-1), "image.jpg", true);

        MvcResult result = mockMvc.perform(put("/api/menu-items/{id}", itemId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andReturn();

        ErrorResponse error = objectMapper.readValue(
            result.getResponse().getContentAsString(), ErrorResponse.class);
        
        assertEquals("Validation Failed", error.getMessage(), "Error message should match");
        assertTrue(error.getErrors().contains("Price must be non-negative"), "Should include price");
        verify(menuItemService, never()).updateMenuItem(any(), any());
    }

    @Test
    void getMenuItem_givenValidId_thenShouldReturnResponse() throws Exception {
        MenuItem menuItem = new MenuItem(itemId, tenantId, "Test Item", "Desc", 
            BigDecimal.TEN, "image.jpg", true, Instant.now(), Instant.now());
        when(menuItemService.getMenuItem(itemId)).thenReturn(menuItem);

        MvcResult result = mockMvc.perform(get("/api/menu-items/{id}", itemId))
            .andExpect(status().isOk())
            .andReturn();

        MenuItemResponse response = objectMapper.readValue(
            result.getResponse().getContentAsString(), MenuItemResponse.class);
        
        assertEquals(itemId, response.getId(), "ID should match");
        assertEquals("Test Item", response.getName(), "Name should match");
        verify(menuItemService, times(1)).getMenuItem(itemId);
    }

    @Test
    void getMenuItem_givenNonExistingId_thenShouldReturnNotFound() throws Exception {
        when(menuItemService.getMenuItem(itemId))
            .thenThrow(new MenuItemException("Menu item not found with id: " + itemId));

        MvcResult result = mockMvc.perform(get("/api/menu-items/{id}", itemId))
            .andExpect(status().isNotFound())
            .andReturn();

        ErrorResponse error = objectMapper.readValue(
            result.getResponse().getContentAsString(), ErrorResponse.class);
        
        assertEquals("Resource Error", error.getMessage(), "Error message should match");
        assertEquals("Menu item not found with id: " + itemId, error.getDetails(), "Details should match");
        verify(menuItemService, times(1)).getMenuItem(itemId);
    }

    @Test
    void getMenuItemsByTenant_givenValidTenantId_thenShouldReturnList() throws Exception {
        MenuItem menuItem = new MenuItem(itemId, tenantId, "Test Item", "Desc", 
            BigDecimal.TEN, "image.jpg", true, Instant.now(), Instant.now());
        List<MenuItem> items = List.of(menuItem);
        when(menuItemService.getMenuItemsByTenant(tenantId)).thenReturn(items);

        MvcResult result = mockMvc.perform(get("/api/menu-items/tenant/{tenantId}", tenantId))
            .andExpect(status().isOk())
            .andReturn();

        List<MenuItemResponse> response = objectMapper.readValue(
            result.getResponse().getContentAsString(), 
            objectMapper.getTypeFactory().constructCollectionType(List.class, MenuItemResponse.class));
        
        assertEquals(1, response.size(), "List size should be 1");
        assertEquals("Test Item", response.get(0).getName(), "Name should match");
        verify(menuItemService, times(1)).getMenuItemsByTenant(tenantId);
    }

    @Test
    void deleteMenuItem_givenValidId_thenShouldReturnOk() throws Exception {
        doNothing().when(menuItemService).deleteMenuItem(itemId);

        mockMvc.perform(delete("/api/menu-items/{id}", itemId))
            .andExpect(status().isOk());

        verify(menuItemService, times(1)).deleteMenuItem(itemId);
    }

    @Test
    void deleteMenuItem_givenNonExistingId_thenShouldReturnNotFound() throws Exception {
        doThrow(new MenuItemException("Menu item not found with id: " + itemId))
            .when(menuItemService).deleteMenuItem(itemId);

        MvcResult result = mockMvc.perform(delete("/api/menu-items/{id}", itemId))
            .andExpect(status().isNotFound())
            .andReturn();

        ErrorResponse error = objectMapper.readValue(
            result.getResponse().getContentAsString(), ErrorResponse.class);
        
        assertEquals("Resource Error", error.getMessage(), "Error message should match");
        assertEquals("Menu item not found with id: " + itemId, error.getDetails(), "Details should match");
        verify(menuItemService, times(1)).deleteMenuItem(itemId);
    }
}