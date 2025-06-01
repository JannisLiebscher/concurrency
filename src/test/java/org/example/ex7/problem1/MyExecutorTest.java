package org.example.ex7.problem1;

import org.junit.jupiter.api.Test;

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
}
