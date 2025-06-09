package com.itbulls.springdemo.resilience4j.timelimiter;

import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class SlowService {

    public CompletableFuture<String> longCall() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(5000); // Simulate long call (5 sec)
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            return "Completed after delay";
        });
    }
}