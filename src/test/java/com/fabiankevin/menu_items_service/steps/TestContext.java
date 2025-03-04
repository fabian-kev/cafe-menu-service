package com.fabiankevin.menu_items_service.steps;

import com.fabiankevin.menu_items_service.web.dto.ErrorResponse;
import com.fabiankevin.menu_items_service.web.dto.MenuItemResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
@Getter
@Setter
public class TestContext {

    @LocalServerPort
    private int port;

    private final RestClient restClient;

    private UUID tenantId;
    private UUID itemId;
    private MenuItemResponse response;
    private ErrorResponse errorResponse;
    private HttpStatusCode httpStatusCode;

    @Autowired
    public TestContext(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl("http://localhost:{port}").build();
    }

    public RestClient getRestClient() {
        return restClient.mutate().baseUrl("http://localhost:" + port).build();
    }
}