//
// Copyright 2018 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//
package com.xavax.concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.xavax.util.AbstractJoinableObject;
import com.xavax.util.Joiner;

/**
 * BasicPromise<T> is a promise of a future value of type T.
 *
 * @param <T>  the class of object promised.
 */
public class BasicPromise<T> extends AbstractJoinableObject implements Promise<T> {
  private final static String NAME = "name";
  private final static String READY = "ready";
  private final static String RESULT = "result";
  private final static String SENTINEL = "sentinel";

  private boolean ready;
  private boolean interrupted;
  private long timeout;
  private final Condition condition;
  private final Lock sentinel;
  private final String name;
  private T result;

  /**
   * Construct a BasicPromise.
   *
   * @param name the name or description of this promise.
   */
  public BasicPromise(final String name) {
    this(name, 0);
  }

  /**
   * Construct a BasicPromise with a maximum timeout value.
   *
   * @param name     the name or description of this promise.
   * @param timeout  the maximum time in nanoseconds to wait for this promise.
   */
  public BasicPromise(final String name, final long timeout) {
    this.name = name;
    this.timeout = timeout;
    interrupted = false;
    ready = false;
    sentinel = new ReentrantLock();
    condition = sentinel.newCondition();
  }

  /**
   * Returns true if the result of this promise is ready.
   *
   * @return true if the result of this promise is ready.
   */
  public boolean isReady() {
    sentinel.lock();
    final boolean readyFlag = ready;
    sentinel.unlock();
    return readyFlag;
  }

  /**
   * Returns the name or description of this promise.
   *
   * @return the name or description of this promise.
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the result of type T, waiting if necessary.
   */
  public T get() {
    T rvalue = result;
    sentinel.lock();
    try {
      boolean first = true;
      while ( first && !ready ) {
	if ( timeout == 0 ) {
	  condition.await();
	}
	else {
	  condition.awaitNanos(timeout);
	}
	first = false;
      }
      rvalue = result;
    }
    catch (InterruptedException e) {
      interrupted = true;
      // TODO: handle(e, INTERRUPTED_PROMISE, name);
    }
    finally {
      sentinel.unlock();
    }
    return rvalue;
  }

  /**
   * Sets the result making it available and waking up any waiting threads.
   *
   * @param result the result.
   */
  public void set(final T result) {
    try {
      sentinel.lock();
      this.result = result;
      ready = true;
      condition.signalAll();
    }
    finally {
      sentinel.unlock();
    }
  }

  /**
   * Returns true if the promise was interrupted.
   *
   * @return true if the promise was interrupted.
   */
  public boolean wasInterrupted() {
    return interrupted;
  }

  /**
   * Output this object to the specified joiner.
   *
   * @param joiner the joiner to use for output.
   * @return this joiner.
   */
  @Override
  protected Joiner doJoin(final Joiner joiner) {
    joiner.append(NAME, name)
	  .append(READY, ready)
	  .append(RESULT, result)
	  .append(SENTINEL, sentinel);
    return joiner;
  }
}
