package com.restaurant.restaurant.dto;

import com.restaurant.restaurant.dto.enums.CuisineType;
import com.restaurant.restaurant.dto.enums.PriceRange;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDTO {

    @NotBlank(message = "Restaurant name cannot be empty")
    private String name;

    private CuisineType cuisineType;

    @NotBlank(message = "Restaurant address cannot be empty")
    private String address;

    private PriceRange priceRange;

}
