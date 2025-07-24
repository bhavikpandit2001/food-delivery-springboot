package com.assesment.food_delivery.controller;

import com.assesment.food_delivery.dto.ApiResponseDto;
import com.assesment.food_delivery.dto.FeedbadkRequestDto;
import com.assesment.food_delivery.dto.ListRequestDto;
import com.assesment.food_delivery.dto.PlaceOrderRequestDto;
import com.assesment.food_delivery.entity.Dish;
import com.assesment.food_delivery.entity.Feedback;
import com.assesment.food_delivery.entity.Order;
import com.assesment.food_delivery.entity.Restaurant;
import com.assesment.food_delivery.service.FeedbackService;
import com.assesment.food_delivery.service.MenuService;
import com.assesment.food_delivery.service.OrderService;
import com.assesment.food_delivery.service.RestaurantService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/customer", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
public class CustomerController {
    @Autowired
    private MenuService menuService;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private FeedbackService feedbackService;

    // menu items listing
    @PostMapping("/listItems")
    public ResponseEntity<ApiResponseDto< List<Dish>>> listDishes(@ModelAttribute ListRequestDto request){
        try{
            List<Dish> dishes = menuService.listDishes(request);
            ApiResponseDto<List<Dish>> response = new ApiResponseDto<>(HttpStatus.OK.value(),"items get successfully...", dishes);
            return ResponseEntity.ok(response);
        }catch (Exception e){
            ApiResponseDto<List<Dish>> response = new ApiResponseDto<>(HttpStatus.BAD_REQUEST.value(),e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // restaurant listing
    @PostMapping(value = "/listRestaurants" ,consumes = MediaType.ALL_VALUE)
    public ResponseEntity<ApiResponseDto<List<Restaurant>>> listRestaurants(@ModelAttribute ListRequestDto request){
        try{
            List<Restaurant> restaurants = restaurantService.listRestaurants(request);
            ApiResponseDto<List<Restaurant>> response = new ApiResponseDto<>(HttpStatus.OK.value(),"restaurants get successfully...", restaurants);
            return ResponseEntity.ok(response);
        }catch (Exception e){
            ApiResponseDto<List<Restaurant>> response = new ApiResponseDto<>(HttpStatus.BAD_REQUEST.value(),e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // place order
    @PostMapping(value = "/placeOrder", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<Order>> placeOrder(@RequestBody PlaceOrderRequestDto request){
        try{
            Order order = orderService.PlaceOrder(request);
            ApiResponseDto<Order> response = new ApiResponseDto<>(HttpStatus.OK.value(),"order place successfully...", order);
            return ResponseEntity.ok(response);
        }catch (Exception e){
            ApiResponseDto<Order> response = new ApiResponseDto<>(HttpStatus.BAD_REQUEST.value(),e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }


    // submit feedback of restaurant and items
    @PostMapping("/submitFeedback")
    public ResponseEntity<ApiResponseDto<Feedback>> submitFeedback(@ModelAttribute FeedbadkRequestDto request){
        try{
            feedbackService.submitFeedback(request);
            ApiResponseDto<Feedback> response = new ApiResponseDto<>(HttpStatus.OK.value(),"feedback submitted successfully...", null);
            return ResponseEntity.ok(response);
        }catch (Exception e){
            ApiResponseDto<Feedback> response = new ApiResponseDto<>(HttpStatus.BAD_REQUEST.value(),e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    //test
    @GetMapping("/world")
    public String World(){
        return " hello world";
    }
}