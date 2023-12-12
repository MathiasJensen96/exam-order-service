package com.example.orderservice.model.dto;

import com.example.orderservice.model.entity.OrderItem;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderItemDTO {
    private int id;
    private int menuItemId;
    private String name;
    private double price;
    private int quantity;
    private int orderId;

    public OrderItemDTO(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public OrderItemDTO(int id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public static List<OrderItemDTO> fromList(List<OrderItem> items) {
        List<OrderItemDTO> orderItems = new ArrayList<>();
        for (OrderItem item : items
        ) {
            orderItems.add(new OrderItemDTO(item.getId(),
                    item.getMenuItemId(),
                    item.getName(), item.getPrice(),
                    item.getQuantity(), item.getOrder().getId()));
        }
        return orderItems;
    }

    public static List<OrderItemDTO> fromNewItems(List<NewOrderItem> items) {
        List<OrderItemDTO> orderItems = new ArrayList<>();
        for (NewOrderItem item : items
        ) {
            OrderItemDTO dto = new OrderItemDTO();
            dto.setMenuItemId(item.getMenuItemId());
            dto.setQuantity(item.getQuantity());
            orderItems.add(dto);
        }
        return orderItems;
    }
}
