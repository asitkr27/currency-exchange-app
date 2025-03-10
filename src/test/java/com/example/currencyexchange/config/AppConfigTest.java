package com.example.currencyexchange.config;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class AppConfigTest {

    @Test
    void restTemplateBeanShouldBeCreated() {
        // Load the application context with AppConfig
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // Retrieve the RestTemplate bean
        RestTemplate restTemplate = context.getBean(RestTemplate.class);

        // Assert that it is not null
        assertNotNull(restTemplate, "RestTemplate bean should not be null");
    }
}
