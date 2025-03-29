package org.example.ex2.problem4;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.security.MessageDigest;

public class Main {
    static BigInteger winnerNonce = new BigInteger("-1");
    static BigInteger winnerHash;
    static double winnerTime;

    public static void main(String[] args) throws InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(20);
        BigInteger nonce = new BigInteger("0");
        long start = System.currentTimeMillis();
        while (winnerNonce.equals(new BigInteger("-1"))) {
            BigInteger finalNonce = nonce;
            executor.submit(() -> {
                try {
                    service("new block", new BigInteger("400000000000000000000000000000000000000000000000000000000000000000000000"), finalNonce);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            });
            nonce = nonce.add(new BigInteger("1"));

        }
        long end = System.currentTimeMillis();
        winnerTime = (double) (end - start) / 1000;
        executor.shutdown();
        while (!executor.isTerminated()) {
            Thread.sleep(100);
        }
        System.out.println("Nonce found: " + winnerNonce);
        System.out.println("Hash: " + winnerHash);
        System.out.println("Time spend: " + winnerTime + " S");
        System.out.println("DONE");
    }

    private static void service(String block, BigInteger target, BigInteger nonce) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] input = (block + nonce.toString()).getBytes();

        byte[] hash = digest.digest((digest.digest(input)));

        if (new BigInteger(hash).compareTo(target) < 0 && new BigInteger(hash).compareTo(new BigInteger("0")) > 0) {
            winnerNonce = nonce;
            winnerHash = new BigInteger(hash);
        }
    }
}
