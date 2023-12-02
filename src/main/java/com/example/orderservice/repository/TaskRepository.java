package com.example.orderservice.repository;

import com.example.orderservice.model.entity.CamundaOrderTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<CamundaOrderTask, Integer> {
}