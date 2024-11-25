package com.scb.backend_dev_assignment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {

    private static final String TOPIC = "my_topic";
    private static final String GROUP_ID = "my-group";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void produce(String message) {
        kafkaTemplate.send(TOPIC, message);
    }

    @KafkaListener(topics = TOPIC, groupId = GROUP_ID)
    public void consume(String message) {
        System.out.println("Received Message: " + message);
    }
}

