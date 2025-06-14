package com.itbulls.springdemo.resilience4j.circuitbreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.event.*;
import jakarta.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;


@Configuration
public class CircuitBreakerEventLogger {

    private final CircuitBreakerRegistry circuitBreakerRegistry;

    public CircuitBreakerEventLogger(CircuitBreakerRegistry circuitBreakerRegistry) {
        this.circuitBreakerRegistry = circuitBreakerRegistry;
    }

    @PostConstruct
    public void registerCircuitBreakerListener() {
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("demoService");

        circuitBreaker.getEventPublisher()
            .onStateTransition(this::logStateTransition)
            .onCallNotPermitted(this::logCallNotPermitted)
            .onError(this::logError)
            .onSuccess(this::logSuccess);
    }

    private void logStateTransition(CircuitBreakerOnStateTransitionEvent event) {
        System.out.println("[CircuitBreaker] State transition: " +
                event.getStateTransition());
    }

    private void logCallNotPermitted(CircuitBreakerOnCallNotPermittedEvent event) {
        System.out.println("[CircuitBreaker] Call not permitted - circuit is open.");
    }

    private void logError(CircuitBreakerOnErrorEvent event) {
        System.out.println("[CircuitBreaker] Error occurred: " +
                event.getThrowable().getMessage());
    }

    private void logSuccess(CircuitBreakerOnSuccessEvent event) {
        System.out.println("[CircuitBreaker] Call succeeded.");
    }
}
