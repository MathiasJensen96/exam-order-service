package com.example.orderservice.listeners;

import com.example.orderservice.model.dto.OrderCancelled;
import com.example.orderservice.model.dto.OrderDTO;
import com.example.orderservice.model.dto.OrderIdDTO;
import com.example.orderservice.model.dto.TaskVariables;
import com.example.orderservice.model.entity.CamundaOrderTask;
import com.example.orderservice.model.enums.OrderStatus;
import com.example.orderservice.service.TaskService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.orderservice.service.OrderService;
import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaListeners {
    @Value("${camunda.server.engine}")
    private String restEngine;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OrderService orderService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private Gson GSON;

    public KafkaListeners(OrderService orderService) {
        this.orderService = orderService;
    }

    public OrderDTO updateOrderStatus(int id, OrderStatus status) {
        OrderDTO result;
        try {
            result = orderService.updateOrderStatus(id, status);
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    @KafkaListener(
            topics = "order_accepted",
            groupId = "order-manager"
    )
    public void orderAccepted(@Payload OrderIdDTO order) {
        updateOrderStatus(order.getSystemOrderId(), OrderStatus.IN_PROGRESS);
    }

    @KafkaListener(
            topics = "order_ready",
            groupId = "order-manager"
    )
    public void orderReady(@Payload OrderIdDTO order) {
        updateOrderStatus(order.getSystemOrderId(), OrderStatus.READY);
    }

    @KafkaListener(
            topics = "order_picked_up",
            groupId = "order-manager"
    )
    public void orderPickedUp(@Payload OrderIdDTO order) {
        updateOrderStatus(order.getSystemOrderId(), OrderStatus.PICKED_UP);
    }

    @KafkaListener(
            topics = "order_delivered",
            groupId = "order-manager")
    public void orderDelivered(@Payload OrderIdDTO order) {
        updateOrderStatus(order.getSystemOrderId(), OrderStatus.COMPLETED);
        CamundaOrderTask task = taskService.getTaskById(order.getSystemOrderId());
        completeCamundaTask(task);
    }

    @KafkaListener(
            topics = "order_cancelled",
            groupId = "order-manager"
    )
    @KafkaHandler
    public void orderCancelled(@Payload OrderCancelled orderCancelled) {

        OrderDTO result = updateOrderStatus(orderCancelled.getSystemOrderId(), OrderStatus.CANCELLED);
        if (orderCancelled.getReason().equals("Unexpected error")) {
            cancelCamundaProcess(result.getProcessId());
        }
    }

    public void cancelCamundaProcess(String instanceId) {
        try {
            String endProcessURL = new StringBuilder(restEngine)
                    .append("process-instance/")
                    .append(instanceId)
                    .toString();
            restTemplate.delete(endProcessURL);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void completeCamundaTask(CamundaOrderTask task) {
        try {
            String url = new StringBuilder(restEngine)
                    .append("external-task/")
                    .append(task.getTaskId())
                    .append("/complete")
                    .toString();

            String requestBody = buildTaskVariables(task.getWorkerId());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            List<MediaType> mediaTypeList = new ArrayList();
            mediaTypeList.add(MediaType.APPLICATION_JSON);
            headers.setAccept(mediaTypeList);
            HttpEntity<String> request =
                    new HttpEntity<>(requestBody, headers);

            restTemplate.postForEntity(url, request, String.class);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private String buildTaskVariables(String workerId) {
        Variables variables = new Variables();
        TaskVariables taskVariables = new TaskVariables(workerId, variables);
        return GSON.toJson(taskVariables, TaskVariables.class);
    }
}