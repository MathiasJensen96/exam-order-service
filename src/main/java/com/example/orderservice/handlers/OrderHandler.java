package com.example.orderservice.handlers;

import com.example.orderservice.model.dto.NewOrderDTO;
import com.example.orderservice.model.dto.OrderDTO;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.service.KafkaService;
import com.example.orderservice.service.OrderService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@ExternalTaskSubscription(topicName = "createOrder")
@Component
public class OrderHandler implements ExternalTaskHandler {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    private KafkaService kafkaService;

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        log.info("Create order topic fired");
        Gson gson = new GsonBuilder().create();
        NewOrderDTO newOrder = gson.fromJson(externalTask.getVariableTyped("order").getValue().toString(), NewOrderDTO.class);
        OrderService orderService = new OrderService(orderRepository);
        OrderDTO orderToCreate = new OrderDTO(newOrder);
        orderToCreate.setProcessId(externalTask.getProcessInstanceId());
        OrderDTO orderCreated = orderService.saveOrder(orderToCreate);
        kafkaService.newOrderPlaced(orderCreated);
        Map<String, Object> allVariables = externalTask.getAllVariables();
        allVariables.put("order", gson.toJson(orderCreated));
        externalTaskService.complete(externalTask, allVariables);
    }
}