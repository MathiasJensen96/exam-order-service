package com.example.orderservice.model.entity;

import com.example.orderservice.model.dto.OrderItemDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity
@Table(name = "order_item")
public class OrderItem {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "menuItemId")
    private int menuItemId;
    @Column(name = "name")
    private String name;
    @Column(name = "price")
    private double price;
    @Column(name = "amount")
    private int amount;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    public OrderItem(String name, double price, int amount) {
        this.name = name;
        this.price = price;
        this.amount = amount;
    }
    public OrderItem(String name, double price, int amount, Order order) {
        this.order = order;
        this.name = name;
        this.price = price;
        this.amount = amount;
    }

    public static List<OrderItem> fromList(List<OrderItemDTO> items) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemDTO item : items
        ) {
            OrderItem orderItem = new OrderItem(item.getName(), item.getPrice(), item.getAmount());
            orderItem.setMenuItemId(item.getMenuItemId());
            orderItems.add(orderItem);
        }
        return orderItems;
    }
}