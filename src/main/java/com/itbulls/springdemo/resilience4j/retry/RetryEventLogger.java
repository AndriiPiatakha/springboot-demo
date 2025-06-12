package com.itbulls.springdemo.resilience4j.retry;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.event.RetryOnRetryEvent;
import jakarta.annotation.PostConstruct;
import io.github.resilience4j.retry.event.RetryEvent;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RetryEventLogger {

    private final RetryRegistry retryRegistry;

    public RetryEventLogger(RetryRegistry retryRegistry) {
        this.retryRegistry = retryRegistry;
    }

    @PostConstruct
    public void registerRetryEventListener() {
        Retry retry = retryRegistry.retry("retryService");
        retry.getEventPublisher().onRetry(event -> logRetry(event));
    }

    private void logRetry(RetryOnRetryEvent event) {
        System.out.println("Retry attempt #" + event.getNumberOfRetryAttempts() +
                " for " + event.getName() + ". Last exception: " + event.getLastThrowable().getMessage());
    }
}