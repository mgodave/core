package org.jetlang.perf;

import org.jetlang.core.Callback;

import java.util.List;

public class ListBasedSubscriberList<T> {
  private final List<Callback<T>> subscribers;

  public ListBasedSubscriberList(List<Callback<T>> subscribers) {
    this.subscribers = subscribers;
  }

  private static <V> void executeAll(final V msg, final List<Callback<V>> cbs) {
    for (Callback<V> cb : cbs) {
      cb.onMessage(msg);
    }
  }

  public synchronized void add(Callback<T> cb) {
    subscribers.add(cb);
  }

  public synchronized int size() {
    return subscribers.size();
  }

  public synchronized void clear() {
    subscribers.clear();
  }

  public synchronized boolean remove(Callback<T> cb) {
    return subscribers.remove(cb);
  }

  public synchronized void publish(T msg) {
    executeAll(msg, subscribers);
  }
}
