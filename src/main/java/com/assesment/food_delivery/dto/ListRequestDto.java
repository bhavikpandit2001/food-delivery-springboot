package com.assesment.food_delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListRequestDto {
    private String search;
    private Double rating;
    private Integer delivery_time;
    private String cuisine;
}
