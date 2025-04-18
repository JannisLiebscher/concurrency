package org.example.ex3.problem2;

import java.io.IOException;
import java.net.URI;
import java.net.URL;

public class Main {
    public static void main(String[] args) throws Exception {
        URL url = URI.create("https://example.com").toURL();
        Downloader downloader = new Downloader(url, "output.txt");

        downloader.addListener(new DeadlockingProgressListener(downloader));

        Thread downloadThread = new Thread(() -> {
            try {
                downloader.run();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        downloadThread.start();
        downloadThread.join();
    }
}
