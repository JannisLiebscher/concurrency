package org.example.ex2.problem3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    static Cache cache = new Cache();
    public static void main(String[] args) throws InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(20);
        for (int number = 10; number <= 14; number++) {
            for (int j = 0; j < 10; j++) {
                final int num = number;
                executor.submit(() -> {
                    Request request = new Request(num, "origin");
                    Response response = new Response(new int[]{}, "destination");
                    service(request, response);
                    System.out.println(cache);
                    try {
                        Thread.sleep((int)(Math.random() * 10));
                    } catch (InterruptedException ignored) {}
                });
            }
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
            Thread.sleep(100);
        }
        System.out.println("DONE");
    }

    private static void service(Request req, Response resp) {
        int i = req.getNumber();
        if(i == cache.getLastNumber()){
            int[] cachedFactors = cache.getLastFactors();
            resp.setFactors(cachedFactors);

            resp.setFactors(cache.getLastFactors());
            if (!Arrays.equals(cachedFactors, factor(i))) {
                System.out.printf("⚠ Race detected! i=%d, cachedFactors=%s%n", i, Arrays.toString(cachedFactors));
            }
        }else{
            int[] factors = factor(i);
            cache.setLastNumber(i);
            cache.setLastFactors(factors);
            resp.setFactors(factors);
        }
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
