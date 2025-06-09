package com.itbulls.springdemo.resilience4j.bulkhead;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class BulkheadService {

    @Bulkhead(name = "backendA", fallbackMethod = "fallback")
    public String processRequest() {
        try {
            TimeUnit.SECONDS.sleep(3); // simulate long processing
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "Processed by " + Thread.currentThread().getName();
    }

    public String fallback(Throwable t) {
        return "Too many concurrent requests, please try again later.";
    }
}