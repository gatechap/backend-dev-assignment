package com.scb.backend_dev_assignment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scb.backend_dev_assignment.dto.KafkaRequestDto;
import com.scb.backend_dev_assignment.service.KafkaService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(KafkaController.class)
class KafkaControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KafkaService kafkaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void publish_ShouldReturnSuccessResponse() throws Exception {
        KafkaRequestDto kafkaRequestDto = new KafkaRequestDto();
        kafkaRequestDto.setTopic("test-topic");
        kafkaRequestDto.setMessage("Test message");

        mockMvc.perform(post("/api/kafka/publish")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(kafkaRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("success"));

        Mockito.verify(kafkaService, Mockito.times(1)).produce(any(KafkaRequestDto.class));
    }
}