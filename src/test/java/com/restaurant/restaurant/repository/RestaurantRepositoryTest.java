package com.restaurant.restaurant.repository;

import com.restaurant.restaurant.entity.Restaurant;
import com.restaurant.restaurant.dto.enums.CuisineType;
import com.restaurant.restaurant.dto.enums.PriceRange;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RestaurantRepositoryTest {

    @Autowired
    private RestaurantRepository repository;

    @Test
    void saveRestaurant_Success() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        restaurant.setCuisineType(CuisineType.ITALIAN);
        restaurant.setAddress("123 Test St");
        restaurant.setPriceRange(PriceRange.MEDIUM);

        Restaurant saved = repository.save(restaurant);
        assertNotNull(saved.getId());
        assertEquals("Test Restaurant", saved.getName());
    }

    @Test
    void findById_Success() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        restaurant.setCuisineType(CuisineType.INDIAN);
        restaurant.setAddress("123 Main St");
        restaurant.setPriceRange(PriceRange.HIGH);

        Restaurant saved = repository.save(restaurant);
        assertTrue(repository.findById(saved.getId()).isPresent());
    }
}
