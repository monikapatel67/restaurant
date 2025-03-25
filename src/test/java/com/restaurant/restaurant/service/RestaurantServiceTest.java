package com.restaurant.restaurant.service;

import com.restaurant.restaurant.dto.RestaurantDTO;
import com.restaurant.restaurant.dto.enums.CuisineType;
import com.restaurant.restaurant.dto.enums.PriceRange;
import com.restaurant.restaurant.entity.Restaurant;
import com.restaurant.restaurant.mapper.CustomMapper;
import com.restaurant.restaurant.repository.RestaurantRepository;
import com.restaurant.restaurant.service.impl.RestaurantServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private CustomMapper customMapper;

    @InjectMocks
    private RestaurantServiceImpl restaurantService;

    private RestaurantDTO restaurantDTO;
    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant(1L, "Test Restaurant", CuisineType.ITALIAN, "123 Test Street", PriceRange.HIGH);
        restaurantDTO = new RestaurantDTO("Test Restaurant", CuisineType.ITALIAN, "123 Test Street", PriceRange.HIGH);
    }

    @Test
    void testSaveRestaurant_Success() {
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);
        when(customMapper.toRestaurantDTO(any(Restaurant.class))).thenReturn(restaurantDTO);
        when(customMapper.toRestaurantEntity(any(RestaurantDTO.class))).thenReturn(restaurant);

        RestaurantDTO saved = restaurantService.save(restaurantDTO);

        assertNotNull(saved);
        assertEquals(restaurantDTO.getName(), saved.getName());
        verify(restaurantRepository, times(1)).save(any(Restaurant.class));
    }
}
