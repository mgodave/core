package org.jetlang.perf;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
@BenchmarkMode({Mode.Throughput})
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class ListIterationBenchmark {

  static final int NUM_ITERATIONS = 100000;

  String[] array;
  ArrayList<String> arrayList;
  LinkedList<String> linkedList;
  CopyOnWriteArrayList<String> copyOnWriteArrayList;

  @Setup
  public void setup() {
    array = new String[NUM_ITERATIONS];
    Arrays.fill(array, "hi");

    arrayList = new ArrayList<>(Arrays.asList(array));
    linkedList = new LinkedList<>(Arrays.asList(array));
    copyOnWriteArrayList = new CopyOnWriteArrayList<>(Arrays.asList(array));
  }

  @Benchmark
  @OperationsPerInvocation(NUM_ITERATIONS)
  public void iterateArrayIterable(Blackhole bh) {
    for (String str : array) {
      bh.consume(str);
    }
  }

  @SuppressWarnings("ForLoopReplaceableByForEach")
  @Benchmark
  @OperationsPerInvocation(NUM_ITERATIONS)
  public void iterateArrayIndexed(Blackhole bh) {
    for (int i = 0; i < array.length; i++) {
      bh.consume(array[i]);
    }
  }

  @Benchmark
  @OperationsPerInvocation(NUM_ITERATIONS)
  public void iterateArrayList(Blackhole bh) {
    for (String str : arrayList) {
      bh.consume(str);
    }
  }

  @Benchmark
  @OperationsPerInvocation(NUM_ITERATIONS)
  public void iterateLinkedList(Blackhole bh) {
    for (String str : linkedList) {
      bh.consume(str);
    }
  }

  @Benchmark
  @OperationsPerInvocation(NUM_ITERATIONS)
  public void iterateCopyOnWriteArrayList(Blackhole bh) {
    for (String str : copyOnWriteArrayList) {
      bh.consume(str);
    }
  }
}