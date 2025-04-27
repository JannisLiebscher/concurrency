package org.example.ex5.problem2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        final int NUM_PHILOSOPHERS = 5;
        Object table = new Object(); // gemeinsamer Tisch für Synchronisation

        // Erzeuge die Philosophen
        IntrinsicPhilosopher[] philosophers = new IntrinsicPhilosopher[NUM_PHILOSOPHERS];
        char id = 'A';
        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            philosophers[i] = new IntrinsicPhilosopher((char) (id + i), table);
        }

        // Setze linke und rechte Nachbarn
        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            philosophers[i].setLeft(philosophers[(i + NUM_PHILOSOPHERS - 1) % NUM_PHILOSOPHERS]);
            philosophers[i].setRight(philosophers[(i + 1) % NUM_PHILOSOPHERS]);
        }

        // Starte die Philosophen
        ExecutorService executor = Executors.newFixedThreadPool(NUM_PHILOSOPHERS);
        for (IntrinsicPhilosopher philosopher : philosophers) {
            executor.execute(philosopher);
        }

        // Lass sie 1 Minute arbeiten
        TimeUnit.MINUTES.sleep(1);

        // Beende die Simulation
        executor.shutdownNow(); // schicke Interrupts
        executor.awaitTermination(5, TimeUnit.SECONDS); // kurze Zeit auf sauberes Beenden warten

        System.out.println("Dining Philosophers simulation finished.");
    }
}
