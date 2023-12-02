package com.example.orderservice.service;

import com.example.orderservice.model.dto.NewOrder;
import com.example.orderservice.model.dto.OrderDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaService {
    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void newOrderPlaced(OrderDTO order) {
        NewOrder newOrder = new NewOrder(order);
        kafkaTemplate.send("new_order_placed", newOrder);
    }
}