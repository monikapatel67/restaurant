package com.restaurant.restaurant.integration;

import com.restaurant.restaurant.dto.UserDTO;
import com.restaurant.restaurant.dto.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserIntegrationTest {

    private final TestRestTemplate restTemplate = new TestRestTemplate();
    @LocalServerPort
    private int port;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api/auth";
    }

    @Test
    void registerUser_Success() {
        UserDTO dto = new UserDTO("newUser", "password", Role.USER.name());
        ResponseEntity<String> response = restTemplate.postForEntity(getBaseUrl() + "/register", dto, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User registered successfully.", response.getBody());
    }
}
