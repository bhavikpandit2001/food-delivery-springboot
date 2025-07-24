package com.assesment.food_delivery.controller;

import com.assesment.food_delivery.dto.ApiResponseDto;
import com.assesment.food_delivery.entity.Dish;
import com.assesment.food_delivery.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import com.assesment.food_delivery.dto.AgentAssignDto;
import com.assesment.food_delivery.dto.RestaurantRequestDto;
import com.assesment.food_delivery.entity.Restaurant;
import com.assesment.food_delivery.service.OrderService;
import com.assesment.food_delivery.service.RestaurantService;

import java.util.Map;

@RestController
@RequestMapping(value = "/restaurant", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private OrderService orderService;


    // add restaurant
    @PostMapping("/add")
    public ResponseEntity<ApiResponseDto<Restaurant>> registerRestaurant(@ModelAttribute RestaurantRequestDto request, @AuthenticationPrincipal UserDetails userDetails){
        try{
            Restaurant data =
                    restaurantService.AddRestaurant(request, userDetails.getUsername());
            ApiResponseDto<Restaurant> response = new ApiResponseDto<>(HttpStatus.OK.value(),"restaurant added successfully...", data);
            return ResponseEntity.ok(response);
        }catch (Exception e){
            ApiResponseDto<Restaurant> response = new ApiResponseDto<>(HttpStatus.BAD_REQUEST.value(),e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

    }

    // update order status and assign delivery agent
    @PostMapping("/updateAndAssign")
    public ResponseEntity<ApiResponseDto<Order>> updateAndAssignAgent(@ModelAttribute AgentAssignDto request){
        try{
            orderService.updateStatusAndAssignAgent(request);
            ApiResponseDto<Order> response = new ApiResponseDto<>(HttpStatus.OK.value(),"order status updated successfully...", null);
            return ResponseEntity.ok(response);
        }catch (Exception e){
            ApiResponseDto<Order> response = new ApiResponseDto<>(HttpStatus.BAD_REQUEST.value(),e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
