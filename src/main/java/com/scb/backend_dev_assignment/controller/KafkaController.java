package com.scb.backend_dev_assignment.controller;

import com.scb.backend_dev_assignment.service.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kafka")
public class KafkaController {

    @Autowired
    private KafkaService kafkaService;

    @PostMapping("/publish")
    public String publish(@RequestBody String message) {
        kafkaService.produce(message);
        return "Message sent to Kafka Topic";
    }
}

