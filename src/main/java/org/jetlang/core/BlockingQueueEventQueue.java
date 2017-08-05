package org.jetlang.core;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class BlockingQueueEventQueue implements EventQueue {

    private final AtomicBoolean _running = new AtomicBoolean(true);
    private final BlockingQueue<Runnable> _queue;

    BlockingQueueEventQueue(BlockingQueue<Runnable> queue) {
        _queue = queue;
    }

    public BlockingQueueEventQueue() {
        this(new LinkedBlockingQueue<Runnable>());
    }

    @Override
    public boolean isRunning() {
        return _running.get();
    }

    @Override
    public void setRunning(boolean isRunning) {
        _running.set(isRunning);
    }

    @Override
    public void put(Runnable r) {
        _queue.offer(r);
    }

    @Override
    public List<Runnable> swap(List<Runnable> buffer) {
        _queue.drainTo(buffer);
        return buffer;
    }

    @Override
    public boolean isEmpty() {
        return _queue.isEmpty();
    }
}

