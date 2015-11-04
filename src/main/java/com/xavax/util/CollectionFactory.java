//
// Copyright 2010 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//

package com.xavax.util;

import java.util.ArrayList;
import java.util.Comparator;
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
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * CollectionFactory creates new collections from the Collections library.
 * This reduces imports and in pre-Java7 code eliminates the need to
 * specify the type parameters on the right side of a statement.
 *
 * @author alvitar@xavax.com
 */
public final class CollectionFactory {

  /**
   * Private constructor provided to keep the compiler from generating
   * a public default constructor.
   */
  private CollectionFactory() {}

  /**
   * Returns a new array list.
   *
   * @param <T>  the list element type.
   * @return a new array list.
   */
  public static <T> List<T> arrayList()
  {
    return new ArrayList<T>();
  }

  /**
   * Returns a new array list with the specified initial capacity.
   *
   * @param <T>  the list element type.
   * @param capacity  the initial capacity of the list.
   * @return a new array list.
   */
  public static <T> List<T> arrayList(final int capacity)
  {
    return new ArrayList<T>(capacity);
  }

  /**
   * Returns a new concurrent linked queue.
   *
   * @param <T>  the queue element type.
   * @return a new concurrent linked queue.
   */
  public static <T> ConcurrentLinkedQueue<T> concurrentLinkedQueue() {
    return new ConcurrentLinkedQueue<T>();
  }

  /**
   * Returns a new hash map.
   *
   * @param <K>  the map key type.
   * @param <V>  the map value type.
   * @return a new hash map.
   */
  public static <K, V> Map<K, V> hashMap()
  {
    return new HashMap<K, V>();
  }

  /**
   * Returns a new hash set.
   *
   * @param <T>  the hash set element type.
   * @return a new hash set.
   */
  public static <T> Set<T> hashSet()
  {
    return new HashSet<T>();
  }

  /**
   * Returns a new linked hash map.
   *
   * @param <K>  the map key type.
   * @param <V>  the map value type.
   * @return a new linked hash map.
   */
  public static <K, V> Map<K, V> linkedHashMap() {
    return new LinkedHashMap<K, V>();
  }

  /**
   * Returns a new linked hash set.
   *
   * @param <E>  the set element type.
   * @return a new linked hash set.
   */
  public static <E> Set<E> linkedHashSet() {
    return new LinkedHashSet<E>();
  }

  /**
   * Returns a new linked list.
   *
   * @param <T>  the list element type.
   * @return a new linked list.
   */
  public static <T> List<T> linkedList()
  {
    return new LinkedList<T>();
  }

  /**
   * Returns a new priority queue.
   *
   * @param <T>  the queue element type.
   * @return a new priority queue.
   */
  public static <T> Queue<T> priorityQueue() {
    return new PriorityQueue<T>();
  }

  /**
   * Returns a new stack.
   *
   * @param <T>  the stack value type.
   * @return a new stack.
   */
  public static <T> Stack<T> stack() {
    return new Stack<T>();
  }

  /**
   * Returns a new tree map.
   *
   * @param <K>  the map key type.
   * @param <V>  the map value type.
   * @return a new tree map.
   */
  public static <K, V> Map<K,V> treeMap()
  {
    return new TreeMap<K,V>();
  }

  /**
   * Returns a new tree map.
   *
   * @param <K>  the map key type.
   * @param <V>  the map value type.
   * @param comparator  the comparator used to compare keys.
   * @return a new tree map.
   */
  public static <K, V> Map<K,V> treeMap(final Comparator<K> comparator)
  {
    return new TreeMap<K,V>(comparator);
  }

  /**
   * Returns a new tree set.
   *
   * @param <E>  the set element type.
   * @return a new tree set.
   */
  public static <E> Set<E> treeSet() {
    return new TreeSet<E>();
  }

  /**
   * Returns a new weak hash map.
   *
   * @param <K>  the map key type.
   * @param <V>  the map value type.
   * @return a new weak hash map.
   */
  public static <K, V> Map<K, V> weakHashMap() {
    return new WeakHashMap<K, V>();
  }

  /**
   * Returns a new iterable enumeration.
   *
   * @param <T>  the element type.
   * @param enumeration  the enumeration to be iterated.
   * @return a new iterable enumeration.
   */
  public static <T> IterableEnumeration<T>
    iterableEnumeration(final Enumeration<T> enumeration) {
    return new IterableEnumeration<T>(enumeration);
  }
}
