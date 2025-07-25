package com.assesment.food_delivery.service;

import com.assesment.food_delivery.dto.AgentAssignDto;
import com.assesment.food_delivery.dto.PlaceOrderRequestDto;
import com.assesment.food_delivery.entity.*;
import com.assesment.food_delivery.enums.NotificationTypes;
import com.assesment.food_delivery.enums.OrderStatus;
import com.assesment.food_delivery.repository.DishRepository;
import com.assesment.food_delivery.repository.OrderRepository;
import com.assesment.food_delivery.repository.RestaurantRepository;
import com.assesment.food_delivery.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private AgentService assignmentService;

    @Autowired
    private WebhookService webhookService;

    @Autowired
    private NotificationService notificationService;

    @Transactional
    public Order PlaceOrder(PlaceOrderRequestDto request){
        try{
            User user =userRepository.findById(request.getUser_id()).orElseThrow(() -> new RuntimeException("user not found..."));

            restaurantRepository.findById(request.getRestaurant_id())
                    .orElseThrow(()-> new RuntimeException("Invalid restaurant id..."));

            // Extract dish IDs from request
            List<Long> dishIds = request.getItems().stream()
                    .map(PlaceOrderRequestDto.OrderDishRequestDto::getDishId)
                    .collect(Collectors.toList());

            List<Dish> dishes = dishRepository.findAllById(dishIds);

            if (dishes.size() != dishIds.size()) {
                throw new IllegalArgumentException("Some dish IDs are invalid...");
            }

            for(Dish dish: dishes){
                if (!Objects.equals(dish.getRestaurant().getId(), request.getRestaurant_id())) {
                    throw new IllegalArgumentException("Please select dishes from same restaurant...");
                }
            }

            Order order = Order.builder()
                    .user(user)
                    .address(request.getAddress())
                    .payment_type(request.getPaymentType())
                    .instruction(request.getInstructions())
                    .status(OrderStatus.PLACED)
                    .delivery_lat(request.getLatitude())
                    .delivery_lng(request.getLongitude())
                    .build();

            List<OrderItem> items = new ArrayList<>();

            double total_price = 0.0;

            for(PlaceOrderRequestDto.OrderDishRequestDto req : request.getItems()){
                System.out.println(req);

                // find dishes
                Dish dish = dishes.stream()
                        .filter(d -> Objects.equals(d.getId(), req.getDishId()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("dish not found..."));

                // find variants
//            Variant matchedVariant = dish.getVariants().stream()
//                    .filter(v -> v.getVariant().equalsIgnoreCase(req.getVariant()))
//                    .findFirst()
//                    .orElseThrow(() -> new IllegalArgumentException("variant not found..." + req.getVariant()));

                Variant matchedVariant = dish.getVariants().stream()
                        .filter(v -> v.getVariant().trim().equalsIgnoreCase(req.getVariant().trim()))
                        .findFirst()
                        .orElseThrow(() -> {
                            System.out.println("Dish ID: " + dish.getId());
                            System.out.println("Looking for variant: [" + req.getVariant().trim() + "]");
                            System.out.println("Available variants:");
                            dish.getVariants().forEach(v -> System.out.println("[" + v.getVariant() + "]"));
                            return new IllegalArgumentException("Variant not found for dishId=" + dish.getId() + " variant=" + req.getVariant());
                        });

                //calculate price according to variant or quantity
                double ItemPrice = matchedVariant.getPrice() * req.getQuantity();

                //calculate total price
                total_price += ItemPrice;

                //create new order
                OrderItem orderItem = OrderItem.builder()
                        .dish(dish)
                        .variant(matchedVariant.getVariant())
                        .quantity(req.getQuantity())
                        .price(ItemPrice)
                        .order(order)
                        .build();

                items.add(orderItem);
            }

            order.setItems(items);
            order.setTotal_price(total_price);

            return  orderRepository.save(order);

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to place order due to unexpected error.");
        }
    }


    public void updateStatusAndAssignAgent(AgentAssignDto request){
        try{
            //find order
            Order order = orderRepository.findById(request.getOrderId()).orElseThrow(() -> new RuntimeException("order not found..."));

            OrderStatus newStatus = OrderStatus.valueOf(request.getStatus());
            //set status of order
            order.setStatus(newStatus);

            LocalDateTime now = LocalDateTime.now();

            long order_id = order.getId();
            long user_id = order.getUser().getId();
            String message = "";
            NotificationTypes type = null;

            switch (newStatus){
                case OrderStatus.CONFIRMED:
                    message = "Confirmed and Cooking. Get your appetite ready!";
                    type = NotificationTypes.ORDER_CONFIRMED;
                    notificationService.sendNotification(order_id, message, type, user_id);
                case OrderStatus.COOKING:
                    if(order.getCookingStartedAt() == null){
                        order.setCookingStartedAt(now);
                    }
                    break;
                case OrderStatus.OUT_FOR_DELIVERY:
                    if(order.getOutForDeliveryAt() == null){
                        order.setOutForDeliveryAt(now);
                        message = "our riders out with your delicious mission...";
                        type = NotificationTypes.OUT_FOR_DELIVERY;
                        notificationService.sendNotification(order_id, message, type, user_id);
                    }
                    assignmentService.assignDeliveryAgent(order);
                    break;

                case OrderStatus.DELIVERED:
                    if(order.getDeliveredAt() == null){
                        order.setDeliveredAt(now);
                        message = "Mission Delicious: Accomplished! thank you for choosing us...";
                        type = NotificationTypes.DELIVERY_ARRIVED;
                        notificationService.sendNotification(order_id, message, type, user_id);
                    }
                        assignmentService.removeAssignment(order);
                     break;

                default:
                    break;
            }

            //update order in db
            System.out.println(order.getDeliveredAt());
            orderRepository.save(order);

            //webhook called
            webhookService.sendOrderStatusWebhook(order);
        }catch (RuntimeException e){
            throw e;
        }catch (Exception e){
            throw new RuntimeException("Failed to list dish due to unexpected error.");
        }
    }
}
