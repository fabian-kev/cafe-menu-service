//// src/test/java/com/example/menuservice/steps/DeleteMenuItemSteps.java
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
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//public class DeleteMenuItemSteps {
//
//    private final TestContext testContext;
//
//    @Autowired
//    public DeleteMenuItemSteps(TestContext testContext) {
//        this.testContext = testContext;
//    }
//
//    @When("I delete the menu item")
//    @Step("Deleting menu item")
//    public void whenDeleteMenuItem() {
//        testContext.getRestClient().delete()
//                .uri("/api/menu-items/{id}", testContext.getItemId())
//                .retrieve()
//                .toBodilessEntity();
//    }
//
//    @Then("the menu item should be deleted successfully")
//    @Step("Verifying menu item deletion success")
//    public void thenMenuItemDeletedSuccessfully() {
//        ResponseEntity<MenuItemResponse> response = testContext.getRestClient().get()
//                .uri("/api/menu-items/{id}", testContext.getItemId())
//                .retrieve()
//                .onStatus(status -> status.is4xxClientError(), (req, res) -> {})
//                .toEntity(MenuItemResponse.class);
//        assertTrue(response.getStatusCode().is4xxClientError(), "Item should not be found after deletion");
//    }
//}