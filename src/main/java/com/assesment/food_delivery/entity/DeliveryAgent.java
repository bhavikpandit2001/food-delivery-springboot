package com.assesment.food_delivery.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@Table(name = "delivery-agent")
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryAgent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private double latitude;

    @Column
    private double longitude;

    @Column
    private int currentAssign;

    @Column
    private boolean isAvailable;
}
