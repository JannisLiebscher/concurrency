package org.example.ex3.problem5;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class Main {

    static AtomicReference<WinnerSnapshot> winner = new AtomicReference<>(null);
    static String step = "10";

    public static void main(String[] args) throws InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(20);
        BigInteger nonce = BigInteger.ZERO;
        long start = System.currentTimeMillis();

        while (winner.get() == null) {
            BigInteger finalNonce = nonce;
            executor.submit(() -> {
                try {
                    service("new block", new BigInteger(String.valueOf(BigInteger.valueOf(2).pow(237))), finalNonce, start);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            });
            nonce = nonce.add(new BigInteger(step));
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
            Thread.sleep(100);
        }

        WinnerSnapshot result = winner.get();
        if (result != null) {
            System.out.println("Nonce found: " + result.nonce);
            System.out.println("Hash: " + result.hash);
            System.out.println("Time spend: " + result.time + " S");
        }
        System.out.println("DONE");
    }

    private static void service(String block, BigInteger target, BigInteger nonce, long startTime) throws NoSuchAlgorithmException {
        BigInteger lastNonce = nonce.add(new BigInteger(step));

        while (!nonce.equals(lastNonce) && winner.get() == null) {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] input = (block + nonce.toString()).getBytes();
            byte[] hash = digest.digest(digest.digest(input));
            BigInteger hashInt = new BigInteger(1, hash); // Unsigned

            if (hashInt.compareTo(target) < 0) {
                double elapsed = (double) (System.currentTimeMillis() - startTime) / 1000;
                WinnerSnapshot candidate = new WinnerSnapshot(nonce, hashInt, elapsed);

                // Versuche, den Gewinner zu setzen (nur der Erste gewinnt)
                winner.compareAndSet(null, candidate);
            }

            nonce = nonce.add(BigInteger.ONE);
        }
    }

    static class WinnerSnapshot {
        final BigInteger nonce;
        final BigInteger hash;
        final double time;

        WinnerSnapshot(BigInteger nonce, BigInteger hash, double time) {
            this.nonce = nonce;
            this.hash = hash;
            this.time = time;
        }
    }
}

