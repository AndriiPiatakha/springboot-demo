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

    // Асинхронно получаем данные о пользователе
    @Async
    public CompletableFuture<String> getUserData() {
        System.out.println("Executing on thread: " + Thread.currentThread());
        return CompletableFuture.completedFuture(fakeDataService.getUserData());
    }

    // Асинхронно получаем данные о счете
    @Async
    public CompletableFuture<String> getAccountData() {
        System.out.println("Executing on thread: " + Thread.currentThread());
        return CompletableFuture.completedFuture(fakeDataService.getAccountData());
    }

    // Асинхронно получаем данные о транзакциях
    @Async
    public CompletableFuture<String> getTransactionData() {
        System.out.println("Executing on thread: " + Thread.currentThread());
        return CompletableFuture.completedFuture(fakeDataService.getTransactionData());
    }
    
    @Async
    public CompletableFuture<String> getUserDataFor(int clientId) {
        System.out.println("UserData for client " + clientId + " on thread: " + Thread.currentThread());
        return CompletableFuture.completedFuture(fakeDataService.getUserData(clientId));
    }

    @Async
    public CompletableFuture<String> getAccountDataFor(int clientId) {
        System.out.println("AccountData for client " + clientId + " on thread: " + Thread.currentThread());
        return CompletableFuture.completedFuture(fakeDataService.getAccountData(clientId));
    }

    @Async
    public CompletableFuture<String> getTransactionDataFor(int clientId) {
        System.out.println("TransactionData for client " + clientId + " on thread: " + Thread.currentThread());
        return CompletableFuture.completedFuture(fakeDataService.getTransactionData(clientId));
    }

    public String fetchDataForMultipleUsers(int numberOfUsers) {
        long start = System.currentTimeMillis();

        List<CompletableFuture<String>> allFutures = new ArrayList<>();

        for (int i = 0; i < numberOfUsers; i++) {
            int clientId = i;
            CompletableFuture<String> userFuture = getUserDataFor(clientId);
            CompletableFuture<String> accountFuture = getAccountDataFor(clientId);
            CompletableFuture<String> txFuture = getTransactionDataFor(clientId);

            CompletableFuture<String> clientCombined = CompletableFuture
                    .allOf(userFuture, accountFuture, txFuture)
                    .thenApply(v -> {
                        try {
                            return "Client #" + clientId + ": "
                                    + userFuture.get() + ", "
                                    + accountFuture.get() + ", "
                                    + txFuture.get();
                        } catch (Exception e) {
                            return "Client #" + clientId + ": ERROR";
                        }
                    });

            allFutures.add(clientCombined);
        }

        // Ожидаем завершения всех клиентов
        CompletableFuture.allOf(allFutures.toArray(new CompletableFuture[0])).join();

        long end = System.currentTimeMillis();
        System.out.printf("TOTAL TIME for %d users (Async): " + (end - start) + " ms", numberOfUsers);

        return "Done";
    }
    
    // Метод для фетчинга всех данных
    public String fetchAllData() {
        long start = System.currentTimeMillis();

        // Запуск всех асинхронных задач
        CompletableFuture<String> userData = getUserData();
        CompletableFuture<String> accountData = getAccountData();
        CompletableFuture<String> transactionData = getTransactionData();

        // Ожидаем завершения всех задач
        CompletableFuture.allOf(userData, accountData, transactionData).join();

        long end = System.currentTimeMillis();
        System.out.println("Time taken for async execution: " + (end - start) + " ms");

        try {
            // Возвращаем данные из каждого CompletableFuture
            return userData.get() + ", " + accountData.get() + ", " + transactionData.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Error occurred";
    }
    
    //=====================================
    
    public String fetchMassiveDataWithVirtualThreads() {
        int taskCount = 100;

        ExecutorService virtualExecutor = Executors.newVirtualThreadPerTaskExecutor();
        List<Callable<String>> tasks = new ArrayList<>();

        for (int i = 0; i < taskCount; i++) {
            int index = i;
            tasks.add(() -> {
                System.out.println("Thread: " + Thread.currentThread());
                return fakeDataService.getFakeData(index);
            });
        }

        long start = System.currentTimeMillis();

        try {
            List<Future<String>> results = virtualExecutor.invokeAll(tasks);

            StringBuilder sb = new StringBuilder();
            for (Future<String> result : results) {
                sb.append(result.get()).append("\n");
            }

            long end = System.currentTimeMillis();
            System.out.println("TOTAL TIME (Virtual Threads): " + (end - start) + " ms");

            return sb.toString();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return "Error";
        } finally {
            virtualExecutor.shutdown();
        }
    }
    
    
    public String fetchMassiveDataWithRegularThreads() {
        int taskCount = 100;

        // Создаём фиксированный пул потоков (например, 20 потоков)
        ExecutorService regularExecutor = Executors.newFixedThreadPool(20);
        List<Callable<String>> tasks = new ArrayList<>();

        for (int i = 0; i < taskCount; i++) {
            int index = i;
            tasks.add(() -> {
                System.out.println("Thread: " + Thread.currentThread());
                return fakeDataService.getFakeData(index);
            });
        }

        long start = System.currentTimeMillis();

        try {
            List<Future<String>> results = regularExecutor.invokeAll(tasks);

            StringBuilder sb = new StringBuilder();
            for (Future<String> result : results) {
                sb.append(result.get()).append("\n");
            }

            long end = System.currentTimeMillis();
            System.out.println("TOTAL TIME (Regular Threads): " + (end - start) + " ms");

            return sb.toString();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return "Error";
        } finally {
            regularExecutor.shutdown();
        }
    }
}
