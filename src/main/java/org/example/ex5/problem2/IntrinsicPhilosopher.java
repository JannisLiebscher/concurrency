package org.example.ex5.problem2;

import java.util.concurrent.TimeUnit;

public class IntrinsicPhilosopher implements Runnable {
    private final char id;
    private boolean eating;
    private IntrinsicPhilosopher left, right;
    private final Object table;

    public IntrinsicPhilosopher(char id, Object table) {
        this.id = id;
        this.eating = false;
        this.table = table;
    }

    public void setLeft(IntrinsicPhilosopher left) {
        this.left = left;
    }

    public void setRight(IntrinsicPhilosopher right) {
        this.right = right;
    }

    @Override
    public void run() {
        try {
            while (true) {
                think();
                eat();
            }
        } catch (InterruptedException ex) {
            // nothing to clean up, terminate
        }
    }

    private void think() throws InterruptedException {
        synchronized (table) {
            eating = false;
            table.notifyAll();
        }
        System.out.println("Philosopher " + id + " thinks for a while");
        TimeUnit.SECONDS.sleep(1);
    }

    private void eat() throws InterruptedException {
        synchronized (table) {
            while (left.eating || right.eating) {
                table.wait();
            }
            eating = true;
        }
        System.out.println("Philosopher " + id + " eats for a while");
        TimeUnit.SECONDS.sleep(1);
    }
}
