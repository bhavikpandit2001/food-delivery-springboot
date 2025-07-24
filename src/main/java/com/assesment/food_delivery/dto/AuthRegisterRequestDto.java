package com.assesment.food_delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRegisterRequestDto {
    private String email;
    private String password;
    private String userRole;
    private String first_name;
    private String last_name;
    private String phone;
    private String username;
}
