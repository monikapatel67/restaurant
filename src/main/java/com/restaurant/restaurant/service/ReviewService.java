package com.restaurant.restaurant.service;

import com.restaurant.restaurant.dto.ReviewDTO;

public interface ReviewService {
    ReviewDTO save(ReviewDTO reviewDTO);

    Double getAverageRating(Long restaurantId);
}
