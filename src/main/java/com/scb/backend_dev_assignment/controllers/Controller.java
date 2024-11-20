package com.scb.backend_dev_assignment.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @GetMapping("/")
    public String getMessage() {
        return "Hello Backend Team";
    }
}
