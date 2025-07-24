package com.assesment.food_delivery.repository;

import com.assesment.food_delivery.entity.Dish;
import com.assesment.food_delivery.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
