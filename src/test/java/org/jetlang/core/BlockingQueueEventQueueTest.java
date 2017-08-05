package org.jetlang.core;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BlockingQueueEventQueueTest {

    @Test
    public void testBlockingQueueEventQueue() throws Exception {

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        RunnableExecutor runnableExecutorWithCustomQueue = new RunnableExecutorImpl(
                new BatchExecutorImpl(),
                new BlockingQueueEventQueue()
        );

        executorService.execute(runnableExecutorWithCustomQueue);

        final CountDownLatch latch = new CountDownLatch(1);
        runnableExecutorWithCustomQueue.execute(new Runnable() {
            @Override
            public void run() {
                latch.countDown();
            }
        });
        latch.await();

        executorService.shutdownNow();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

    }

}