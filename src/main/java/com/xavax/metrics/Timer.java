package com.xavax.metrics;

/**
 * Timer keeps track of the start and stop times of a transaction
 * and updates a TimeMetric with the duration of the transaction.
 */
public class Timer {
  private long startTime;
  private TimeMetric metric;

  /**
   * Create a timer with a new TimeMetric.
   */
  public Timer() {
    this(new TimeMetric());
  }

  /**
   * Create a timer with the time metric already in use by
   * another timer.
   *
   * @param timer  the timer to copy.
   */
  public Timer(final Timer timer) {
    this(timer.metric);
  }

  /**
   * Create a timer with an existing TimeMetric.
   *
   * @param metric  the time metric to use with this timer.
   */
  public Timer(final TimeMetric metric) {
    assert(metric != null);
    this.metric = metric;
    startTime = metric.currentTime();
  }

  /**
   * Start timing.
   */
  public void start() {
    startTime = metric.currentTime();
  }

  /**
   * Stop timing, update the time metric, and set the
   * start time to the current time.
   */
  public void stop() {
    final long stopTime = metric.currentTime();
    metric.addTransaction(startTime, stopTime);
    startTime = stopTime;
  }

  /**
   * Returns the TimeMetric used by this timer.
   *
   * @return the TimeMetric used by this timer.
   */
  public TimeMetric getMetric() {
    return metric;
  }

  /**
   * Returns the start time.
   *
   * @return the start time.
   */
  public long getStartTime() {
    return startTime;
  }
}
