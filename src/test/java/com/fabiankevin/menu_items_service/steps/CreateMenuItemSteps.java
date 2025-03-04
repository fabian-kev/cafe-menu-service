package com.fabiankevin.menu_items_service.steps;


import com.fabiankevin.menu_items_service.web.dto.ErrorResponse;
import com.fabiankevin.menu_items_service.web.dto.MenuItemRequest;
import com.fabiankevin.menu_items_service.web.dto.MenuItemResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CreateMenuItemSteps {

    private final TestContext testContext;
    private final ObjectMapper objectMapper;

    @Autowired
    public CreateMenuItemSteps(TestContext testContext, ObjectMapper objectMapper) {
        this.testContext = testContext;
        this.objectMapper = objectMapper;
    }

    @Given("a tenant with ID {string}")
    @Step("Setting up tenant with ID: {0}")
    public void givenTenantWithId(String tenantIdStr) {
        testContext.setTenantId(UUID.fromString(tenantIdStr));
    }

    @When("I create a menu item with name {string} and price {double}")
    @Step("Creating menu item with name: {0} and price: {1}")
    public void whenCreateMenuItem(String name, double price) throws JsonProcessingException {
        MenuItemRequest request = new MenuItemRequest(testContext.getTenantId(), name, "Desc",
                BigDecimal.valueOf(price), "image.jpg", true);
        try {
            ResponseEntity<MenuItemResponse> response = testContext.getRestClient().post()
                    .uri("/api/menu-items")
                    .body(request)
                    .retrieve()
                    .toEntity(MenuItemResponse.class);
            testContext.setHttpStatusCode(response.getStatusCode());
            testContext.setResponse(response.getBody());
            testContext.setItemId(response.getBody().getId()); // Store the generated ID
        } catch (RestClientResponseException e) {
            testContext.setErrorResponse(objectMapper.readValue(e.getResponseBodyAsString(), ErrorResponse.class));
            testContext.setHttpStatusCode(e.getStatusCode());
        }
    }

    @Then("the menu item should be created successfully")
    @Step("Verifying menu item creation success")
    public void thenMenuItemCreatedSuccessfully() {
        assertTrue(testContext.getHttpStatusCode().is2xxSuccessful(),
                "Should return 200 OK");
    }


    @Then("the creation should fail with validation errors")
    @Step("Verifying creation failure due to validation")
    public void thenCreationFailsWithValidationErrors() {
        assertTrue(testContext.getHttpStatusCode().is4xxClientError(),
                "Should return 400 Bad Request");
    }

    @Then("the response should contain errors including {string} and {string}")
    @Step("Verifying error response contains: {0} and {1}")
    public void thenResponseContainsErrors(String error1, String error2) {
        ErrorResponse body = testContext.getErrorResponse();
        assertNotNull(body, "Error response body should not be null");
        assertTrue(body.getErrors().contains(error1), "Should contain error: " + error1);
        assertTrue(body.getErrors().contains(error2), "Should contain error: " + error2);
    }
}