package com.assesment.food_delivery.repository;

import com.assesment.food_delivery.entity.Dish;
import com.assesment.food_delivery.entity.Restaurant;
import com.assesment.food_delivery.enums.OnboardingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findByStatus(OnboardingStatus status);

    @Query("""
    SELECT r FROM Restaurant r
    WHERE r.status = 'APPROVED'
    AND (:rating IS NULL OR r.rating >= :rating)
    AND (:delivery_time IS NULL OR r.delivery_time <= :delivery_time)
    AND (:search IS NULL OR LOWER(r.restaurant_name) LIKE LOWER(CONCAT('%', :search, '%')))
""")
    List<Restaurant> listRestaurants(
            @Param("delivery_time") Integer delivery_time,
            @Param("rating") Double rating,
            @Param("search") String search
    );

    @Query("SELECT AVG(r.delivery_time) FROM Restaurant r")
    Double getAvgDeliveryTime();
}
