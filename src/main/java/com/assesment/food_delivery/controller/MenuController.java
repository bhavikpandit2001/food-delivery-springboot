package com.assesment.food_delivery.controller;

import com.assesment.food_delivery.dto.ApiResponseDto;
import com.assesment.food_delivery.dto.DishRequestDto;
import com.assesment.food_delivery.entity.Dish;
import com.assesment.food_delivery.entity.Feedback;
import com.assesment.food_delivery.entity.User;
import com.assesment.food_delivery.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/restaurant/menu", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
public class MenuController {

    @Autowired
    private MenuService menuService;

    // restaurant add menu items
    @PostMapping(value = "/addItem", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<Dish>> AddDish(@RequestBody DishRequestDto request){
        try{
            Dish data =
                    menuService.addDish(request);
            ApiResponseDto<Dish> response = new ApiResponseDto<>(HttpStatus.OK.value(),"items added successfully...", data);
            return ResponseEntity.ok(response);
        }catch (Exception e){
            ApiResponseDto<Dish> response = new ApiResponseDto<>(HttpStatus.BAD_REQUEST.value(),e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // restaurant update menu items
    @PostMapping("/updateItem/{dishId}")
    public ResponseEntity<ApiResponseDto<Dish>> updateDish(@ModelAttribute DishRequestDto request, @PathVariable Long dishId){
        try{
            Dish data =
                    menuService.updateDish(request, dishId);
            System.out.println(data.getDish_name());
            ApiResponseDto<Dish> response = new ApiResponseDto<>(HttpStatus.OK.value(),"items updated successfully...", data);
            return ResponseEntity.ok(response);
        }catch (Exception e){
            ApiResponseDto<Dish> response = new ApiResponseDto<>(HttpStatus.BAD_REQUEST.value(),e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // restaurant delete menu items
    @DeleteMapping(value = "/deleteItem/{dishId}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<ApiResponseDto<Dish>> deleteDish(@PathVariable Long dishId){
        try{
            menuService.deleteDish(dishId);
            ApiResponseDto<Dish> response = new ApiResponseDto<>(HttpStatus.OK.value(),"items deleted successfully...", null);
            return ResponseEntity.ok(response);
        }catch (Exception e){
            ApiResponseDto<Dish> response = new ApiResponseDto<>(HttpStatus.BAD_REQUEST.value(),e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
