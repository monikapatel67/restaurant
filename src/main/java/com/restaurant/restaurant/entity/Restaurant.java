package com.restaurant.restaurant.entity;

import com.restaurant.restaurant.dto.enums.CuisineType;
import com.restaurant.restaurant.dto.enums.PriceRange;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @Enumerated(EnumType.STRING)
    private CuisineType cuisineType;

    @NotBlank
    private String address;

    @Enumerated(EnumType.STRING)
    private PriceRange priceRange;
}


