package org.jetlang.channels;

import org.jetlang.core.Callback;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * User: mrettig
 * Date: Aug 29, 2009
 */
public class SubscriberListTest {

  @Test
  @Ignore
  public void addAndRemove() {
    SubscriberList<String> list = new SubscriberList<>();
    final List<String> received = new ArrayList<>();
    Callback<String> cb = new Callback<String>() {
      public void onMessage(String message) {
        received.add(message);
      }
    };

    list.add(cb);
    assertEquals(1, list.size());
    list.publish("hello");
    assertEquals(1, received.size());

    assertTrue(list.remove(cb));
    assertEquals(0, list.size());

    list.publish("bye");
    assertEquals(1, received.size());
  }

  @Test
  @Ignore
  public void addAndRemoveWithTwo() {
    SubscriberList<String> list = new SubscriberList<>();
    final List<String> received = new ArrayList<>();
    Callback<String> cb = new Callback<String>() {
      public void onMessage(String message) {
        received.add(message);
      }
    };

    list.add(cb);
    list.add(cb);
    assertEquals(2, list.size());
    list.publish("hello");
    assertEquals(2, received.size());

    assertTrue(list.remove(cb));
    assertEquals(1, list.size());

    list.publish("bye");
    assertEquals(3, received.size());
  }

  static void iterate(Callback<String>[] cbs) {
    for (Callback<String> each : cbs) {
      each.onMessage("hello");
    }
  }

  @Test
  public void perfWithArrayTest() {
    Callback<String>[] list = new Callback[3];
    Callback<String> cb = new Callback<String>() {
      public void onMessage(String message) {
      }
    };

    list[0] = cb;
    list[1] = cb;
    list[2] = cb;

    Long start = System.currentTimeMillis();
    for (int i = 0; i < 100000000; i++) {
      iterate(list);
    }
    System.out.println(System.currentTimeMillis() - start);

  }

  @Test
  public void perfWithLinkedListTest() {
    List<Callback<String>> list = new LinkedList<>();
    Callback<String> cb = new Callback<String>() {
      public void onMessage(String message) {
      }
    };

    list.add(cb);
    list.add(cb);
    list.add(cb);

    Long start = System.currentTimeMillis();
    for (int i = 0; i < 100000000; i++) {
      for (Callback<String> each : list) {
        each.onMessage("hello");
      }
    }
    System.out.println(System.currentTimeMillis() - start);

  }

  @Test
  public void perfWithArrayListTest() {
    List<Callback<String>> list = new ArrayList<>(3);
    Callback<String> cb = new Callback<String>() {
      public void onMessage(String message) {
      }
    };

    list.add(cb);
    list.add(cb);
    list.add(cb);

    Long start = System.currentTimeMillis();
    for (int i = 0; i < 100000000; i++) {
      for (Callback<String> each : list) {
        each.onMessage("hello");
      }
    }
    System.out.println(System.currentTimeMillis() - start);

  }

  @Test
  public void perfWithArrayCopyTest() {
    SubscriberList<String> list = new SubscriberList<>();
    Callback<String> cb = new Callback<String>() {
      public void onMessage(String message) {
      }
    };

    list.add(cb);
    list.add(cb);
    list.add(cb);

    Long start = System.currentTimeMillis();
    for (int i = 0; i < 100000000; i++) {
      list.publish("hello");
    }
    System.out.println(System.currentTimeMillis() - start);

  }

  @Test
  public void perfTestWithCopyOnWrite() {
    CopyOnWriteArrayList<Callback<String>> list = new CopyOnWriteArrayList<>();
    Callback<String> cb = new Callback<String>() {
      public void onMessage(String message) {
      }
    };

    list.add(cb);
    list.add(cb);
    list.add(cb);

    Long start = System.currentTimeMillis();
    for (int i = 0; i < 100000000; i++) {
      for (Callback<String> each : list) {
        each.onMessage("hello");
      }
    }
    System.out.println(System.currentTimeMillis() - start);

  }

}
