package com.assesment.food_delivery.service;

import com.assesment.food_delivery.dto.AgentRequestDto;
import com.assesment.food_delivery.entity.DeliveryAgent;
import com.assesment.food_delivery.repository.DeliveryAgentRepository;
import com.assesment.food_delivery.repository.OrderRepository;
import com.assesment.food_delivery.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.AlreadyBuiltException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class AdminService {
    @Autowired
    private DeliveryAgentRepository deliveryAgentRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;

    public DeliveryAgent create(AgentRequestDto request) {
        try {
            System.out.println("agent create request " + request);

            if (findUserByEmail(request.getEmail()).isPresent()) {
                throw new AlreadyBuiltException("agent already exist please add different email...");
            }

            DeliveryAgent agent = DeliveryAgent.builder()
                    .name(request.getName())
                    .email(request.getEmail())
                    .latitude(request.getLatitude())
                    .longitude(request.getLongitude())
                    .isAvailable(true)
                    .currentAssign(0)
                    .build();

            return deliveryAgentRepository.save(agent);


        } catch (AlreadyBuiltException | IllegalArgumentException ex) {
            throw ex; // will be handled by global exception handler
        } catch (Exception ex) {
            throw new RuntimeException("Signup failed due to an unexpected error.");
        }
    }

    public Optional<DeliveryAgent> findUserByEmail(String email){
        return deliveryAgentRepository.findByEmail(email);
    }

    public Map<String, Object> getAnalytics(){
        Double revenue = orderRepository.getTotalRevenue();
        Double avgDeliveryTime = restaurantRepository.getAvgDeliveryTime();

        Map<String, Object> analytics = new HashMap<>();
        analytics.put("total_revenue",revenue != null ? revenue :0.0);
        analytics.put("avg_delivery_time",avgDeliveryTime != null ? avgDeliveryTime :0.0);
        return analytics;

    }
}
