package com.assesment.food_delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishRequestDto {
    private String dish_name;
    private String description;
    private boolean available;
    private Long restaurant_id;
    private String cuisine;
    private Double rating;
    private List<VariantDto> variants;

    @Data
    public static class VariantDto{
        private String variant;
        private Double price;
    }
}
