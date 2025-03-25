package com.restaurant.restaurant.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.restaurant.dto.AuthRequest;
import com.restaurant.restaurant.dto.AuthResponse;
import com.restaurant.restaurant.dto.RestaurantDTO;
import com.restaurant.restaurant.dto.enums.CuisineType;
import com.restaurant.restaurant.dto.enums.PriceRange;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RestaurantIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String jwtToken;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api/restaurants";
    }

    @BeforeAll
    void setUp() {
        createAdminUser();
        loginAdmin();
    }

    void createAdminUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = """
                {
                    "username": "admin",
                    "password": "password",
                    "email": "admin@example.com",
                    "role": "ADMIN"
                }
                """;

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/api/auth/register", HttpMethod.POST, request, String.class);

        System.out.println("Admin User Creation Response: " + response.getStatusCode() + " | " + response.getBody());
        assertTrue(response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK,
                "Admin user should be created or already exists");
    }

    void loginAdmin() {
        AuthRequest authRequest = new AuthRequest("admin", "password");
        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/auth/login",
                authRequest,
                String.class // Change response type to String for debugging
        );

        System.out.println("Login Response Status: " + response.getStatusCode());
        System.out.println("Login Response Body: " + response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Login request failed!");
        assertNotNull(response.getBody(), "Auth response should not be null");

        // Manually parse JWT token
        AuthResponse authResponse = null;
        try {
            authResponse = new ObjectMapper().readValue(response.getBody(), AuthResponse.class);
        } catch (Exception e) {
            System.out.println("Failed to parse AuthResponse: " + e.getMessage());
        }

        assertNotNull(authResponse, "Parsed AuthResponse should not be null");
        jwtToken = authResponse.getToken();
        assertNotNull(jwtToken, "JWT Token should not be null");
    }


    @Test
    void createRestaurant_Success() {
        RestaurantDTO dto = new RestaurantDTO("The Great Dine", CuisineType.ITALIAN, "123 Main St", PriceRange.MEDIUM);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwtToken);

        HttpEntity<RestaurantDTO> request = new HttpEntity<>(dto, headers);

        ResponseEntity<RestaurantDTO> response = restTemplate.exchange(getBaseUrl(), HttpMethod.POST, request, RestaurantDTO.class);

        System.out.println("Create Restaurant Response: " + response.getStatusCode() + " | " + response.getBody());

        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Restaurant should be created");
        assertNotNull(response.getBody());
        assertEquals("The Great Dine", response.getBody().getName());
    }
}
