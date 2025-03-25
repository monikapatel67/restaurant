package com.restaurant.restaurant.repository;

import com.restaurant.restaurant.entity.Restaurant;
import com.restaurant.restaurant.entity.Review;
import com.restaurant.restaurant.dto.enums.ReviewStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    void saveReview_Success() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        restaurant = restaurantRepository.save(restaurant);

        Review review = new Review();
        review.setRestaurant(restaurant);
        review.setRating(5);
        review.setComment("Amazing food!");
        review.setVisitDate(LocalDate.now());
        review.setStatus(ReviewStatus.APPROVED);

        Review saved = reviewRepository.save(review);
        assertNotNull(saved.getId());
        assertEquals(5, saved.getRating());
    }

    @Test
    void findReviewsByRestaurantId_Success() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        restaurant = restaurantRepository.save(restaurant);

        Review review1 = new Review(null, restaurant, 4, "Great food!", LocalDate.now(), ReviewStatus.APPROVED);
        Review review2 = new Review(null, restaurant, 5, "Excellent!", LocalDate.now(), ReviewStatus.APPROVED);

        reviewRepository.save(review1);
        reviewRepository.save(review2);

        List<Review> reviews = reviewRepository.findAll();
        assertEquals(2, reviews.size());
    }
}
