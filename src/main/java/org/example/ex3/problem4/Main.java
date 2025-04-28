package org.example.ex3.problem4;

import org.example.ex2.problem2.Request;
import org.example.ex2.problem2.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    static Cache cache = new Cache();
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static void main(String[] args) throws InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int number = 10; number <= 50; number++) {
            for (int j = 0; j < 10; j++) {
                final int num = number;
                executor.submit(() -> {
                    Request request = new Request(num, "origin");
                    Response response = new Response(new int[]{}, "destination");
                    try {
                        service(request, response);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    //System.out.println(cache);
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

    private static void service(Request req, Response resp) throws InterruptedException {
        int i = req.getNumber();
        int[] result;

        synchronized (cache) {
            if (i == cache.getLastNumber()) {
                Thread.sleep(1);
                int[] cachedFactors = cache.getLastFactors();
                resp.setFactors(cachedFactors);

                if (!Arrays.equals(cachedFactors, factor(i))) {
                    System.out.printf(ANSI_RED + "⚠ Race condition detected! i=%d, cachedFactors=%s%n"
                            + ANSI_RESET, i, Arrays.toString(cachedFactors));
                } else {
                    System.out.println("Cache successfully used");
                }
                return;
            }
        }

        // Faktorzerlegung wird außerhalb des Locks durchgeführt (rechenintensiv)
        result = factor(i);

        synchronized (cache) {
            cache.setLastNumber(i);
            cache.setLastFactors(result);
        }

        resp.setFactors(result);
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
