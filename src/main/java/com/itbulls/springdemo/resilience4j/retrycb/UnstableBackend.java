package com.itbulls.springdemo.resilience4j.retrycb;

import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class UnstableBackend {

    public String unstableCall() {
        long second = Instant.now().getEpochSecond();

        if (second % 3 != 0) {
            System.out.println("Simulating failure");
            throw new RuntimeException("Backend failure");
        }

        return "Success!";
    }
}