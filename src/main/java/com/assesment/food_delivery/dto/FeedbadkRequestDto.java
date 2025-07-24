package com.assesment.food_delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbadkRequestDto {
    private Long dish_id;
    private Long restaurant_id;
    private Long customer_id;
    private String feedback;
    private Double rating;
    private String title;
}
