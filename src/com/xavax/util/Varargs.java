package com.xavax.util;

import java.util.Collection;
import java.util.List;

/**
 * Varargs is a utility class for supporting methods with variable
 * arguments signatures.
 */
public class Varargs {
  private final static int DEFAULT_CAPACITY = 16;

  private List<Object> list;

  /**
   * Construct a Varargs.
   */
  public Varargs() {
    this(DEFAULT_CAPACITY);
  }

  /**
   * Construct a Varargs with the specified initial capacity.
   *
   * @param capacity  the initial capacity of the container.
   */
  public Varargs(final int capacity) {
    list = CollectionFactory.arrayList(capacity);
  }

  /**
   * Returns a new Varargs instance.
   *
   * @return a new Varargs instance.
   */
  public static Varargs getInstance() {
    return new Varargs();
  }

  /**
   * Returns a new Varargs instance with the specified capacity.
   *
   * @param capacity  the initial capacity of the container.
   * @return a new Varargs instance.
   */
  public static Varargs getInstance(final int capacity) {
    return new Varargs(capacity);
  }

  /**
   * Appends a parameter to the list.
   *
   * @param parameter  the parameter to append.
   * @return this Varargs object.
   */
  public Varargs append(final Object parameter) {
    list.add(parameter);
    return this;
  }

  /**
   * Appends an array to the list as a single parameter.
   *
   * @param parameter  the parameter to append.
   * @return this Varargs object.
   */
  public Varargs append(final Object[] parameter) {
    list.add(parameter);
    return this; 
  }

  /**
   * Appends a collection to the list as a single parameter.
   * 
   * @param collection  the parameter to append.
   * @return this Varargs object.
   */
  public Varargs append(final Collection<?> collection) {
    list.add(collection);
    return this; 
  }

  /**
   * Appends an array of parameters to the list as individual parameters.
   *
   * @param parameters  the array of parameters to append.
   * @return this Varargs object.
   */
  public Varargs flatten(final Object[] parameters) {
    for ( final Object object : parameters ) {
      list.add(object);
    }
    return this;
  }

  /**
   * Appends an collection of parameters to the list as individual parameters.
   *
   * @param parameters  the collection of parameters to append.
   * @return this Varargs object.
   */
  public Varargs flatten(final Collection<?> collection) {
    for ( final Object object : collection ) {
      list.add(object);
    }
    return this;
  }

  /**
   * Returns the list of arguments as an array of objects.
   *
   * @return the list of arguments as an array of objects.
   */
  public Object[] toArray() {
    return list.toArray();
  }
}
