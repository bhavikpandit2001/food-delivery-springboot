package com.assesment.food_delivery.dto;

import com.assesment.food_delivery.entity.User;
import com.assesment.food_delivery.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String message;
    private int status;
    private String access_token;
    private User user;
}
