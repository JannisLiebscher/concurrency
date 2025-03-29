package org.example.ex2.problem2;

import org.example.ex2.problem1.Account;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    static Cache cache = new Cache();
    public static void main(String[] args) throws InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(20);
        for (int i = 10; i < 50; i++) {
            final int number = i;
            executor.submit(() -> {
                Request request = new Request(number,"origin");
                Response response = new Response(new int[]{},"destination");
                service(request, response);
                System.out.println(cache);
            });
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
            Thread.sleep(100);
        }
        System.out.println("DONE");
    }

    private static void service(Request req, Response resp) {
        int i = req.getNumber();
        int[] factors = factor(i);
        cache.setLastNumber(i);
        cache.setLastFactors(factors);
        resp.setFactors(factors);
    }


    private static int[] factor(int number) {
        List<Integer> factors = new ArrayList<>();
        for (int i = 2; i <= number; i++) {
            while (number % i == 0) {
                factors.add(i);
                number /= i;
            }
        }
        return factors.stream().mapToInt(i -> i).toArray();
    }


}
