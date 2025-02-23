package com.bank.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class OpenApiConfigTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void testCustomOpenAPI() {
        // Retrieve the OpenAPI bean from the application context
        OpenAPI openAPI = context.getBean(OpenAPI.class);

        // Verify that the OpenAPI bean is not null
        assertNotNull(openAPI);

        // Verify the Info object
        Info info = openAPI.getInfo();
        assertNotNull(info);

        // Verify the title
        assertEquals("Bank Account API", info.getTitle());

        // Verify the version
        assertEquals("1.0.0", info.getVersion());

        // Verify the description
        assertEquals("API for managing bank accounts", info.getDescription());
    }
}