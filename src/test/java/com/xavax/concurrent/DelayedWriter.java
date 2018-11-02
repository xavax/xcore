package com.xavax.concurrent;

/**
 * Delayed writer fulfills a promise after delay milliseconds.
 *
 * @param <T>  the type of the promised result.
 */
public class DelayedWriter<T> extends Thread {
  private final long delay;
  private final Promise<T> promise;
  private final T result;

  /**
   * Construct a DelayedWriter.
   *
   * @param delay    the delay time in milliseconds.
   * @param promise  the promise.
   * @param result   the promised result.
   */
  DelayedWriter(final long delay, final Promise<T> promise, final T result) {
    this.delay = delay;
    this.promise = promise;
    this.result = result;
  }

  /**
   * Sleep for delay milliseconds then fulfill the promise
   */
  public void run() {
    try {
      Thread.sleep(delay);
    }
    catch (InterruptedException e) {
    }
    promise.set(result);
  }

  /**
   * Fulfill a promise after a delay time.
   *
   * @param delay    the delay time in milliseconds.
   * @param promise  the promise.
   * @param result   the promised result.
   */
  public static <T> void delayedWrite(final long delay,
				      final Promise<T> promise,
				      final T result) {
    final DelayedWriter<T> writer = new DelayedWriter<>(delay, promise, result);
    writer.start();
  }
}