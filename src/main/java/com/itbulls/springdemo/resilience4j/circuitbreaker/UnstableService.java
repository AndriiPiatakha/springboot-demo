package com.itbulls.springdemo.resilience4j.circuitbreaker;

import org.springframework.stereotype.Service;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UnstableService {

    private final AtomicInteger counter = new AtomicInteger(0);

    public String callExternalService() {
        int count = counter.incrementAndGet();
        if (count % 3 != 0) { // fail 2 out of 3 calls
            throw new RuntimeException("Simulated service failure");
        }
        return "Success from external service!";
    }
}
