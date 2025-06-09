package com.itbulls.springdemo.resilience4j.retry;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UnreliableBackend {

    public String unstableCall() {
        long second = Instant.now().getEpochSecond();
        System.out.println("Current second: " + second);

        if (second % 3 != 0) {
            System.out.println("Simulating failure");
            throw new RuntimeException("Backend failure");
        }

        return "Success at second: " + second;
    }
}