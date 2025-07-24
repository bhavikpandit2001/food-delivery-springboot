package com.assesment.food_delivery.repository;

import com.assesment.food_delivery.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    List<Dish> findByRestaurantId(Long restaurant_id);

    @Query("""
    SELECT d FROM Dish d
    WHERE (:cuisine IS NULL OR LOWER(d.cuisine) LIKE LOWER(CONCAT('%', :cuisine, '%')))
    AND (:rating IS NULL OR d.rating >= :rating)
    """)
    List<Dish> listDishes(
            @Param("cuisine") String cuisine,
            @Param("rating") Double rating,
            @Param("search") String search
    );
}
