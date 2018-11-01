//
// Copyright 2018 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//
package com.xavax.concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import com.infoinsightsllc.gambit.GambitException;

import com.xavax.util.AbstractJoinableObject;
import com.xavax.util.Joiner;

import static com.infoinsightsllc.gambit.GambitConstants.*;
import static com.infoinsightsllc.gambit.GambitMessage.INTERRUPTED_PROMISE;

public class BasicPromise<T> extends AbstractJoinableObject implements Promise<T> {
	private final static String SENTINEL_FIELD = "sentinel";

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
	 * @param name  the name or description of this promise.
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
		boolean readyFlag = false;
		sentinel.lock();
		readyFlag = ready;
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
			if ( !ready ) {
				if ( timeout == 0 ) {
					condition.await();
				}
				else {
					condition.awaitNanos(timeout);
				}
			}
			rvalue = result;
		}
		catch (InterruptedException e) {
			interrupted = true;
			GambitException.handle(e, INTERRUPTED_PROMISE, name);
		}
		finally {
			sentinel.unlock();
		}
		return rvalue;
	}

	/**
	 * Sets the result making it available and waking up any waiting threads.
	 *
	 * @param result  the result.
	 */
	public void set(final T result) {
		sentinel.lock();
		this.result = result;
		ready = true;
	    condition.signalAll();
	    sentinel.unlock();
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
	 * @param joiner  the joiner to use for output.
	 * @return this joiner.
	 */
	@Override
	protected Joiner doJoin(Joiner joiner) {
		joiner.append(NAME_FIELD, name)
			  .append(READY_FIELD, ready)
			  .append(RESULT_FIELD, result)
			  .append(SENTINEL_FIELD, sentinel);
		return joiner;
	}

}
