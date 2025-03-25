package com.restaurant.restaurant.mapper;

import com.restaurant.restaurant.dto.RestaurantDTO;
import com.restaurant.restaurant.dto.ReviewDTO;
import com.restaurant.restaurant.entity.Restaurant;
import com.restaurant.restaurant.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomMapper {

    RestaurantDTO toRestaurantDTO(Restaurant restaurant);

    @Mapping(target = "id", ignore = true)
    Restaurant toRestaurantEntity(RestaurantDTO restaurantDTO);

    @Mapping(source = "restaurant.id", target = "restaurantId")
    ReviewDTO toReviewDTO(Review review);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "restaurantId", target = "restaurant.id")
    Review toReviewEntity(ReviewDTO reviewDTO);
}
