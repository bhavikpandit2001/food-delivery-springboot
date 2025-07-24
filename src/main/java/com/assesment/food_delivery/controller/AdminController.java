package com.assesment.food_delivery.controller;

import com.assesment.food_delivery.dto.AgentRequestDto;

import com.assesment.food_delivery.dto.ApiResponseDto;
import com.assesment.food_delivery.dto.ChangeStatusRequestDto;
import com.assesment.food_delivery.entity.DeliveryAgent;
import com.assesment.food_delivery.entity.Restaurant;
import com.assesment.food_delivery.entity.User;
import com.assesment.food_delivery.service.AdminService;
import com.assesment.food_delivery.service.RestaurantService;
import org.aspectj.weaver.loadtime.Agent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/admin", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private RestaurantService restaurantService;


    // status update of restaurant approved, rejected or pending
    @PostMapping("/changeStatus/{id}")
    public ResponseEntity<ApiResponseDto<Restaurant>> ApproveOrReject(@ModelAttribute ChangeStatusRequestDto request, @PathVariable Long id){
        try{
            System.out.println(request);
            Restaurant data =
                    restaurantService.ApproveOrReject(request, id);
            ApiResponseDto<Restaurant> response = new ApiResponseDto<>(HttpStatus.OK.value(),"status changed successfully...", data);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponseDto<Restaurant> response = new ApiResponseDto<>(HttpStatus.BAD_REQUEST.value(),e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // admin can create delivery agent
    @PostMapping("/createAgent")
    public ResponseEntity<ApiResponseDto<DeliveryAgent>> create(@ModelAttribute AgentRequestDto request){
        try{
            DeliveryAgent data = adminService.create(request);
            ApiResponseDto<DeliveryAgent> response = new ApiResponseDto<>(HttpStatus.OK.value(),"agent created successfully...", data);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponseDto<DeliveryAgent> response = new ApiResponseDto<>(HttpStatus.BAD_REQUEST.value(),e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // fetch analytics
    @GetMapping(value = "/getAnalytics", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<ApiResponseDto<Map<String, Object>>> getAnalytics(){
        try{
            Map<String, Object> data = adminService.getAnalytics();
            ApiResponseDto<Map<String, Object>> response = new ApiResponseDto<>(HttpStatus.OK.value(),"analytics get successfully...", data);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponseDto<Map<String, Object>> response = new ApiResponseDto<>(HttpStatus.BAD_REQUEST.value(),e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

    }

}
