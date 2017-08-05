package org.jetlang.core;

import java.util.List;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class TransferQueueEventQueue implements EventQueue {

    private final AtomicBoolean _running = new AtomicBoolean(true);
    private final TransferQueue<Runnable> _queue;

    TransferQueueEventQueue(TransferQueue<Runnable> queue) {
        _queue = queue;
    }

    public TransferQueueEventQueue() {
        this(new LinkedTransferQueue<Runnable>());
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
        try {
            _queue.transfer(r);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Runnable> swap(List<Runnable> buffer) {
        _queue.drainTo(buffer);
        return buffer;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
