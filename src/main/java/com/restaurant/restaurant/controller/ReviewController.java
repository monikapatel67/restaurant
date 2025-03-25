package com.restaurant.restaurant.controller;

import com.restaurant.restaurant.dto.ReviewDTO;
import com.restaurant.restaurant.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    @Autowired
    private  ReviewService service;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public ResponseEntity<ReviewDTO> submitReview(@RequestBody @Valid ReviewDTO reviewDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(reviewDTO));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/average/{restaurantId}")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(service.getAverageRating(restaurantId));
    }
}
