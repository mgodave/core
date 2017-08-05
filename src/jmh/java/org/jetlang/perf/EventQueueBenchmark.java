package org.jetlang.perf;

import org.jetlang.core.*;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
@BenchmarkMode({Mode.Throughput})
@State(Scope.Thread)
public class EventQueueBenchmark {

    ExecutorService executor = Executors.newFixedThreadPool(3);

    RunnableExecutor runnableExecutorWithBlockingQueue = new RunnableExecutorImpl(
            new BatchExecutorImpl(),
            new RunnableBlockingQueue()
    );

    RunnableExecutor runnableExecutorWithCustomQueue = new RunnableExecutorImpl(
            new BatchExecutorImpl(),
            new BlockingQueueEventQueue()
    );

    RunnableExecutor runnableExecutorWithTransferQueue = new RunnableExecutorImpl(
            new BatchExecutorImpl(),
            new TransferQueueEventQueue()
    );

    @Setup
    public void setup() {
        executor.execute(runnableExecutorWithBlockingQueue);
        executor.execute(runnableExecutorWithCustomQueue);
        executor.execute(runnableExecutorWithTransferQueue);
    }

    @TearDown
    public void teardown() throws Exception {
        runnableExecutorWithBlockingQueue.dispose();
        runnableExecutorWithCustomQueue.dispose();
        runnableExecutorWithBlockingQueue.dispose();
        executor.shutdownNow();
        executor.awaitTermination(10, TimeUnit.SECONDS);
    }

    @Benchmark
    @OperationsPerInvocation(100000)
    public void measureRunnableExecutorWithBlockingQueue() throws Exception{
        final CountDownLatch latch = new CountDownLatch(100000);
        for (int i = 0; i < 100000; i++) {
            runnableExecutorWithBlockingQueue.execute(new Runnable() {
                @Override
                public void run() {
                    Blackhole.consumeCPU(1);
                    latch.countDown();
                }
            });
        }
        latch.await();
    }

    @Benchmark
    @OperationsPerInvocation(100000)
    public void measureRunnableExecutorWithCustomQueue() throws Exception{
        final CountDownLatch latch = new CountDownLatch(100000);
        for (int i = 0; i < 100000; i++) {
            runnableExecutorWithCustomQueue.execute(new Runnable() {
                @Override
                public void run() {
                    Blackhole.consumeCPU(1);
                    latch.countDown();
                }
            });
        }
        latch.await();
    }

    @Benchmark
    @OperationsPerInvocation(100000)
    public void measureRunnableExecutorWithTransferQueue() throws Exception{
        final CountDownLatch latch = new CountDownLatch(100000);
        for (int i = 0; i < 100000; i++) {
            runnableExecutorWithTransferQueue.execute(new Runnable() {
                @Override
                public void run() {
                    Blackhole.consumeCPU(1);
                    latch.countDown();
                }
            });
        }
        latch.await();
    }

}
