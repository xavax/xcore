package com.infoinsightsllc.gambit.pagination;

import com.infoinsightsllc.gambit.GambitException;

import static com.infoinsightsllc.gambit.GambitMessage.INTERRUPTED_PROMISE;

/**
 * InterruptedPromise is thrown when a thread waiting for a promise
 * is interrupted.
 */
public class InterruptedPromiseException extends GambitException {
	private final static long serialVersionUID = 1L;

	private final Promise<?> promise;

	/**
	 * Construct an InterruptedPromiseException.
	 *
	 * @param cause     the cause of this exception.
	 * @param promise   the promise that was interrupted.
	 */
	public InterruptedPromiseException(final Throwable cause, Promise<?> promise) {
		super(cause, INTERRUPTED_PROMISE, promise.getName());
		this.promise = promise;
	}

	/**
	 * Returns the promise that was interrupted.
	 *
	 * @return the promise that was interrupted.
	 */
	public Promise<?> getPromise() {
		return promise;
	}
}
