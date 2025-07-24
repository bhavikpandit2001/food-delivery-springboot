package com.assesment.food_delivery.controller;

import com.assesment.food_delivery.dto.*;
import com.assesment.food_delivery.entity.User;
import com.assesment.food_delivery.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/auth", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
public class AuthController {

    @Autowired
    private AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<?> Login(@ModelAttribute AuthLoginRequestDto request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    // register user with role
    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDto<User>> signup(@ModelAttribute AuthRegisterRequestDto request){
        try{
            System.out.println("signup called");
            System.out.println("auth controller request "+ request);
            User data = authService.signup(request);
            ApiResponseDto<User> response = new ApiResponseDto<>(HttpStatus.OK.value(),"Signup successfully...", data);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponseDto<User> response = new ApiResponseDto<>(HttpStatus.BAD_REQUEST.value(),e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

    }
}
