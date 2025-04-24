package com.itbulls.springdemo.virtualthreads;

import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class UserDataSource {

    // Simulate retrieving user data
    public String getUserData() {
        simulateDelay();
        return "User data: John Doe";
    }

    // Simulate retrieving account data
    public String getAccountData() {
        simulateDelay();
        return "Account data: Balance $1000";
    }

    // Simulate retrieving transaction data
    public String getTransactionData() {
        simulateDelay();
        return "Transaction data: 3 transactions";
    }

    // Method to simulate delay
    private void simulateDelay() {
        try {
            TimeUnit.SECONDS.sleep(2); // 2-second delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
