//
// Copyright 2018 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//
package com.xavax.concurrent;

/**
 * Promise is an interface for a promise to return a result that
 * may not yet be calculated. On any attempt to access the result,
 * implementations of Promise wait until the result is available.
 *
 * @param <T>  the type of the result.
 */
public interface Promise<T> {
  /**
   * Returns the result of type T, waiting if necessary.
   *
   * @return the result of type T.
   */
  T get();

  /**
   * Returns the name or description of this promise.
   *
   * @return the name or description of this promise.
   */
  String getName();

  /**
   * Returns true if the result of this promise is ready.
   *
   * @return true if the result of this promise is ready.
   */
  boolean isReady();

  /**
   * Sets the result making it available.
   *
   * @param result  the result.
   */
  void set(final T result);

  /**
   * Returns true if the promise was interrupted.
   *
   * @return true if the promise was interrupted.
   */
  boolean wasInterrupted();
}
