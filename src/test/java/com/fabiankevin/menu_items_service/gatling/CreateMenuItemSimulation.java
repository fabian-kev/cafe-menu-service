// src/test/java/com/example/menuservice/gatling/CreateMenuItemSimulation.java
package com.fabiankevin.menu_items_service.gatling;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import java.time.Duration;
import java.util.UUID;

public class CreateMenuItemSimulation extends Simulation {

    private final HttpProtocolBuilder httpProtocol = http
        .baseUrl("http://localhost:8080")
        .acceptHeader("application/json")
        .contentTypeHeader("application/json");

    private final ScenarioBuilder createMenuItem = scenario("Create Menu Item")
        .exec(http("POST /api/menu-items")
            .post("/api/menu-items")
            .body(StringBody(
                "{\"tenantId\":\"550e8400-e29b-41d4-a716-446655440000\"," +
                "\"name\":\"Burger" + UUID.randomUUID() + "\"," +
                "\"description\":\"Tasty burger\",\"price\":9.99," +
                "\"imageUrl\":\"burger.jpg\",\"available\":true}"
            ))
            .check(status().is(200)));

    {
        setUp(
            createMenuItem.injectOpen(
                rampUsers(100).during(Duration.ofSeconds(30)) // Ramp up to 100 users over 30s
            )
        ).protocols(httpProtocol)
        .assertions(
            global().responseTime().max().lt(1000), // Max response time < 1s
            global().successfulRequests().percent().gt(95.0) // >95% success
        );
    }
}