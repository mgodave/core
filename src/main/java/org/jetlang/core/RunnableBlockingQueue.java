package org.jetlang.core;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Blocking queue supporting efficient put and sweep operations.
 *
 * @author mrettig
 */
public class RunnableBlockingQueue implements EventQueue {

  private final Lock _lock = new ReentrantLock();
  private final Condition _waiter = _lock.newCondition();
  private volatile boolean _running = true;
  private List<Runnable> _queue = new LinkedList<>();

  public boolean isRunning() {
    return _running;
  }

  public void setRunning(boolean isRunning) {
    this._running = isRunning;
  }

  public void put(Runnable r) {
    _lock.lock();
    try {
      _queue.add(r);
      _waiter.signal();
    } finally {
      _lock.unlock();
    }
  }

  public List<Runnable> swap(List<Runnable> buffer) {
    _lock.lock();
    try {
      while (_queue.isEmpty() && _running) {
        try {
          _waiter.await();
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
      List<Runnable> toReturn = _queue;
      _queue = buffer;
      return toReturn;
    } finally {
      _lock.unlock();
    }
  }

  public boolean isEmpty() {
    _lock.lock();
    try {
      return _queue.isEmpty();
    } finally {
      _lock.unlock();
    }
  }
}
