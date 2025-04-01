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
    static String step = "1";

    public static void main(String[] args) throws InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(40);
        BigInteger nonce = new BigInteger("0");
        long start = System.currentTimeMillis();
        while (winnerNonce.equals(new BigInteger("-1"))) {
            BigInteger finalNonce = nonce;
            executor.submit(() -> {
                try {
                    service("new block", new BigInteger(String.valueOf(BigInteger.valueOf(2).pow(237))), finalNonce);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            });
            nonce = nonce.add(new BigInteger(step));

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
        BigInteger lastNonce = nonce.add(new BigInteger(step));
        while (!nonce.equals(lastNonce) && winnerNonce.equals(new BigInteger("-1"))) {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] input = (block + nonce.toString()).getBytes();

            byte[] hash = digest.digest((digest.digest(input)));

            if (new BigInteger(hash).compareTo(target) < 0 && new BigInteger(hash).compareTo(new BigInteger("0")) > 0) {
                winnerNonce = nonce;
                winnerHash = new BigInteger(hash);
            }
            nonce = nonce.add(new BigInteger("1"));
        }
    }
}
