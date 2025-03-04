// src/test/java/com/example/menuservice/steps/DeleteMenuItemSteps.java
package com.fabiankevin.menu_items_service.steps;

import com.fabiankevin.menu_items_service.web.dto.MenuItemRequest;
import com.fabiankevin.menu_items_service.web.dto.MenuItemResponse;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommonSteps {

    private final TestContext testContext;

    @Autowired
    public CommonSteps(TestContext testContext) {
        this.testContext = testContext;
    }

    @Given("an existing menu item with ID {string}")
    @Step("Setting up existing menu item with ID: {0}")
    public void givenExistingMenuItemWithId(String itemIdStr) {
        testContext.setItemId(UUID.fromString(itemIdStr));
        // Create an item to ensure it exists
        MenuItemRequest request = new MenuItemRequest(testContext.getTenantId(), "Initial Item", "Initial Desc",
                BigDecimal.TEN, "initial.jpg", true);
        ResponseEntity<MenuItemResponse> response = testContext.getRestClient().post()
                .uri("/api/menu-items")
                .body(request)
                .retrieve()
                .toEntity(MenuItemResponse.class);
        testContext.setItemId(response.getBody().getId()); // Use the real generated ID
    }

    @Then("the response should contain the name {string} and price {double}")
    @Step("Verifying response contains name: {0} and price: {1}")
    public void thenResponseContainsNameAndPrice(String name, double price) {
        MenuItemResponse body = testContext.getResponse();
        assertNotNull(body, "Response body should not be null");
        assertEquals(name, body.getName(), "Name should match");
        assertEquals(BigDecimal.valueOf(price), body.getPrice(), "Price should match");
    }
}