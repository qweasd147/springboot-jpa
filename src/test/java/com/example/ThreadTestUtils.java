package com.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class ThreadTestUtils {

    public final static int NUM_THREADS = 10;
    public final static int NUM_ITERATIONS = 100;

    public static boolean run(int threadCount, Function<Integer, Runnable> toRunnable) throws InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {

            Runnable runnable = toRunnable.apply(i);
            executor.submit(runnable);
        }

        executor.shutdown();
        return executor.awaitTermination(10, TimeUnit.SECONDS);
    }
}
