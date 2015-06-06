//
// Copyright 2011 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//
package com.xavax.util;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * IterableEnumeration is an adaptor that allows an Enumeration to
 * implement Iterable so it can be used in a for-each statement.
 * The design was originally described by Dr. Heinz M. Kabutz in
 * the following article.
 *   http://www.javaspecialists.eu/archive/Issue107.html
 */
public class IterableEnumeration<T> implements Iterable<T> {
  private final Enumeration<T> enumeration;

  /**
   * Construct an InterableEnumeration from an existing enumeration.
   */
  public IterableEnumeration(final Enumeration<T> enumeration) {
    this.enumeration = enumeration;
  }

  /**
   * Returns an Iterator for the underlying Enumeration, thereby
   * making it Iterable.
   *
   * @return an Iterator for this enumeration.
   */
  public Iterator<T> iterator() {
    return new Iterator<T>() {
      /**
       * Returns true if this iterator has more items.
       *
       * @return true if this iterator has more items.
       */
      public boolean hasNext() {
        return enumeration.hasMoreElements();
      }

      /**
       * Returns the next item.
       *
       * @return the next item.
       */
      public T next() {
        return enumeration.nextElement();
      }

      /**
       * This method of the Iterator interface is not supported.
       */
      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }

  /**
   * Returns an IterableEnumeration created from the enumeration.
   */
  public static <T> IterableEnumeration<T> create(final Enumeration<T> enumeration) {
    return new IterableEnumeration<T>(enumeration);
  }
}
