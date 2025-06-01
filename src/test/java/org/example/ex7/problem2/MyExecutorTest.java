package org.example.ex7.problem2;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyExecutorTest {

    @Test
    public void testBasicExecution() throws InterruptedException {
        MyExecutor executor = new MyExecutor(3);
        AtomicInteger counter = new AtomicInteger(0);

        for (int i = 0; i < 10; i++) {
            executor.execute(counter::incrementAndGet);
        }

        Thread.sleep(500);

        assertEquals(10, counter.get());

        executor.shutdown();
    }

    @Test
    public void testCallableWithFuture() throws Exception {
        MyExecutor executor = new MyExecutor(2);

        Future<String> future = executor.submit(() -> {
            Thread.sleep(100);
            return "hello";
        });

        String result = future.get();
        assertEquals("hello", result);

        executor.shutdown();
    }

}
