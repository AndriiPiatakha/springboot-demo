package com.itbulls.springdemo.virtualthreads;

import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class FakeDataService {

    // Симулируем получение данных пользователя
    public String getUserData() {
        simulateDelay();
        return "User data: John Doe";
    }

    // Симулируем получение данных счета
    public String getAccountData() {
        simulateDelay();
        return "Account data: Balance $1000";
    }

    // Симулируем получение данных о транзакциях
    public String getTransactionData() {
        simulateDelay();
        return "Transaction data: 3 transactions";
    }

    // Метод для симуляции задержки
    private void simulateDelay() {
        try {
            TimeUnit.SECONDS.sleep(2); // Задержка в 2 секунды
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    // ====================
    
    public String getUserData(int clientId) {
        simulateDelay();
        return "UserData[" + clientId + "]";
    }

    public String getAccountData(int clientId) {
        simulateDelay();
        return "AccountData[" + clientId + "]";
    }

    public String getTransactionData(int clientId) {
        simulateDelay();
        return "TransactionData[" + clientId + "]";
    }
    
    // =====================
    
    public String getFakeData(int index) {
        simulateDelay();
        return "Response " + index;
    }
}
