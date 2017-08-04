package org.jetlang.perf;

import org.jetlang.channels.SubscriberList;
import org.jetlang.core.Callback;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
@BenchmarkMode({Mode.All})
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class SubscriberListBenchmark {

  static final int NUM_ITERATIONS = 100000;

  Callback<String>[] array;
  ListBasedSubscriberList<String> arrayList;
  ListBasedSubscriberList<String> linkedList;
  ListBasedSubscriberList<String> copyOnWriteArrayList;
  SubscriberList<String> subscriberList;

  @Setup
  public void setup() {
    array = new Callback[NUM_ITERATIONS];
    Arrays.fill(array, new NullCallback<String>());

    arrayList = new ListBasedSubscriberList<>(
        new ArrayList<>(Arrays.asList(array))
    );
    linkedList = new ListBasedSubscriberList<>(
        new LinkedList<>(Arrays.asList(array))
    );
    copyOnWriteArrayList = new ListBasedSubscriberList<>(
        new CopyOnWriteArrayList<>(Arrays.asList(array))
    );

    subscriberList = new SubscriberList<>();
    for (int i = 0; i < NUM_ITERATIONS; i++) {
      subscriberList.add(new NullCallback<String>());
    }
  }

  @Benchmark
  @OperationsPerInvocation(NUM_ITERATIONS)
  public void publishSubscriberList() {
    subscriberList.publish("hi");
  }

  @Benchmark
  @OperationsPerInvocation(NUM_ITERATIONS)
  public void iterateArrayList() {
    arrayList.publish("hi");
  }

  @Benchmark
  @OperationsPerInvocation(NUM_ITERATIONS)
  public void iterateLinkedList() {
    linkedList.publish("hi");
  }

  @Benchmark
  @OperationsPerInvocation(NUM_ITERATIONS)
  public void iterateCopyOnWriteArrayList() {
    copyOnWriteArrayList.publish("hi");
  }
}

class NullCallback<T> implements Callback<T> {
  @Override
  public void onMessage(T message) {
    Blackhole.consumeCPU(1);
  }
}