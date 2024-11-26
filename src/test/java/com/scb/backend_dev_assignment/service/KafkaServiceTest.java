package com.scb.backend_dev_assignment.service;

import com.scb.backend_dev_assignment.dto.KafkaRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

public class KafkaServiceTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;
    @InjectMocks
    private KafkaService kafkaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        System.setOut(new PrintStream(outContent));
    }

    @Test
    void produce_ShouldSendMessageToKafka() {
        KafkaRequestDto kafkaRequestDto = new KafkaRequestDto();
        kafkaRequestDto.setTopic("my_topic");
        kafkaRequestDto.setMessage("Test message");

        kafkaService.produce(kafkaRequestDto);

        verify(kafkaTemplate).send(eq("my_topic"), eq("Test message"));
    }

    @Test
    void consumeMyTopic_ShouldPrintReceivedMessage() {
        String message = "Test message";

        kafkaService.consumeMyTopic(message);

        assertTrue(outContent.toString().contains("Received Message: /n" + message));
    }
}
