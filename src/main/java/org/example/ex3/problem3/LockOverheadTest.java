package org.example.ex3.problem3;

public class LockOverheadTest {

    private int counter = 0;

    public synchronized void incrementSync() {
        counter++;
    }

    public void incrementPlain() {
        counter++;
    }

    public static void main(String[] args) {
        final int iterations = 100_000_000;
        LockOverheadTest test = new LockOverheadTest();

        // unsynchronized
        long startPlain = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            test.incrementPlain();
        }
        long endPlain = System.nanoTime();
        long durationPlain = endPlain - startPlain;
        System.out.println("Unsynchronized duration: " + durationPlain / 1_000_000 + " ms");

        test.counter = 0;

        // synchronized
        long startSync = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            test.incrementSync();
        }
        long endSync = System.nanoTime();
        long durationSync = endSync - startSync;
        System.out.println("Synchronized duration: " + durationSync / 1_000_000 + " ms");

        double overhead = (double) durationSync / durationPlain;
        System.out.printf("Overhead: %.2fx%n", overhead);
    }
}

