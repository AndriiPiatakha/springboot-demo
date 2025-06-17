package com.itbulls.springdemo.resilience4j.bulkhead;

import org.springframework.stereotype.Service;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;

import java.util.concurrent.CompletableFuture;

@Service
public class AsyncBulkheadService {

	@Bulkhead(name = "backendAsync", type = Bulkhead.Type.THREADPOOL,
	          fallbackMethod = "fallbackAsync")
    public CompletableFuture<String> processAsyncRequest() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "Processed by ASYNC: " + Thread.currentThread().getName() + System.lineSeparator();
        });
    }

    public CompletableFuture<String> fallbackAsync(Throwable t) {
        return CompletableFuture.completedFuture("Async fallback: bulkhead full." + System.lineSeparator());
    }
}