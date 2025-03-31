package org.example.ex2.problem1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Account account = new Account(1000);

        Runnable deposit= () -> {
            for (int i = 0; i < 500; i++) {
                account.deposit(1);
            }
        };

        Runnable withdraw = () -> {
            for (int i = 0; i < 500; i++) {
                account.withdraw(1);
            }
        };

        try (ExecutorService executor = Executors.newFixedThreadPool(10)) {

            for (int i = 0; i < 10; i++) {
                executor.execute(deposit);
                executor.execute(withdraw);
            }

            executor.shutdown();

            while (!executor.isTerminated()) {
                Thread.sleep(100);
            }
        }

        System.out.println("Guthaben: " + account.getBalance());
    }

}
