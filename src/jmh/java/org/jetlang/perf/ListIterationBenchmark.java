package org.jetlang.perf;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@BenchmarkMode({Mode.Throughput})
@State(Scope.Thread)
//@Measurement(batchSize = 1, iterations = 5)
//@Warmup(batchSize = 1, iterations = 5)
//@Fork(1)
public class ListIterationBenchmark {

  String[] array = new String[100000];
  ArrayList<String> arrayList = new ArrayList<>(100000);
  LinkedList<String> linkedList = new LinkedList<>(arrayList);
  CopyOnWriteArrayList<String> copyOnWriteArrayList = new CopyOnWriteArrayList<>(arrayList);

  @Setup
  public void setup() {
    Arrays.fill(array, "hi");
    Collections.fill(arrayList, "hi");
    Collections.fill(linkedList, "hi");
    Collections.fill(copyOnWriteArrayList, "hi");
  }

  @Benchmark
  @OperationsPerInvocation(100000)
  public void iterateArray(Iterating<String> iterating) throws Exception {
    iterating.iterateT(array);
  }

  @Benchmark
  @OperationsPerInvocation(100000)
  public void iterateArrayList(Iterating<String> iterating) throws Exception {
    iterating.iterateT(arrayList);
  }

  @Benchmark
  @OperationsPerInvocation(100000)
  public void iterateLinkedList(Iterating<String> iterating) throws Exception {
    iterating.iterateT(linkedList);
  }

  @Benchmark
  @OperationsPerInvocation(100000)
  public void iterateCopyOnWriteArrayList(Iterating<String> iterating) throws Exception {
    iterating.iterateT(copyOnWriteArrayList);
  }

  @State(Scope.Thread)
  public static class Iterating<T> {

    void iterateT(Iterable<T> list) {
      for (T ignored : list) {
        Blackhole.consumeCPU(1);
      }
    }

    void iterateT(T[] list) {
      for (T ignored : list) {
        Blackhole.consumeCPU(1);
      }
    }

  }

}