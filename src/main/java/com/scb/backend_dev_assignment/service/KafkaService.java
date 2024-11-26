package com.scb.backend_dev_assignment.service;

import com.scb.backend_dev_assignment.dto.KafkaRequestDto;
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

    public void produce(KafkaRequestDto kafkaRequestDto) {
        kafkaTemplate.send(kafkaRequestDto.getTopic(), kafkaRequestDto.getMessage());
    }

    @KafkaListener(topics = TOPIC, groupId = GROUP_ID)
    public void consumeMyTopic(String message) {
        System.out.println("Received Message: /n" + message);
    }
}

