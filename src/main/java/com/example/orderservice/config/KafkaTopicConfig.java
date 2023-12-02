package com.example.orderservice.config;

import com.example.orderservice.model.enums.Topic;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic NewOrderPlaced() {
        return TopicBuilder.name(Topic.new_order_placed.toString()).build();
    }

    @Bean
    public NewTopic OrderAccepted() {
        return TopicBuilder.name(Topic.order_accepted.toString()).build();
    }

    @Bean
    public NewTopic OrderReady() {
        return TopicBuilder.name(Topic.order_ready.toString()).build();
    }

    @Bean
    public NewTopic OrderCancelled() {
        return TopicBuilder.name(Topic.order_cancelled.toString()).build();
    }

    @Bean
    public NewTopic OrderPickedUp() {
        return TopicBuilder.name(Topic.order_picked_up.toString()).build();
    }

    @Bean
    public NewTopic OrderDelivered() {
        return TopicBuilder.name(Topic.order_delivered.toString()).build();
    }

    @Bean
    public NewTopic OrderClaimed() {
        return TopicBuilder.name(Topic.order_claimed.toString()).build();
    }
}