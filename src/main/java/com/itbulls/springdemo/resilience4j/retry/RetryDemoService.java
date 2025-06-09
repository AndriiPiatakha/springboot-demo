package com.itbulls.springdemo.resilience4j.retry;

import io.github.resilience4j.retry.annotation.Retry;

import java.time.Instant;

import org.springframework.stereotype.Service;

@Service
public class RetryDemoService {

    private final UnreliableBackend backend;

    public RetryDemoService(UnreliableBackend backend) {
        this.backend = backend;
    }

    @Retry(name = "retryService", fallbackMethod = "fallback")
    public String callWithRetry() {
        return backend.unstableCall();
    }

    public String fallback(Throwable t) {
        return "Fallback triggered at " + Instant.now() + ". Reason: " + t.getMessage();
    }
}