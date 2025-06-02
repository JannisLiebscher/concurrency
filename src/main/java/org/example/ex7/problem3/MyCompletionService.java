package org.example.ex7.problem3;

import org.example.ex7.problem2.MyExecutor;

import java.util.concurrent.*;

public class MyCompletionService<T> {

    private final MyExecutor executor;
    private final BlockingQueue<Future<T>> completionQueue = new LinkedBlockingQueue<>();

    public MyCompletionService(int numberThreads) {
        this.executor = new MyExecutor(numberThreads);
    }

    public void submit(Callable<T> task) throws InterruptedException {
        FutureTask<T> futureTask = new FutureTask<>(task) {
            @Override
            protected void done() {
                completionQueue.add(this);
            }
        };
        executor.execute(futureTask);
    }

    public Future<T> poll() throws InterruptedException {
        return completionQueue.poll(2L, TimeUnit.SECONDS);
    }

    public void shutdown() {
        executor.shutdown();
    }
}
