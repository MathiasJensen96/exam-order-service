package com.example.orderservice.model.entity;

import com.example.orderservice.model.dto.OrderDTO;
import com.example.orderservice.model.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "`order`")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "customerId")
    private int customerId;
    @Column(name = "restaurantId")
    private int restaurantId;
    @Column(name = "createdAt")
    private Timestamp createdAt;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();
    @Column(name = "orderPrice")
    private double orderPrice;
    @Column(name = "deliveryPrice")
    private double deliveryPrice;
    @Column(name = "withDelivery")
    private boolean withDelivery;
    @Column(name = "courierId")
    private Integer courierId;
    @Column(name = "processId")
    private String processId;

    public static Order fromDto(OrderDTO dto) {
        Order order = Order.builder()
                .customerId(dto.getCustomerId())
                .restaurantId(dto.getRestaurantId())
                .status(dto.getStatus())
                .withDelivery(dto.isWithDelivery())
                .items(OrderItem.fromList(dto.getItems()))
                .processId(dto.getProcessId())
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();
        for (OrderItem item : order.getItems()) {
            item.setOrder(order);
        }
        return order;
    }

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }
}