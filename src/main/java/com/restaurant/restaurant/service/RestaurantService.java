package com.restaurant.restaurant.service;

import com.restaurant.restaurant.dto.RestaurantDTO;
import com.restaurant.restaurant.dto.enums.CuisineType;
import com.restaurant.restaurant.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.SQLException;
import java.util.List;

public interface RestaurantService {
    RestaurantDTO save(RestaurantDTO restaurantDTO);

    Page<RestaurantDTO> findAll(Pageable pageable);

    List<Restaurant> getTop3ByCuisine(CuisineType cuisineType) throws ClassNotFoundException, SQLException;

}
