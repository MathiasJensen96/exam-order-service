package com.example.orderservice.kafka;

import com.example.orderservice.config.KafkaProducerConfig;
import com.example.orderservice.listeners.KafkaListeners;
import com.example.orderservice.service.KafkaService;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.service.TaskService;
import com.google.gson.Gson;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.web.client.RestTemplate;

//@Disabled
@SpringBootTest(classes = {KafkaProducerConfig.class, KafkaListeners.class, KafkaService.class, RestTemplate.class, Gson.class})
@EmbeddedKafka(partitions = 1)
public class KafkaTest {
    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private KafkaListeners kafkaListeners;

    @Autowired
    private KafkaService kafkaService;

    @MockBean
    private OrderService orderService;

    @MockBean
    private TaskService taskService;

    @Test
    void testKafka() {

    }
}
