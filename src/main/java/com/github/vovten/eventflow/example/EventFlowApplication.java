package com.github.vovten.eventflow.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Event Flow Spring example.
 * This Spring Boot application demonstrates the event-flow library integration
 * with Spring ecosystem for handling internal, external, and broadcast events.
 */
@SpringBootApplication
public class EventFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventFlowApplication.class, args);
    }
}
