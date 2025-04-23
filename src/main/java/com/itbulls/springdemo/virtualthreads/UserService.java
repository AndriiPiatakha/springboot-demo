package com.itbulls.springdemo.virtualthreads;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service
public class UserService {

    private final FakeDataService fakeDataService;

    public UserService(FakeDataService fakeDataService) {
        this.fakeDataService = fakeDataService;
    }

    // Asynchronously retrieve user data
    @Async
    public CompletableFuture<String> getUserData() {
        System.out.println("Executing on thread: " + Thread.currentThread());
        return CompletableFuture.completedFuture(fakeDataService.getUserData());
    }

    // Asynchronously retrieve account data
    @Async
    public CompletableFuture<String> getAccountData() {
        System.out.println("Executing on thread: " + Thread.currentThread());
        return CompletableFuture.completedFuture(fakeDataService.getAccountData());
    }

    // Asynchronously retrieve transaction data
    @Async
    public CompletableFuture<String> getTransactionData() {
        System.out.println("Executing on thread: " + Thread.currentThread());
        return CompletableFuture.completedFuture(fakeDataService.getTransactionData());
    }
    
    // Method to fetch all data
    public String fetchAllData() {
        long start = System.currentTimeMillis();

        // Launch all asynchronous tasks
        CompletableFuture<String> userData = getUserData();
        CompletableFuture<String> accountData = getAccountData();
        CompletableFuture<String> transactionData = getTransactionData();

        // Wait for all tasks to complete
        CompletableFuture.allOf(userData, accountData, transactionData).join();

        long end = System.currentTimeMillis();
        System.out.println("Time taken for async execution: " + (end - start) + " ms");

        try {
        	// Return data from each CompletableFuture
            return userData.get() + ", " + accountData.get() + ", " + transactionData.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Error occurred";
    }
}
