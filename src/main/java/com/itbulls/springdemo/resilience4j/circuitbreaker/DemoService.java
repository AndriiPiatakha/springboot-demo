package com.itbulls.springdemo.resilience4j.circuitbreaker;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

@Service
public class DemoService {

    private final UnstableService unstableService;

    public DemoService(UnstableService unstableService) {
        this.unstableService = unstableService;
    }

    @CircuitBreaker(name = "demoService", fallbackMethod = "fallback")
    public String callServiceWithCircuitBreaker() {
        return unstableService.callExternalService();
    }

    public String fallback(Throwable t) {
        return "Fallback response: " + t.getMessage();
    }
}
