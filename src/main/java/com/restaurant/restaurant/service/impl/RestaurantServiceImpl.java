package com.restaurant.restaurant.service.impl;

import com.restaurant.restaurant.dto.RestaurantDTO;
import com.restaurant.restaurant.dto.enums.CuisineType;
import com.restaurant.restaurant.entity.Restaurant;
import com.restaurant.restaurant.exception.CustomException;
import com.restaurant.restaurant.exception.ResourceNotFoundException;
import com.restaurant.restaurant.mapper.CustomMapper;
import com.restaurant.restaurant.repository.RestaurantRepository;
import com.restaurant.restaurant.service.RestaurantService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private static final Logger logger = LoggerFactory.getLogger(RestaurantServiceImpl.class);

    @Autowired
    private  RestaurantRepository repository;

    @Autowired
    private  CustomMapper mapper;

    @Override
    public RestaurantDTO save(RestaurantDTO restaurantDTO) {
        logger.info("Saving restaurant: {}", restaurantDTO);

        if (restaurantDTO.getName() == null || restaurantDTO.getName().isEmpty()) {
            logger.error("Restaurant name is empty");
            throw new CustomException("Restaurant name cannot be empty!");
        }
        Restaurant restaurant = mapper.toRestaurantEntity(restaurantDTO);
        Restaurant savedRestaurant = repository.save(restaurant);
        logger.info("Restaurant saved with ID: {}", savedRestaurant.getId());

        return mapper.toRestaurantDTO(savedRestaurant);
    }

    @Override
    public Page<RestaurantDTO> findAll(Pageable pageable) {
        logger.info("Fetching all restaurants with pagination: {}", pageable);

        Page<Restaurant> restaurants = repository.findAll(pageable);
        if (restaurants.isEmpty()) {
            logger.warn("No restaurants found");
            throw new ResourceNotFoundException("No restaurants found!");
        }
        logger.info("Found {} restaurants", restaurants.getTotalElements());
        return restaurants.map(mapper::toRestaurantDTO);
    }

    @Override
    public List<Restaurant> getTop3ByCuisine(CuisineType cuisineType) throws ClassNotFoundException, SQLException {
        try {
            logger.info("Fetching top 3 restaurants for cuisine: {}", cuisineType);

            if (cuisineType == null) {
                logger.warn("Cuisine type is null or empty");
                return Collections.emptyList();
            }

            List<Restaurant> top3 = repository.getTop3RestaurantsByCuisine(cuisineType);
            logger.info("Found {} restaurants for cuisine: {}", top3.size(), cuisineType);
            return top3;

        } catch (Exception e) {
            logger.error("Error fetching top 3 restaurants by cuisine: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch top 3 restaurants", e);
        }
    }
}
