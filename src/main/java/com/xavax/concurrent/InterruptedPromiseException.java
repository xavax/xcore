package com.xavax.concurrent;

/**
 * InterruptedPromise is thrown when a thread waiting for a promise is
 * interrupted.
 */
public class InterruptedPromiseException extends RuntimeException {
  private final static long serialVersionUID = 1L;
  private final Promise<?> promise;

  /**
   * Construct an InterruptedPromiseException.
   *
   * @param cause    the cause of this exception.
   * @param promise  the promise that was interrupted.
   */
  public InterruptedPromiseException(final Throwable cause,
				     final Promise<?> promise) {
    super(cause);
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
