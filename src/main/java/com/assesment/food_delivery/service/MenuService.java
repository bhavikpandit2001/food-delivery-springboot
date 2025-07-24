package com.assesment.food_delivery.service;

import com.assesment.food_delivery.dto.DishRequestDto;
import com.assesment.food_delivery.dto.ListRequestDto;
import com.assesment.food_delivery.entity.Dish;
import com.assesment.food_delivery.entity.Restaurant;
import com.assesment.food_delivery.entity.Variant;
import com.assesment.food_delivery.enums.OnboardingStatus;
import com.assesment.food_delivery.repository.DishRepository;
import com.assesment.food_delivery.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {
    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    public Dish addDish(DishRequestDto request) {
        try {
            Restaurant restaurant = restaurantRepository.findById(request.getRestaurant_id())
                    .orElseThrow(() -> new RuntimeException("Restaurant not found..."));

            if(restaurant.getStatus() == OnboardingStatus.PENDING ||  restaurant.getStatus() == OnboardingStatus.REJECTED){
                throw new RuntimeException("please approve restaurant then add menu for this restaurant...");
            }
            Dish dish = Dish.builder()
                    .dish_name(request.getDish_name())
                    .description(request.getDescription())
                    .cuisine(request.getCuisine())
                    .rating(request.getRating())
                    .rate_count(0)
                    .restaurant(restaurant)
                    .available(request.isAvailable())
                    .build();

            // Set dish reference inside each variant
            List<Variant> variantEntities = request.getVariants().stream()
                    .map(dto -> Variant.builder()
                            .variant(dto.getVariant())
                            .price(dto.getPrice())
                            .dish(dish) // Set the back-reference
                            .build())
                    .toList();

            dish.setVariants(variantEntities); // Assign list to dish

            return dishRepository.save(dish);

        } catch (RuntimeException ex) {
            throw ex; // Will be handled by global exception handler or controller
        } catch (Exception ex) {
            throw new RuntimeException("Failed to add dish due to unexpected error.");
        }
    }


    public Dish updateDish(DishRequestDto request, Long dishId){
        try {
            System.out.println("id -===> "+ dishId);
            Dish dish = dishRepository.findById(dishId)
                    .orElseThrow(() -> new RuntimeException("Item not found..."));
            System.out.println("dish ==>"+ dish.getId()+ "name ==> "+ dish.getDish_name());

            //update details of dish not variants
            dish.setDish_name(request.getDish_name());
            dish.setDescription(request.getDescription());
            dish.setAvailable(request.isAvailable());

            return dishRepository.save(dish);

        } catch (RuntimeException e){
            throw e; // Will be handled by global exception handler or controller
        } catch (Exception e) {
            throw new RuntimeException("Failed to update dish due to unexpected error.");
        }
    }

    public void deleteDish(Long dishId){
        try {
            System.out.println("id -===> "+ dishId);
            Dish dish = dishRepository.findById(dishId)
                    .orElseThrow(() -> new RuntimeException("Item not found..."));
            dishRepository.delete(dish);

        } catch (RuntimeException e){
            throw e; // Will be handled by global exception handler or controller
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete dish due to unexpected error.");
        }

    }

    public List<Dish> listDishes(ListRequestDto request){
        try {
            String cuisine = request != null ? request.getCuisine() : null;
            Double rating = request != null ? request.getRating() : null;
            String search = request != null ? request.getSearch() : null;

            return  dishRepository.listDishes(cuisine, rating, search);

        } catch (RuntimeException e){
            throw e; // Will be handled by global exception handler or controller
        } catch (Exception e) {
            throw new RuntimeException("Failed to list dish due to unexpected error.");
        }
    }
}
