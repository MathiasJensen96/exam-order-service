package com.example.orderservice.service;

import com.example.orderservice.model.dto.NewOrderDTO;
import com.example.orderservice.model.dto.OrderDTO;
import com.example.orderservice.model.entity.Order;
import com.example.orderservice.model.enums.OrderStatus;
import com.example.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = OrderService.class)
class OrderServiceTest {
    @Autowired
    OrderService orderService;

    @MockBean
    OrderRepository orderRepository;

    Order order;

    @BeforeEach
    void setUp() {
        order = new Order(
                1,
                1,
                1,
                Timestamp.from(Instant.now()),
                OrderStatus.IN_PROGRESS,
                Collections.emptyList(),
                0,
                0,
                false,
                null,
                "1"
        );
    }

    @Test
    void saveOrder() {
        // Arrange
        NewOrderDTO newOrderDTO = new NewOrderDTO(
                1,
                1,
                new Date(),
                false,
                Collections.emptyList()
        );
        OrderDTO orderDTO = new OrderDTO(newOrderDTO);
        Order order = Order.fromDto(orderDTO);

        // mock the static method Order.fromDto, so that it returns the order object
        // then we can use that exact object to mock the save method
        // this would be easier with an integration test and real database instead of mocking
        try (MockedStatic<Order> orderFromDto = mockStatic(Order.class)) {
            orderFromDto.when(() -> Order.fromDto(orderDTO)).thenReturn(order);
            order.setId(1); // this probably still applies to the object before insertion which isn't exactly correct.
            when(orderRepository.save(order)).thenReturn(order);

            // Act
            OrderDTO savedOrderDTO = orderService.saveOrder(orderDTO);

            // Assert
            assertNotNull(savedOrderDTO);
            assertEquals(order.getId(), savedOrderDTO.getId());
            assertEquals(order.getStatus(), savedOrderDTO.getStatus());
        }
    }

    @Test
    void givenCorrectIdAndNewStatus_whenUpdate_thenReturnOrder() {
        // Arrange
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        // Act
        OrderDTO orderDTO = orderService.updateOrderStatus(order.getId(), OrderStatus.CANCELLED);

        // Assert
        assertNotNull(orderDTO);
        assertEquals(order.getId(), orderDTO.getId());
        assertEquals(OrderStatus.CANCELLED, orderDTO.getStatus());
    }

    @Test
    void givenWrongId_whenUpdate_thenThrowException() {
        // Arrange
        when(orderRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> orderService.updateOrderStatus(1, OrderStatus.CANCELLED));
    }

    @Test
    void givenWrongId_whenGetOrderById_thenThrowException() {
        // Arrange
        when(orderRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> orderService.getOrderById(1));
    }

    @Test
    void givenCorrectId_whenGetOrderById_thenReturnOrder() {
        // Arrange
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        // Act
        OrderDTO orderDTO = orderService.getOrderById(1);

        // Assert
        assertNotNull(orderDTO);
        assertEquals(order.getId(), orderDTO.getId());
        assertEquals(order.getStatus(), orderDTO.getStatus());
    }
}