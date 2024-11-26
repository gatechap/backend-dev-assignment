package com.scb.backend_dev_assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KafkaRequestDto {
    private String topic;
    private String message;
}
