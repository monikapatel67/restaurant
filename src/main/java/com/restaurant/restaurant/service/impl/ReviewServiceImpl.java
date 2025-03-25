package com.restaurant.restaurant.service.impl;

import com.restaurant.restaurant.dto.ReviewDTO;
import com.restaurant.restaurant.entity.Review;
import com.restaurant.restaurant.exception.CustomException;
import com.restaurant.restaurant.exception.ResourceNotFoundException;
import com.restaurant.restaurant.mapper.CustomMapper;
import com.restaurant.restaurant.repository.ReviewRepository;
import com.restaurant.restaurant.service.ReviewService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private static final Logger logger = LoggerFactory.getLogger(ReviewServiceImpl.class);

    @Autowired
    private  ReviewRepository repository;

    @Autowired
    private  CustomMapper customMapper;

    @Override
    public ReviewDTO save(ReviewDTO reviewDTO) {
        logger.info("Saving review: {}", reviewDTO);
        if (reviewDTO.getRating() < 1 || reviewDTO.getRating() > 5) {
            logger.error("Invalid rating: {}. Rating must be between 1 and 5!", reviewDTO.getRating());
            throw new CustomException("Rating must be between 1 and 5!");
        }
        Review review = customMapper.toReviewEntity(reviewDTO);
        Review savedReview = repository.save(review);
        logger.info("Review saved with ID: {}", savedReview.getId());

        return customMapper.toReviewDTO(savedReview);
    }

    @Override
    public Double getAverageRating(Long restaurantId) {
        logger.info("Fetching average rating for restaurant ID: {}", restaurantId);
        Double avgRating = repository.findAverageRating(restaurantId);
        if (avgRating == null) {
            logger.warn("No reviews found for restaurant ID: {}", restaurantId);
            throw new ResourceNotFoundException("No reviews found for this restaurant!");
        }
        logger.info("Average rating for restaurant ID {}: {}", restaurantId, avgRating);
        return avgRating;
    }
}
