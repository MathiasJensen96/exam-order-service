package com.example.orderservice.service;

import com.example.orderservice.model.entity.Order;
import com.example.orderservice.model.dto.OrderDTO;
import com.example.orderservice.model.enums.OrderStatus;
import com.example.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderDTO saveOrder(OrderDTO dto) {
        Order toSave = Order.fromDto(dto);
        Order order = orderRepository.save(toSave);
        OrderDTO orderDTO = OrderDTO.fromOrder(order);
        return orderDTO;
    }

    public OrderDTO updateOrderStatus(int orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.setStatus(newStatus);
        OrderDTO orderDTO = OrderDTO.fromOrder(order);
        return orderDTO;
    }

    public OrderDTO getOrderById(int orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        return OrderDTO.fromOrder(order);
    }
}