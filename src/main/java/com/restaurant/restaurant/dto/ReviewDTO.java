package com.restaurant.restaurant.dto;

import com.restaurant.restaurant.dto.enums.ReviewStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

    private Long restaurantId;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot be more than 5")
    private int rating;

    @NotBlank(message = "Comment cannot be empty")
    private String comment;

    @PastOrPresent(message = "Visit date must be in the past or today")
    private LocalDate visitDate;

    private ReviewStatus status;
}
