package org.example.ex7.problem3;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Main {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        MyCompletionService<List<String>> completionService = new MyCompletionService<>(10);

        for (int i = 100; i >= 10; i -= 10) {
            String s = RandomStringUtils.insecure().nextAlphabetic(i);

            Callable<List<String>> sortTask = () -> {

                int random = (int)(Math.random() * 10);
                if(random <= 4) throw new ForcedException();

                String original = String.valueOf(s);
                char[] chars = s.toCharArray();
                Arrays.sort(chars);
                String sorted = new String(chars);

                return Arrays.asList(original, sorted, String.valueOf(random));
            };

            try {
                completionService.submit(sortTask);
            } catch (InterruptedException e) {
                System.out.println("Task unterbrochen");
            }

        }

        while (true) {
            Future<List<String>> result = null;
            try {
                result = completionService.poll();
            } catch (InterruptedException e) {
                System.out.print("Poll wurde unterbrochen");
            }
            if(result == null) break; // (Vermutlich) Letzter Task
            if(result.state() == Future.State.FAILED) {
                System.out.println("\n##### Task Failed #####\n");
            } else {
                System.out.println("Original:" + result.get().get(0));
                System.out.println("Sortiert:" + result.get().get(1));
            }
        }
        completionService.shutdown();
    }
}
