package com.example.orderservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.camunda.bpm.engine.variable.Variables;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaskVariables {
    String workerId;
    Variables variables;
}