//// src/test/java/com/example/menuservice/steps/RetrieveMenuItemSteps.java
//package com.fabiankevin.menu_items_service.steps;
//
//import com.fabiankevin.menu_items_service.web.dto.MenuItemRequest;
//import com.fabiankevin.menu_items_service.web.dto.MenuItemResponse;
//import io.cucumber.java.en.Given;
//import io.cucumber.java.en.When;
//import io.cucumber.java.en.Then;
//import io.qameta.allure.Step;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.client.RestClient;
//
//import java.math.BigDecimal;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class RetrieveMenuItemSteps {
//
//    private final TestContext testContext;
//
//    @Autowired
//    public RetrieveMenuItemSteps(TestContext testContext) {
//        this.testContext = testContext;
//    }
//
//    @When("I retrieve the menu item by ID")
//    @Step("Retrieving menu item by ID")
//    public void whenRetrieveMenuItemById() {
//        testContext.setResponse(testContext.getRestClient().get()
//                .uri("/api/menu-items/{id}", testContext.getItemId())
//                .retrieve()
//                .toEntity(MenuItemResponse.class));
//    }
//
//    @Then("the menu item should be retrieved successfully")
//    @Step("Verifying menu item retrieval success")
//    public void thenMenuItemRetrievedSuccessfully() {
//        assertTrue(((ResponseEntity<?>) testContext.getResponse()).getStatusCode().is2xxSuccessful(),
//                "Should return 200 OK");
//    }
//
//    @Then("the response should contain the expected details")
//    @Step("Verifying response contains expected details")
//    public void thenResponseContainsExpectedDetails() {
//        MenuItemResponse body = ((ResponseEntity<MenuItemResponse>) testContext.getResponse()).getBody();
//        assertNotNull(body, "Response body should not be null");
//        assertEquals(testContext.getItemId(), body.getId(), "ID should match");
//    }
//}