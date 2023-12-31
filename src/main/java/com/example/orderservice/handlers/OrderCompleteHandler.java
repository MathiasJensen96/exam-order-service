package com.example.orderservice.handlers;

import com.example.orderservice.model.entity.CamundaOrderTask;
import com.example.orderservice.service.TaskService;
import com.example.orderservice.listeners.KafkaListeners;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@ExternalTaskSubscription(topicName = "completeOrder")
@Component
public class OrderCompleteHandler implements ExternalTaskHandler {
    @Autowired
    TaskService taskService;

    @Autowired
    KafkaListeners kafkaListener;

    @Autowired
    Gson gson;

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        String processId = externalTask.getProcessInstanceId();
        String taskDefinitionKey = externalTask.getActivityId();
        String taskId = externalTask.getId();
        String workerId = externalTask.getWorkerId();

        int systemOrderId = gson.fromJson(externalTask.getVariableTyped("systemOrderId").getValue().toString(), Integer.class);
        CamundaOrderTask task = new CamundaOrderTask(systemOrderId, processId, taskDefinitionKey, taskId, workerId);
        taskService.createTask(task);
    }
}