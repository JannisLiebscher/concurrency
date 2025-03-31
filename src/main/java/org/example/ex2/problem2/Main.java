package org.example.ex2.problem2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    static Cache cache = new Cache();
    public static void main(String[] args) throws InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 10; i < 50; i++) {
            int number = i;
            executor.submit(() -> {
                Request request = new Request(number,"origin");
                Response response = new Response(new int[]{},"destination");
                try {
                    service(request, response);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (cache.getLastNumber() != productOf(cache.getLastFactors())) {
                    System.out.println("FEHLER: Inkonsistenter Cache!");
                }
                System.out.println(cache);

            });
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
            Thread.sleep(100);
        }
        System.out.println("DONE");
    }

    private static void service(Request req, Response resp) throws InterruptedException {
        int i = req.getNumber();
        int[] factors = factor(i);
        cache.setLastNumber(i);
        Thread.sleep(1);
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
    private static int productOf(int[] numbers) {
        int product = 1;
        for (int n : numbers) {
            product *= n;
        }
        return product;
    }



}
