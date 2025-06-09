package com.itbulls.springdemo.resilience4j.retrycb;

import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class UnstableBackend {

    public String call() {
        long second = Instant.now().getEpochSecond();
        System.out.println("Second: " + second);

        if (second % 3 != 0) {
            throw new RuntimeException("Simulated failure");
        }

        return "Success at second " + second;
    }
}