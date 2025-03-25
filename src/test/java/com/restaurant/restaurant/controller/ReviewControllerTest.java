package com.restaurant.restaurant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.restaurant.dto.ReviewDTO;
import com.restaurant.restaurant.dto.enums.CuisineType;
import com.restaurant.restaurant.dto.enums.PriceRange;
import com.restaurant.restaurant.dto.enums.ReviewStatus;
import com.restaurant.restaurant.entity.Restaurant;
import com.restaurant.restaurant.entity.Review;
import com.restaurant.restaurant.repository.RestaurantRepository;
import com.restaurant.restaurant.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private ReviewDTO validReviewDTO;
    private ReviewDTO invalidReviewDTO;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    private Long restaurantId;

    @BeforeEach
    void setUp() {

        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        restaurant.setAddress("123 Test Street");
        restaurant.setCuisineType(CuisineType.ITALIAN);
        restaurant.setPriceRange(PriceRange.MEDIUM);

        restaurant = restaurantRepository.save(restaurant);  // Save and get ID
        restaurantId = restaurant.getId();

        Review review = new Review();
        review.setRestaurant(restaurant);
        review.setRating(5);
        review.setComment("Great food!");
        review.setStatus(ReviewStatus.APPROVED);
        review.setVisitDate(LocalDate.now());

        reviewRepository.save(review);

        validReviewDTO = new ReviewDTO(1L, 5, "Excellent food!", LocalDate.now(), ReviewStatus.APPROVED);
        invalidReviewDTO = new ReviewDTO(1L, 6, "", LocalDate.of(2030, 1, 1), ReviewStatus.PENDING);
    }

    @Test
    @WithMockUser(roles = "USER")
    void testSubmitReview_Valid() throws Exception {
        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validReviewDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testSubmitReview_Invalid() throws Exception {
        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidReviewDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetAverageRating() throws Exception {
        mockMvc.perform(get("/api/reviews/average/" + restaurantId))  // Use real restaurant ID
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
