package com.itbulls.springdemo.resilience4j.timelimiter;

import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class TimeLimiterDemoService {

    private final SlowService slowService;

    public TimeLimiterDemoService(SlowService slowService) {
        this.slowService = slowService;
    }

    @TimeLimiter(name = "slowService", fallbackMethod = "timeoutFallback")
    public CompletableFuture<String> callWithTimeout() {
        return slowService.longCall();
    }

    public CompletableFuture<String> timeoutFallback(Throwable t) {
        return CompletableFuture.completedFuture("Fallback: request timed out");
    }
}