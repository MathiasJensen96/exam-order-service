package com.example.orderservice.service;

import com.example.orderservice.model.entity.CamundaOrderTask;
import com.example.orderservice.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public CamundaOrderTask createTask(CamundaOrderTask task) {
        CamundaOrderTask storedTask = taskRepository.save(task);
        return storedTask;
    }

    public CamundaOrderTask getTaskById(int systemOrderId) {
        CamundaOrderTask storedTask = taskRepository.findById(systemOrderId).orElseThrow();
        return storedTask;
    }
}