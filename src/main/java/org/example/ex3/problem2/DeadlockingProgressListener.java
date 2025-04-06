package org.example.ex3.problem2;

public class DeadlockingProgressListener implements ProgressListener {
    private final Downloader downloader;

    public DeadlockingProgressListener(Downloader downloader) {
        this.downloader = downloader;
    }

    @Override
    public void onProgress(int n) {
        System.out.println("Progress: " + n);

        Thread t = new Thread(() -> {
            downloader.addListener(new ProgressListener() {
                @Override
                public void onProgress(int n) {
                    System.out.println("Nested progress: " + n);
                }
            });
        });
        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

