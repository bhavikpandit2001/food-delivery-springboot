package com.assesment.food_delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantRequestDto {
    private String restaurant_name;
    private String address;
    private String phone;
    private Integer delivery_time;
    private Double rating;
}
