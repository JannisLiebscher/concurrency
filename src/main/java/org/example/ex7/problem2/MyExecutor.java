package org.example.ex7.problem2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class MyExecutor{

    private final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
    private final List<Thread> pool;

    public MyExecutor(int numberOfThreads) {
        pool = new ArrayList<Thread>();
        for(int i = 0; i<numberOfThreads; i++){
            Thread t = new Thread(new Worker());
            pool.add(t);
            t.start();
        }
    }

    private class Worker implements Runnable{
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    queue.take().run();
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }

    public void execute(Runnable command) throws InterruptedException {
        queue.put(command);
    }

    public <T> Future<T> submit(Callable<T> task) throws InterruptedException {
        FutureTask<T> futureTask = new FutureTask<>(task);
        queue.put(futureTask);
        return futureTask;
    }


    public void shutdown() {
        for(Thread t : pool){
            t.interrupt();
        }
    }
}
