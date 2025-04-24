package com.itbulls.springdemo.virtualthreads;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service
public class UserService {

    private final UserDataSource fakeDataSource;

    public UserService(UserDataSource fakeDataService) {
        this.fakeDataSource = fakeDataService;
    }

    // Asynchronously retrieve user data
    @Async
    public CompletableFuture<String> getUserData() {
        System.out.println("Executing on thread: " + Thread.currentThread());
        return CompletableFuture.completedFuture(fakeDataSource.getUserData());
    }

    // Asynchronously retrieve account data
    @Async
    public CompletableFuture<String> getAccountData() {
        System.out.println("Executing on thread: " + Thread.currentThread());
        return CompletableFuture.completedFuture(fakeDataSource.getAccountData());
    }

    // Asynchronously retrieve transaction data
    @Async
    public CompletableFuture<String> getTransactionData() {
        System.out.println("Executing on thread: " + Thread.currentThread());
        return CompletableFuture.completedFuture(fakeDataSource.getTransactionData());
    }
    
    // Method to fetch all data
    public String fetchAllData() {
        // Launch all asynchronous tasks
        CompletableFuture<String> userData = getUserData();
        CompletableFuture<String> accountData = getAccountData();
        CompletableFuture<String> transactionData = getTransactionData();

        // Wait for all tasks to complete
        CompletableFuture.allOf(userData, accountData, transactionData).join();

        try {
        	// Return data from each CompletableFuture
            return userData.get() + ", " + accountData.get() + ", " + transactionData.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Error occurred";
    }
}
