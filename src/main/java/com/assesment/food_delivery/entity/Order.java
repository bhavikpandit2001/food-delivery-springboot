package com.assesment.food_delivery.entity;

import com.assesment.food_delivery.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.List;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String payment_type;

    @Column(nullable = false)
    private String instruction;

    @Column(nullable = false)
    private double total_price;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrderItem> items;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @Column
    private LocalDateTime cookingStartedAt;

    @Column
    private LocalDateTime outForDeliveryAt;

    @Column
    private LocalDateTime deliveredAt;

    @Column
    private double delivery_lat;

    @Column
    private double delivery_lng;

    @ManyToOne
    private DeliveryAgent assigned_agent;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
