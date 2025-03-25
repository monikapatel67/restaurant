package com.restaurant.restaurant.service;

import com.restaurant.restaurant.dto.ReviewDTO;
import com.restaurant.restaurant.dto.enums.ReviewStatus;
import com.restaurant.restaurant.entity.Review;
import com.restaurant.restaurant.mapper.CustomMapper;
import com.restaurant.restaurant.repository.ReviewRepository;
import com.restaurant.restaurant.service.impl.ReviewServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private ReviewDTO reviewDTO;
    private Review review;

    @Mock
    private CustomMapper customMapper;

    @BeforeEach
    void setUp() {
        reviewDTO = new ReviewDTO(1L, 4, "Nice place!", LocalDate.now(), ReviewStatus.APPROVED);
        review = new Review(1L, null, 4, "Nice place!", LocalDate.now(), ReviewStatus.APPROVED);
    }

    @Test
    void testSubmitReview_Success() {
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        when(customMapper.toReviewEntity(reviewDTO)).thenReturn(review);
        when(customMapper.toReviewDTO(review)).thenReturn(reviewDTO);

        ReviewDTO saved = reviewService.save(reviewDTO);
        assertNotNull(saved);
        assertEquals("Nice place!", saved.getComment());
    }
}
