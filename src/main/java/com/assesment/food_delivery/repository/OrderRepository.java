package com.assesment.food_delivery.repository;

import com.assesment.food_delivery.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDateTime;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.status = 'COOKING' AND o.cookingStartedAt <= :threshold")
    List<Order> findCookingDelayed(@Param("threshold") LocalDateTime threshold);

    @Query("SELECT o FROM Order o WHERE o.status = 'OUT_FOR_DELIVERY' AND o.outForDeliveryAt <= :threshold")
    List<Order> findDeliveryDelayed(@Param("threshold") LocalDateTime threshold);

    @Query("SELECT SUM(o.total_price) FROM Order o WHERE o.status = 'DELIVERED'")
    Double getTotalRevenue();

}
