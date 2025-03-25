package com.restaurant.restaurant.repository;

import com.restaurant.restaurant.dto.enums.CuisineType;
import com.restaurant.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Query("""
            SELECT r FROM Restaurant r
            JOIN Review rev ON rev.restaurant.id = r.id
            WHERE rev.status = 'APPROVED' AND r.cuisineType = :cuisineType
            GROUP BY r.id
            ORDER BY AVG(rev.rating) DESC
            """)
    List<Restaurant> getTop3RestaurantsByCuisine(@Param("cuisineType") CuisineType cuisineType);


}

