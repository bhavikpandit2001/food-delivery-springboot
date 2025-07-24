package com.assesment.food_delivery.service;

import com.assesment.food_delivery.dto.FeedbadkRequestDto;
import com.assesment.food_delivery.entity.Dish;
import com.assesment.food_delivery.entity.Feedback;
import com.assesment.food_delivery.entity.Restaurant;
import com.assesment.food_delivery.entity.User;
import com.assesment.food_delivery.repository.DishRepository;
import com.assesment.food_delivery.repository.FeedbackRepository;
import com.assesment.food_delivery.repository.RestaurantRepository;
import com.assesment.food_delivery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private DishRepository dishRepository;

    public void submitFeedback(FeedbadkRequestDto request) {
        try {
            User customer = userRepository.findById(request.getCustomer_id())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found..."));

            Feedback feedback = new Feedback();
            feedback.setFeedback(request.getFeedback());
            feedback.setCustomer(customer);
            feedback.setTitle(request.getTitle());
            feedback.setCreatedAt(LocalDateTime.now());

            if (request.getDish_id() != null) {
                Dish dish = dishRepository.findById(request.getDish_id())
                        .orElseThrow(() -> new RuntimeException("dish not found..."));
                feedback.setDish(dish);
                feedbackRepository.save(feedback);
                updateDishRating(dish.getId(), request.getRating());
            } else if (request.getRestaurant_id() != null) {
                Restaurant restaurant = restaurantRepository.findById(request.getRestaurant_id())
                        .orElseThrow(() -> new RuntimeException("restaurant not found..."));
                feedback.setRestaurant(restaurant);
                feedbackRepository.save(feedback);
                updateRestaurantRating(restaurant.getId(), request.getRating());
            } else {
                throw new IllegalArgumentException("ids must be provided...");
            }

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("feedback submission due to unexpected error.");
        }
    }

    public void updateDishRating(Long dish_id, double rating){
        System.out.println(rating);
        try{
            Dish dish = dishRepository.findById(dish_id)
                    .orElseThrow(() -> new RuntimeException("dish not found..."));
            if(dish.getRating() == null){
               dish.setRating(0.0);
            }
            if(dish.getRate_count() == null){
                dish.setRate_count(0);
            }
            int count = dish.getRate_count() + 1;
            Double rating_sum = dish.getRating() * count;

            System.out.println(rating_sum);
            rating_sum += rating;

            System.out.println(rating);
            Double total = rating_sum / count;

            System.out.println(total);

            dish.setRating(total);
            dish.setRate_count(count);
            dishRepository.save(dish);
        } catch (RuntimeException e) {
            throw new RuntimeException("failed to update rating...");
        }
    }

    public void updateRestaurantRating(Long restaurant_id, double rating){
        try{
            Restaurant restaurant = restaurantRepository.findById(restaurant_id)
                    .orElseThrow(() -> new RuntimeException("restaurant not found..."));
            if(restaurant.getRating() == null){
                restaurant.setRating(0.0);
            }
            if(restaurant.getRate_count() == null){
                restaurant.setRate_count(0);
            }
            int count = restaurant.getRate_count() + 1;
            double rating_sum = restaurant.getRating() * restaurant.getRate_count();
            rating_sum += rating;
            double total = rating_sum / count;

            restaurant.setRating(total);
            restaurant.setRate_count(count);
            restaurantRepository.save(restaurant);
        } catch (RuntimeException e) {
            throw new RuntimeException("failed to update rating...");
        }
    }
}
