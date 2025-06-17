package com.itbulls.springdemo.resilience4j.ratelimiter;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.stereotype.Service;

@Service
public class RateLimitedService {

    @RateLimiter(name = "apiRateLimiter", fallbackMethod = "rateLimitFallback")
    public String getData() {
        return "Request successful!";
    }

    public String rateLimitFallback(Throwable t) {
        return "Too many requests – please try again later.";
    }
}
