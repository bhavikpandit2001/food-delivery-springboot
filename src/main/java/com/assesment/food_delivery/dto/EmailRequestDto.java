package com.assesment.food_delivery.dto;

import com.assesment.food_delivery.enums.NotificationTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequestDto {
    private Long orderId;
    private NotificationTypes types;
    private String message;
    private String email;
}