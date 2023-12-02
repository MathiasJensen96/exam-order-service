package com.example.orderservice.model.dto;

import com.example.orderservice.model.enums.OrderStatus;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class NewOrder {
    private int id;
    private int restaurantId;
    private int customerId;
    private Timestamp createdAt;
    private OrderStatus status;
    private boolean withDelivery;
    private double totalPrice;
    private List<NewOrderItem> items;

    public NewOrder(OrderDTO order) {
        this.id = order.getId();
        this.restaurantId = order.getRestaurantId();
        this.customerId = order.getCustomerId();
        this.createdAt = order.getCreatedAt();
        this.status = order.getStatus();
        this.withDelivery = order.isWithDelivery();
        this.items = NewOrderItem.fromItemDTOList(order.getItems());
    }
}