package com.itbulls.springdemo.resilience4j.bulkhead;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;

@Service
public class BulkheadService {

    @Bulkhead(name = "backendSync", fallbackMethod = "fallbackSync")
    public String processSyncRequest() {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "Processed by SYNC: " + Thread.currentThread().getName() + System.lineSeparator();
    }

    public String fallbackSync(Throwable t) {
        return "Sync fallback: too many concurrent requests." + System.lineSeparator();
    }
}