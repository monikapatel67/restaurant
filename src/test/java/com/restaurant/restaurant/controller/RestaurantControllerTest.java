package com.restaurant.restaurant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.restaurant.dto.RestaurantDTO;
import com.restaurant.restaurant.dto.enums.CuisineType;
import com.restaurant.restaurant.dto.enums.PriceRange;
import com.restaurant.restaurant.entity.Restaurant;
import com.restaurant.restaurant.service.RestaurantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RestaurantService service;

    private RestaurantDTO validRestaurantDTO;
    private List<Restaurant> top3Restaurants;


    @BeforeEach
    void setUp() {
        validRestaurantDTO = new RestaurantDTO("Test Restaurant", CuisineType.ITALIAN, "123 Test Street", PriceRange.MEDIUM);

        top3Restaurants = Arrays.asList(
                new Restaurant(1L, "Italian Delight", CuisineType.ITALIAN, "123 Main St", PriceRange.MEDIUM),
                new Restaurant(2L, "Pasta Palace", CuisineType.ITALIAN, "456 Elm St", PriceRange.HIGH),
                new Restaurant(3L, "Romeo's Pizza", CuisineType.ITALIAN, "789 Oak St", PriceRange.LOW)
        );

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateRestaurant_Valid() throws Exception {
        mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRestaurantDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetAllRestaurants() throws Exception {
        mockMvc.perform(get("/api/restaurants"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetTop3RestaurantsByCuisine() throws Exception {
        CuisineType cuisineType = CuisineType.ITALIAN;

        when(service.getTop3ByCuisine(cuisineType)).thenReturn(top3Restaurants);

        mockMvc.perform(get("/api/restaurants/top3")
                        .param("cuisineType", cuisineType.name()) // Check what is being sent
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].name").value("Italian Delight"))
                .andExpect(jsonPath("$[1].name").value("Pasta Palace"))
                .andExpect(jsonPath("$[2].name").value("Romeo's Pizza"));
    }


}
