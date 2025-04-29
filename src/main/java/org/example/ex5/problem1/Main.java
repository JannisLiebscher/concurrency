package org.example.ex5.problem1;

public class Main {
    public static void main(String[] args) {
        ConcurrentList list = new ConcurrentList();
        boolean boring = false;

        Thread inserter = new Thread(() -> {
            int i = 1;
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    i = list.insert(i++) ? i : --i;
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
                    i = list.delete(i++) ? i : --i;
                    Thread.sleep(boring ? 1000 : 500);
                }
            } catch (InterruptedException e) {
                System.out.println("Deleter interrupted");
            }
        });

        inserter.start();

        try {
            Thread.sleep(10000); // wait 10 seconds before starting deleter
            deleter.start();
            if(boring)
            {
                Thread.sleep(50000); // run for 1 minute
            } else
            {
                System.out.println(list);
                Thread.sleep(5000); // run for 5 seconds
                System.out.println(list);
                Thread.sleep(5000); // run for 5 seconds
                System.out.println(list);
                Thread.sleep(30000); // run for 30 more seconds
            }

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
        if(!boring) System.out.println(list);
        System.out.println("Test program terminated.");
    }
}
