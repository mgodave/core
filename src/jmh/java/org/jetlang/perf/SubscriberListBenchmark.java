package org.jetlang.perf;

import org.jetlang.channels.SubscriberList;
import org.jetlang.core.Callback;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@BenchmarkMode({Mode.Throughput})
@State(Scope.Thread)
@Measurement(batchSize = 1, iterations = 5)
@Warmup(batchSize = 1, iterations = 5)
@Fork(1)
public class SubscriberListBenchmark {

  Callback<String>[] array;
  ListBasedSubscriberList<String> arrayList;
  ListBasedSubscriberList<String> linkedList;
  ListBasedSubscriberList<String> copyOnWriteArrayList;
  SubscriberList<String> subscriberList;

  @Setup
  public void setup() {
    array = new Callback[100000];
    Arrays.fill(array, new NullCallback<String>());

    arrayList = new ListBasedSubscriberList<>(new ArrayList<>(Arrays.asList(array)));
    linkedList = new ListBasedSubscriberList<>(new LinkedList<>(Arrays.asList(array)));
    copyOnWriteArrayList = new ListBasedSubscriberList<>(new CopyOnWriteArrayList<>(Arrays.asList(array)));

    subscriberList = new SubscriberList<>();
    for (int i = 0; i < 100000; i++) {
      subscriberList.add(new NullCallback<String>());
    }
  }

  @Benchmark
  @OperationsPerInvocation(100000)
  public void publishSubscriberList() {
    subscriberList.publish("hi");
  }

  @Benchmark
  @OperationsPerInvocation(100000)
  public void iterateArrayList() {
    arrayList.publish("hi");
  }

  @Benchmark
  @OperationsPerInvocation(100000)
  public void iterateLinkedList() {
    linkedList.publish("hi");
  }

  @Benchmark
  @OperationsPerInvocation(100000)
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