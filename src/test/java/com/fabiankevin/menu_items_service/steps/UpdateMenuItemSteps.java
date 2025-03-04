//// src/test/java/com/example/menuservice/steps/UpdateMenuItemSteps.java
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
//public class UpdateMenuItemSteps {
//
//    private final TestContext testContext;
//
//    @Autowired
//    public UpdateMenuItemSteps(TestContext testContext) {
//        this.testContext = testContext;
//    }
//
//    @When("I update the menu item with name {string} and price {double}")
//    @Step("Updating menu item with name: {0} and price: {1}")
//    public void whenUpdateMenuItem(String name, double price) {
//        MenuItemRequest request = new MenuItemRequest(testContext.getTenantId(), name, "Updated Desc",
//                BigDecimal.valueOf(price), "updated.jpg", true);
//        testContext.setResponse(testContext.getRestClient().put()
//                .uri("/api/menu-items/{id}", testContext.getItemId())
//                .body(request)
//                .retrieve()
//                .toEntity(MenuItemResponse.class));
//    }
//
//    @Then("the menu item should be updated successfully")
//    @Step("Verifying menu item update success")
//    public void thenMenuItemUpdatedSuccessfully() {
//        assertTrue(((ResponseEntity<?>) testContext.getResponse()).getStatusCode().is2xxSuccessful(),
//                "Should return 200 OK");
//    }
//}