package com.restaurant.restaurant.entity;

import com.restaurant.restaurant.dto.enums.ReviewStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Min(1)
    @Max(5)
    private int rating;

    @NotBlank
    private String comment;

    @PastOrPresent
    private LocalDate visitDate;

    @Enumerated(EnumType.STRING)
    private ReviewStatus status;
}

