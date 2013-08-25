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
import java.util.List;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.TreeMap;
import java.util.Vector;
import java.util.WeakHashMap;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

import static org.testng.Assert.*;

public class TestCollectionFactory {

  @BeforeMethod
  public void setUp() {
  }

  @Test
  public void testArrayListFactory() {
    List<String> l = CollectionFactory.arrayList();
    assertTrue(l instanceof ArrayList);
  }

  @Test
  public void testHashMapFactory() {
    Map<String, String> map = CollectionFactory.hashMap();
    assertTrue(map instanceof HashMap);
  }

  @Test
  public void testHashSetFactory() {
    Set<String> set = CollectionFactory.hashSet();
    assertTrue(set instanceof HashSet);
  }

  @Test
  public void testIterableEnumerationFactory() {
    String[] sa = new String[] {};
    List<String> l = Arrays.asList(sa);
    Enumeration<String> en = Collections.enumeration(l);
    IterableEnumeration<String> ie =
	CollectionFactory.iterableEnumeration(en);
    assertTrue(ie instanceof IterableEnumeration);
  }

  @Test
  public void testLinkedListFactory() {
    List<String> l = CollectionFactory.linkedList();
    assertTrue(l instanceof LinkedList);
  }

  @Test
  public void testLinkedHashMapFactory() {
    Map<String, String> map = CollectionFactory.linkedHashMap();
    assertTrue(map instanceof LinkedHashMap);
  }

  @Test
  public void testLinkedHashSetFactory() {
    Set<String> set = CollectionFactory.linkedHashSet();
    assertTrue(set instanceof LinkedHashSet);
  }

  @Test
  public void testPriorityQueueFactory() {
    Queue<String> queue = CollectionFactory.priorityQueue();
    assertTrue(queue instanceof PriorityQueue);
  }

  @Test
  public void testStackFactory() {
    Stack<String> stack = CollectionFactory.stack();
    assertTrue(stack instanceof Stack);
  }

  @Test
  public void testTreeMapFactory() {
    Map<String, String> map = CollectionFactory.treeMap();
    assertTrue(map instanceof TreeMap);
  }

  @Test
  public void testTreeSetFactory() {
    Set<String> set = CollectionFactory.treeSet();
    assertTrue(set instanceof TreeSet);
  }

  @Test
  public void testVectorFactory() {
    Vector<String> v = CollectionFactory.vector();
    assertTrue(v instanceof Vector);
  }

  @Test
  public void testWeakHashMap() {
    Map<String, String> map = CollectionFactory.weakHashMap();
    assertTrue(map instanceof WeakHashMap);
  }
}
