package com.fabiankevin.menu_items_service.steps;

import com.fabiankevin.menu_items_service.MenuItemsServiceApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@AutoConfigureMockMvc
@CucumberContextConfiguration
@SpringBootTest(classes = MenuItemsServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SpringContextIntegrationConfig {

}