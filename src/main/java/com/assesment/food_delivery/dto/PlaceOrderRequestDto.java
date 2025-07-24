package com.assesment.food_delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceOrderRequestDto {
    private List<OrderDishRequestDto> items;
    private String address;
    private String PaymentType;
    private String instructions;
    private Long restaurant_id;
    private double latitude;
    private double longitude;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderDishRequestDto{
        private Long dishId;
        private String variant;
        private int quantity;
    }
}
