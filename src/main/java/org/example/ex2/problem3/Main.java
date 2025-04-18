package org.example.ex2.problem3;

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

        System.out.println(Arrays.toString(factor(21)));
    }

    private static void service(Request req, Response resp) throws InterruptedException {
        int i = req.getNumber();
        if(i == cache.getLastNumber()){
            Thread.sleep(1);
            int[] cachedFactors = cache.getLastFactors();
            resp.setFactors(cachedFactors);

            if (!Arrays.equals(cachedFactors, factor(i))) {
                System.out.printf(ANSI_RED + "⚠ Race condition detected! i=%d, cachedFactors=%s%n"
                        + ANSI_RESET, i, Arrays.toString(cachedFactors));
            } else {
                System.out.println("Cache successfully used");
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
