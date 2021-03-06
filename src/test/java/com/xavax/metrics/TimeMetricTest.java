package com.xavax.metrics;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Test cases for the TimeMetric class.
 */
public class TimeMetricTest {
  private final static long START_TIME = 1000;
  private final static long STOP_TIME = 1100;
  private final static long SCALE_FACTOR = 100;

  private TimeMetric metric;

  /**
   * Test setup.
   */
  @BeforeMethod
  public void setUp() {
    metric = new TimeMetric();
  }

  /**
   * Test the result method.
   */
  @Test
  public void testResult() {
    final TimeMetric.Result result = metric.result();
    assertNotNull(result);
  }

  /**
   * Test the addTransaction method.
   */
  @Test
  public void testAddTransaction() {
    metric.addTransaction(START_TIME, STOP_TIME);
    metric.addTransaction(START_TIME, STOP_TIME + 1);
    metric.addTransaction(START_TIME, STOP_TIME + 2);
    metric.addTransaction(START_TIME, STOP_TIME + 3);
    metric.addTransaction(START_TIME);
    final TimeMetric.Result result = metric.result();
    assertEquals(result.count(), 5);
  }

  /**
   * Test the toString method.
   */
  @Test
  public void testToString() {
    metric.addTransaction(START_TIME, STOP_TIME);
    TimeMetric.Result result = metric.result();
    result = metric.result();
    assertEquals(result.toString(), "(1, 100, 100, 100.00, 0.00)");
    metric.addTransaction(START_TIME, STOP_TIME + 1);
    metric.addTransaction(START_TIME, STOP_TIME + 2);
    metric.addTransaction(START_TIME, STOP_TIME + 4);
    metric.addTransaction(START_TIME, STOP_TIME + 3);
    result = metric.result();
    assertEquals(result.toString(), "(5, 100, 104, 102.00, 1.41)");
    assertEquals(result.toString(), metric.toString());
  }

  /**
   * Test the scaleFactor method.
   */
  @Test
  public void testScaleFactor() {
    metric = new TimeMetric(SCALE_FACTOR);
    metric.addTransaction(START_TIME, STOP_TIME);
    final TimeMetric.Result result = metric.result();
    assertEquals(result.min(), 1);
  }

  /**
   * Test the reset method.
   */
  @Test
  public void testReset() {
    metric.addTransaction(START_TIME);
    metric.addTransaction(START_TIME);
    metric.reset();
    metric.addTransaction(START_TIME);
    metric.addTransaction(START_TIME);
    final TimeMetric.Result result = metric.result();
    assertEquals(result.count(), 2);
  }

  /**
   * Test statistics.
   */
  @Test
  public void testStatistics() {
    metric.addTransaction(START_TIME, STOP_TIME);
    metric.addTransaction(START_TIME, STOP_TIME + 1);
    metric.addTransaction(START_TIME, STOP_TIME + 2);
    metric.addTransaction(START_TIME, STOP_TIME + 4);
    metric.addTransaction(START_TIME, STOP_TIME + 3);
    final TimeMetric.Result result = metric.result();
    assertEquals(result.min(), 100);
    assertEquals(result.max(), 104);
    assertEquals(result.mean(), 102.0);
    assertEquals(result.totalTime(), 510);
    assertEquals(result.totalTimeSquared(), 52030);
    assertEquals(result.deviation(), 1.4142135623730951);
  }
}
