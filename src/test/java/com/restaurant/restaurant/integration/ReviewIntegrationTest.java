package com.restaurant.restaurant.integration;

import com.restaurant.restaurant.dto.AuthRequest;
import com.restaurant.restaurant.dto.AuthResponse;
import com.restaurant.restaurant.dto.ReviewDTO;
import com.restaurant.restaurant.dto.enums.CuisineType;
import com.restaurant.restaurant.dto.enums.PriceRange;
import com.restaurant.restaurant.entity.Restaurant;
import com.restaurant.restaurant.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReviewIntegrationTest {
    @Autowired
    private RestaurantRepository restaurantRepository;

    private Long restaurantId;

    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate = new TestRestTemplate();
    private String jwtToken;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api/reviews";
    }

    @BeforeAll
    void setUp() {
        createTestUser();

        AuthRequest authRequest = new AuthRequest("user", "password");
        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/auth/login",
                authRequest,
                AuthResponse.class
        );

        // Debugging output
        System.out.println("Login Status Code: " + response.getStatusCode());
        System.out.println("Login Response Body: " + response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Login request failed!");
        assertNotNull(response.getBody(), "Auth response should not be null");
        jwtToken = response.getBody().getToken();
        assertNotNull(jwtToken, "JWT Token should not be null");

        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        restaurant.setAddress("123 Test Street");
        restaurant.setCuisineType(CuisineType.ITALIAN);
        restaurant.setPriceRange(PriceRange.MEDIUM);

        restaurant = restaurantRepository.save(restaurant);
        restaurantId = restaurant.getId();
    }

    void createTestUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = """
        {
            "username": "user",
            "password": "password",
            "email": "user@example.com",
            "role": "USER"
        }
        """;

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/auth/register",
                HttpMethod.POST,
                request,
                String.class
        );

        // Debugging output
        System.out.println("User Creation Status Code: " + response.getStatusCode());
        System.out.println("User Creation Response Body: " + response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode(), "User creation failed!");
    }


    @Test
    @WithMockUser(roles = "USER")
    void submitReview_Success() {
        ReviewDTO dto = new ReviewDTO(restaurantId, 5, "Amazing food!", null, null);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwtToken);

        HttpEntity<ReviewDTO> request = new HttpEntity<>(dto, headers);

        ResponseEntity<ReviewDTO> response = restTemplate.exchange(
                getBaseUrl(),
                HttpMethod.POST,
                request,
                ReviewDTO.class
        );

        System.out.println("Review Submission Response: " + response.getStatusCode() + " | " + response.getBody());

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(5, response.getBody().getRating());
    }
}
