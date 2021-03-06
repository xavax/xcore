//
// Copyright 2013 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//
package com.xavax.metrics;

import java.util.concurrent.atomic.AtomicLong;

/**
 * TimeMetric is used to keep statistics for operations measured in integral
 * units of time (usually milliseconds or nanoseconds). This object is not
 * tied to a specific unit of measure but the start and stop times passed to
 * addTransaction should use consistent units of measure.
 *
 * @author alvitar@xavax.com
 */
public class TimeMetric {

  public final static long SCALE_BY_NANOSECONDS  = 1;
  public final static long SCALE_BY_MICROSECONDS = 1000;
  public final static long SCALE_BY_MILLISECONDS = 1000000;

  private long min;
  private long max;
  private long scaleFactor;
  private AtomicLong count;
  private AtomicLong totalTime;
  private AtomicLong totalTimeSquared;

  /**
   * Construct a TimeMetric object.
   */
  public TimeMetric() {
    this(SCALE_BY_NANOSECONDS);
  }

  /**
   * Construct a TimeMetric with the specified scale factor.
   *
   * @param scaleFactor  the scale factor for scaling elapsed times.
   */
  public TimeMetric(final long scaleFactor) {
    this.scaleFactor = scaleFactor;
    min = Long.MAX_VALUE;
    max = 0;
    count = new AtomicLong();
    totalTime = new AtomicLong();
    totalTimeSquared = new AtomicLong();
  }

  /**
   * Add a transaction to this metric with the specified start
   * and stop times.
   *
   * @param start  the start time.
   * @param stop   the stop time.
   */
  public void addTransaction(final long start, final long stop) {
    long elapsed = stop - start;
    if ( scaleFactor != SCALE_BY_NANOSECONDS ) {
      elapsed /= scaleFactor;
    }
    synchronized ( this ) {
      if ( elapsed < min ) {
	min = elapsed;
      }
      if ( elapsed > max ) {
	max = elapsed;
      }
      count.incrementAndGet();
      totalTime.addAndGet(elapsed);
      totalTimeSquared.addAndGet(elapsed * elapsed);
    }
  }

  /**
   * Add a transaction to this metric with the specified start
   * time. The stop time is determined by calling System.nanoTime.
   *
   * @param start  the start time.
   */
  public void addTransaction(final long start) {
    addTransaction(start, currentTime());
  }

  /**
   * Returns the time in nanoseconds as produced by System.nanoTime.
   * Override this method to use a different time base.
   *
   * @return the current time in nanoseconds
   */
  public long currentTime() {
    return System.nanoTime();
  }

  /**
   * Reset this metric.
   */
  public void reset() {
    synchronized ( this ) {
      min = Long.MAX_VALUE;
      max = 0;
      count.set(0);
      totalTime.set(0);
      totalTimeSquared.set(0);
    }
  }

  /**
   * Return a snapshot of the results for this metric.
   *
   * @return  a snapshot of the results for this metric.
   */
  public Result result() {
    Result result = null;
    synchronized ( this ) {
      result = new Result(this);
    }
    return result;
  }

  /**
   * Return a string representation of this result.
   *
   * @return a string representation of this result.
   */
  public String toString() {
    return result().toString();
  }

  /**
   * Result represents the results gathered by a TimeMetric object.
   */
  public static class Result {
    private final long count;
    private final long min;
    private final long max;
    private final long totalTime;
    private final long totalTimeSquared;
    private double mean;
    private double deviation;

    /**
     * Construct a Result for a TimeMetric object.
     *
     * @param timeMetric  the TimeMetric object to be examined.
     */
    @SuppressWarnings("PMD.AccessorMethodGeneration")
    public Result(final TimeMetric timeMetric) {
      this.min = timeMetric.min;
      this.max = timeMetric.max;
      this.count = timeMetric.count.get();
      this.totalTime = timeMetric.totalTime.get();
      this.totalTimeSquared = timeMetric.totalTimeSquared.get();
      if ( count > 0 ) {
	this.mean = (double) totalTime / (double) count;
	this.deviation = Math.sqrt((double) totalTimeSquared/count - mean * mean);
      }
      else {
	this.mean = 0;
	this.deviation = 0;
      }
    }

    /**
     * Return the transaction count.
     *
     * @return the transaction count.
     */
    public long count() {
      return this.count;
    }

    /**
     * Return the total elapsed time for all transactions.
     *
     * @return the total elapsed time for all transactions.
     */
    public long totalTime() {
      return this.totalTime;
    }

    /**
     * Return the total elapsed time squared for all transactions.
     *
     * @return the total elapsed time squared for all transactions.
     */
    public long totalTimeSquared() {
      return this.totalTimeSquared;
    }

    /**
     * Return the minimum transaction time.
     *
     * @return the minimum transaction time.
     */
    public long min() {
      return this.min;
    }

    /**
     * Return the maximum transaction time.
     *
     * @return the maximum transaction time.
     */
    public long max() {
      return this.max;
    }

    /**
     * Return the mean elapsed time for all transactions.
     *
     * @return the mean elapsed time for all transactions.
     */
    public double mean() {
      return this.mean;
    }

    /**
     * Return the standard deviation for all transactions.
     *
     * @return the standard deviation for all transactions.
     */
    public double deviation() {
      return this.deviation;
    }

    /**
     * Return a string representation of this result.
     *
     * @return a string representation of this result.
     */
    public String toString() {
      final String format = "(%d, %d, %d, %.2f, %.2f)";
      return String.format(format, count, min, max, mean, deviation);
    }
  }
}
