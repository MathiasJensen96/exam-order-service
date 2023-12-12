package com.example.orderservice.repository;

import com.example.orderservice.model.entity.Order;
import com.example.orderservice.model.entity.OrderItem;
import com.example.orderservice.model.enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;

    private Order order1;
    private Order order2;
    private OrderItem orderItem1;
    private OrderItem orderItem2;
    private OrderItem orderItem3;

    @BeforeEach
    void setUp() {
        order1 = Order.builder()
                .customerId(1)
                .restaurantId(1)
                .createdAt(Timestamp.valueOf("2023-01-01 00:00:00"))
                .items(new ArrayList<>())
                .status(OrderStatus.IN_PROGRESS)
                .withDelivery(true)
                .courierId(1)
                .build();

        order2 = Order.builder()
                .customerId(2)
                .restaurantId(2)
                .createdAt(Timestamp.valueOf("2023-01-01 00:00:00"))
                .items(new ArrayList<>())
                .status(OrderStatus.IN_PROGRESS)
                .withDelivery(false)
                .build();

        orderItem1 = OrderItem.builder()
                .menuItemId(1)
                .name("name1")
                .price(1)
                .quantity(1)
                .build();

        orderItem2 = OrderItem.builder()
                .menuItemId(2)
                .name("name2")
                .price(2)
                .quantity(2)
                .build();

        orderItem3 = OrderItem.builder()
                .menuItemId(3)
                .name("name3")
                .price(3)
                .quantity(3)
                .build();

        order1.addItem(orderItem1);
        order1.addItem(orderItem2);
        order2.addItem(orderItem3);

        orderRepository.save(order1);
        orderRepository.save(order2);
    }

    @Test
    void init() {
        assertNotNull(orderRepository);
    }

    @Test
    void givenOrdersInDatabase_whenFindAll_returnAllOrders() {
        // Act
        List<Order> orders = orderRepository.findAll();

        // Assert
        assertNotNull(orders);
        assertEquals(2, orders.size());
    }

    @Test
    void givenOrdersInDatabase_whenFindById_returnOrder() {
        // Act
        Order order = orderRepository.findById(order1.getId()).orElse(null);

        // Assert
        assertNotNull(order);
        assertEquals(order1.getId(), order.getId());
        assertEquals(order1.getItems().size(), order.getItems().size());
    }

    @Test
    void givenExistingOrder_whenSave_thenOrderIsUpdated() {
        // Arrange
        order1.setStatus(OrderStatus.CANCELLED);

        // Act
        Order order = orderRepository.save(order1);

        // Assert
        assertNotNull(order);
        assertEquals(order1.getId(), order.getId());
        assertEquals(order1.getStatus(), order.getStatus());
    }

    @Test
    void givenExistingOrders_whenDelete_thenOrderIsDeleted() {
        // Arrange
        assertEquals(2, orderRepository.count());

        // Act
        orderRepository.delete(order1);

        // Assert
        assertFalse(orderRepository.findById(order1.getId()).isPresent());
        assertEquals(1, orderRepository.count());
    }

    @Test
    void givenExistingOrders_whenDeleteAll_thenAllOrdersAreDeleted() {
        // Arrange
        assertEquals(2, orderRepository.count());

        // Act
        orderRepository.deleteAll();

        // Assert
        assertEquals(0, orderRepository.count());
    }

    @Test
    void givenExistingOrdersAndNewOrder_whenSave_thenOrderIsSaved() {
        // Arrange
        assertEquals(2, orderRepository.count());

        Order order = Order.builder()
                .customerId(3)
                .restaurantId(3)
                .createdAt(Timestamp.from(Instant.now()))
                .items(new ArrayList<>())
                .status(OrderStatus.IN_PROGRESS)
                .withDelivery(true)
                .courierId(3)
                .build();

        // Act
        Order savedOrder = orderRepository.save(order);

        // Assert
        assertNotNull(savedOrder);
        assertNotNull(savedOrder.getId());
        assertEquals(3, orderRepository.count());
        assertEquals(order.getCustomerId(), savedOrder.getCustomerId());
        assertEquals(order.getRestaurantId(), savedOrder.getRestaurantId());
        assertEquals(order.getCreatedAt(), savedOrder.getCreatedAt());
        assertEquals(order.getItems().size(), savedOrder.getItems().size());
        assertEquals(order.getStatus(), savedOrder.getStatus());
        assertEquals(order.isWithDelivery(), savedOrder.isWithDelivery());
        assertEquals(order.getCourierId(), savedOrder.getCourierId());
    }
}