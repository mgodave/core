package org.jetlang.perf;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;

@BenchmarkMode({Mode.Throughput})
@State(Scope.Thread)
@Measurement(batchSize = 1, iterations = 5)
@Warmup(batchSize = 1, iterations = 5)
@Fork(1)
public class ListIterationBenchmark {

  String[] array;
  ArrayList<String> arrayList;
  LinkedList<String> linkedList;
  CopyOnWriteArrayList<String> copyOnWriteArrayList;

  @Setup
  public void setup() {
    array = new String[100000];
    Arrays.fill(array, "hi");

    arrayList = new ArrayList<>(Arrays.asList(array));
    linkedList = new LinkedList<>(Arrays.asList(array));
    copyOnWriteArrayList = new CopyOnWriteArrayList<>(Arrays.asList(array));
  }

  @Benchmark
  @OperationsPerInvocation(100000)
  public void iterateArrayIterable(Blackhole bh) {
    for (String str : array) {
      bh.consume(str);
    }
  }

  @Benchmark
  @OperationsPerInvocation(100000)
  public void iterateArrayIndexed(Blackhole bh) {
    for (int i = 0; i < array.length; i++) {
      bh.consume(array[i]);
    }
  }

  @Benchmark
  @OperationsPerInvocation(100000)
  public void iterateArrayList(Blackhole bh) {
    for (String str : arrayList) {
      bh.consume(str);
    }
  }

  @Benchmark
  @OperationsPerInvocation(100000)
  public void iterateLinkedList(Blackhole bh) {
    for (String str : linkedList) {
      bh.consume(str);
    }
  }

  @Benchmark
  @OperationsPerInvocation(100000)
  public void iterateCopyOnWriteArrayList(Blackhole bh) {
    for (String str : copyOnWriteArrayList) {
      bh.consume(str);
    }
  }
}