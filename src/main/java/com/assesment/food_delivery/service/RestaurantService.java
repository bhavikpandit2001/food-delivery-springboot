package com.assesment.food_delivery.service;

import com.assesment.food_delivery.dto.ChangeStatusRequestDto;
import com.assesment.food_delivery.dto.ListRequestDto;
import com.assesment.food_delivery.dto.RestaurantRequestDto;
import com.assesment.food_delivery.entity.Restaurant;
import com.assesment.food_delivery.entity.User;
import com.assesment.food_delivery.enums.OnboardingStatus;
import com.assesment.food_delivery.repository.RestaurantRepository;
import com.assesment.food_delivery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private UserRepository userRepository;

    public Restaurant AddRestaurant(RestaurantRequestDto request, String owner_email){
       try{
           User owner = userRepository.findByEmail(owner_email).orElseThrow(() -> new RuntimeException("user not found..."));
           Restaurant restaurant = Restaurant.builder()
                   .restaurant_name(request.getRestaurant_name())
                   .address(request.getAddress())
                   .phone(request.getPhone())
                   .owner(owner)
                   .status(OnboardingStatus.PENDING)
                   .delivery_time(request.getDelivery_time())
                   .rating(0.0) // ✅ this line fixes the issue
                   .rate_count(0)
                   .build();


           System.out.println("restaurant builder "+ restaurant);
           return restaurantRepository.save(restaurant);
       } catch (RuntimeException e) {
           throw e;
       } catch (Exception e) {
           throw new RuntimeException("Failed to add restaurant due to unexpected error.");
       }
    }

    public Restaurant ApproveOrReject(ChangeStatusRequestDto request, Long id){
        try{
            System.out.println("status ===>"+ request);
            System.out.println("id ===>"+ id);
            Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() -> new RuntimeException("Restaurant not found..."));

            if(request.getStatus() == OnboardingStatus.APPROVED){
                restaurant.setStatus(OnboardingStatus.APPROVED);
            }else if(request.getStatus() == OnboardingStatus.REJECTED){
                restaurant.setStatus(OnboardingStatus.REJECTED);
            }

            return  restaurantRepository.save(restaurant);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to add restaurant due to unexpected error.");
        }
    }

    public List<Restaurant> listRestaurants(ListRequestDto request){
        try {
            Integer delivery_time = request != null ? request.getDelivery_time() : null;
            Double rating = request != null ? request.getRating() : null;
            String search = request != null ? request.getSearch() : null;

            return  restaurantRepository.listRestaurants(delivery_time, rating, search);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to list restaurants due to unexpected error.");
        }
    }
}
