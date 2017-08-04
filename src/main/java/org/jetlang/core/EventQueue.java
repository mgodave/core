package org.jetlang.core;

import java.util.List;

public interface EventQueue {
    boolean isRunning();

    void setRunning(boolean isRunning);

    void put(Runnable r);

    List<Runnable> swap(List<Runnable> buffer);

    boolean isEmpty();
}
