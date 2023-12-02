package com.example.orderservice.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "camundaOrderTask")
public class CamundaOrderTask {
    private static final long serialVersionUID = 1L;

    @Id
    private int systemOrderId;
    private String processId;
    private String taskDefinitionKey;
    private String taskId;
    private String workerId;
}