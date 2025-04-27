package org.example.ex5.problem1;

public class Main {
    public static void main(String[] args) {
        ConcurrentList list = new ConcurrentList();

        Thread inserter = new Thread(() -> {
            int i = 1;
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    list.insert(i++);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                System.out.println("Inserter interrupted");
            }
        });

        Thread deleter = new Thread(() -> {
            int i = 1;
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    list.delete(i++);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                System.out.println("Deleter interrupted");
            }
        });

        inserter.start();

        try {
            Thread.sleep(10000); // wait 10 seconds before starting deleter
            deleter.start();

            Thread.sleep(60000); // run for 1 minute
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        inserter.interrupt();
        deleter.interrupt();

        try {
            inserter.join();
            deleter.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Test program terminated.");
    }
}
