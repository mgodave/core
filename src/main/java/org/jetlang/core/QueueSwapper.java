package org.jetlang.core;

import java.nio.channels.Selector;
import java.util.LinkedList;
import java.util.List;

public class QueueSwapper {
    private final Selector sink;
    private List<Runnable> _queue = new LinkedList<>();

    private boolean running = true;

    public QueueSwapper(Selector sink) {
        this.sink = sink;
    }


    public synchronized void put(Runnable r) {
        if (!running) {
            return;
        }
        _queue.add(r);
        if (_queue.size() == 1) {
            sink.wakeup();
        }
    }

    public synchronized List<Runnable> swap(List<Runnable> buffer) {
        List<Runnable> toReturn = _queue;
        _queue = buffer;
        return toReturn;
    }

    public synchronized int size() {
        return _queue.size();
    }

    public synchronized void setRunning(boolean running) {
        this.running = running;
    }
}
