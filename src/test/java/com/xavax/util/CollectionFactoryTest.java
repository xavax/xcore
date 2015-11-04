//
// Copyright 2011 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//
package com.xavax.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.TreeSet;
import java.util.TreeMap;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Test cases for CollectionFactory.
 */
public class CollectionFactoryTest {
  public final static int INITIAL_CAPACITY = 32;

  /**
   * Test the arrayList method.
   */
  @Test
  public void testArrayListFactory() {
    assertTrue(CollectionFactory.arrayList() instanceof ArrayList);
    assertTrue(CollectionFactory.arrayList(INITIAL_CAPACITY) instanceof ArrayList);
  }

  /**
   * Test the concurrentLinkedQueue method.
   */
  @Test
  public void testConcurrentLinkedQueueFactory() {
    assertTrue(CollectionFactory.concurrentLinkedQueue() instanceof ConcurrentLinkedQueue);
  }

  /**
   * Test the hashMap method.
   */
  @Test
  public void testHashMapFactory() {
    assertTrue(CollectionFactory.hashMap() instanceof HashMap);
  }

  /**
   * Test the hashSet method.
   */
  @Test
  public void testHashSetFactory() {
    assertTrue(CollectionFactory.hashSet() instanceof HashSet);
  }

  /**
   * Test the iterableEnumeration method.
   */
  @Test
  public void testIterableEnumerationFactory() {
    final Enumeration<String> enumeration = Collections.enumeration(Arrays.asList(new String[] {}));
    assertTrue(CollectionFactory.iterableEnumeration(enumeration) instanceof IterableEnumeration);
  }

  /**
   * Test the linkedList method.
   */
  @Test
  public void testLinkedListFactory() {
    assertTrue(CollectionFactory.linkedList() instanceof LinkedList);
  }

  /**
   * Test the linkedHashMap method.
   */
  @Test
  public void testLinkedHashMapFactory() {
    assertTrue(CollectionFactory.linkedHashMap() instanceof LinkedHashMap);
  }

  /**
   * Test the linkedHashSet method.
   */
  @Test
  public void testLinkedHashSetFactory() {
    assertTrue(CollectionFactory.linkedHashSet() instanceof LinkedHashSet);
  }

  /**
   * Test the priorityQueue method.
   */
  @Test
  public void testPriorityQueueFactory() {
    assertTrue(CollectionFactory.priorityQueue() instanceof PriorityQueue);
  }

  /**
   * Test the stack method.
   */
  @Test
  public void testStackFactory() {
    assertTrue(CollectionFactory.stack() instanceof Stack);
  }

  /**
   * Test the treeMap method.
   */
  @Test
  public void testTreeMapFactory() {
    assertTrue(CollectionFactory.treeMap() instanceof TreeMap);
  }

  /**
   * Test the treeMap method.
   */
  @Test
  public void testTreeMapFactoryWithComparator() {
    assertTrue(CollectionFactory.treeMap(Strings.comparator()) instanceof TreeMap);
  }

  /**
   * Test the treeSet method.
   */
  @Test
  public void testTreeSetFactory() {
    assertTrue(CollectionFactory.treeSet() instanceof TreeSet);
  }

  /**
   * Test the weakHashMap method.
   */
  @Test
  public void testWeakHashMap() {
    assertTrue(CollectionFactory.weakHashMap() instanceof WeakHashMap);
  }

}
