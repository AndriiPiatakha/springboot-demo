package com.itbulls.springdemo.resilience4j.retrycb;

import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

@Service
public class ReliableService {

    private final UnstableBackend backend;

    public ReliableService(UnstableBackend backend) {
        this.backend = backend;
    }

    @Retry(name = "backendService")
    @CircuitBreaker(name = "backendService", fallbackMethod = "fallback")
    public String callExternal() {
        return backend.call();
    }

    public String fallback(Throwable t) {
        return "Fallback triggered: " + t.getMessage();
    }
}