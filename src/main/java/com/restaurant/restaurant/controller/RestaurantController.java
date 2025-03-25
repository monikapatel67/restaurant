package com.restaurant.restaurant.controller;

import com.restaurant.restaurant.dto.RestaurantDTO;
import com.restaurant.restaurant.dto.enums.CuisineType;
import com.restaurant.restaurant.entity.Restaurant;
import com.restaurant.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;


@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {
    @Autowired
    private RestaurantService service;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<RestaurantDTO> create(@RequestBody @Valid RestaurantDTO restaurantDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(restaurantDTO));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<RestaurantDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping("/top3")
    public ResponseEntity<List<Restaurant>> top3(@RequestParam CuisineType cuisineType) throws ClassNotFoundException, SQLException {
        return ResponseEntity.ok(service.getTop3ByCuisine(cuisineType));
    }
}
