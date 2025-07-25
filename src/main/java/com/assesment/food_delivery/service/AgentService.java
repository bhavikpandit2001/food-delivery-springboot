package com.assesment.food_delivery.service;

import com.assesment.food_delivery.entity.DeliveryAgent;
import com.assesment.food_delivery.entity.Order;
import com.assesment.food_delivery.enums.OrderStatus;
import com.assesment.food_delivery.repository.DeliveryAgentRepository;
import com.assesment.food_delivery.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class AgentService {
    @Autowired
    private DeliveryAgentRepository deliveryAgentRepository;

    @Autowired
    private OrderRepository orderRepository;

    public DeliveryAgent assignDeliveryAgent(Order order){
       try{
           //fetch available agent
           List<DeliveryAgent> agents = deliveryAgentRepository.findByIsAvailableTrue();

           if(agents.isEmpty()){
               throw new RuntimeException("no available agents...");
           }

           //find the closest agent + the least busy
           DeliveryAgent selectedAgent = agents.stream()
                   .min(Comparator.comparingDouble(agent ->
                           calculateDistance(agent.getLatitude(), agent.getLongitude(),order.getDelivery_lat(), order.getDelivery_lng())
                    + agent.getCurrentAssign() * 2
                   )).orElseThrow(() -> new RuntimeException("no available agent..."));

           //assign order to agent
           selectedAgent.setCurrentAssign(selectedAgent.getCurrentAssign() + 1);
           selectedAgent.setIsAvailable(false);
           order.setAssigned_agent(selectedAgent);

           //save updates
           deliveryAgentRepository.save(selectedAgent);
           orderRepository.save(order);

           System.out.println("order "+ order.getId() + " assigned to delivery agent " + selectedAgent.getId());

           return selectedAgent;
       } catch (RuntimeException e){
           throw e; // Will be handled by global exception handler or controller
       } catch (Exception e) {
           throw new RuntimeException("Failed to assign delivery agent due to unexpected error.");
       }
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2 ){
        final int R = 1000;
        double DLat = Math.toRadians(lat2 - lat1);
        double DLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(DLat / 2) * Math.sin(DLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(DLon / 2) * Math.sin(DLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    public void removeAssignment(Order order){
        try{
            DeliveryAgent agent = deliveryAgentRepository.findById(order.getAssigned_agent().getId()).orElseThrow(() -> new RuntimeException("agent not found..."));

            if(order.getStatus() == OrderStatus.DELIVERED){
                agent.setIsAvailable(true);
                agent.setCurrentAssign(0);
                deliveryAgentRepository.save(agent);
            }
        } catch (RuntimeException e){
            throw e; // Will be handled by global exception handler or controller
        } catch (Exception e) {
            throw new RuntimeException("Failed to free delivery agent due to unexpected error.");
        }
    }
}
