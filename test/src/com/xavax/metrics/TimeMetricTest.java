package com.xavax.metrics;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.xavax.metrics.TimeMetric;

import static org.testng.Assert.*;

public class TimeMetricTest {
  private final static long START_TIME = 1000;
  private final static long STOP_TIME = 1100;
  private final static long SCALE_FACTOR = 100;

  private TimeMetric metric;

  @BeforeMethod
  public void setup() {
    metric = new TimeMetric();
  }

  @Test
  public void testResult() {
    TimeMetric.Result result = metric.result();
    assertNotNull(result);
  }

  @Test
  public void testAddTransaction() {
    metric.addTransaction(START_TIME, STOP_TIME);
    metric.addTransaction(START_TIME, STOP_TIME + 1);
    metric.addTransaction(START_TIME, STOP_TIME + 2);
    metric.addTransaction(START_TIME, STOP_TIME + 3);
    metric.addTransaction(START_TIME, STOP_TIME + 4);
    TimeMetric.Result result = metric.result();
    assertEquals(result.count(), 5);
  }

  @Test
  public void testToString() {
    metric.addTransaction(START_TIME, STOP_TIME);
    TimeMetric.Result result = metric.result();
    result = metric.result();
    assertEquals(result.toString(), "(1, 100, 100, 100.00, 0.00)");
    metric.addTransaction(START_TIME, STOP_TIME + 1);
    metric.addTransaction(START_TIME, STOP_TIME + 2);
    metric.addTransaction(START_TIME, STOP_TIME + 3);
    metric.addTransaction(START_TIME, STOP_TIME + 4);
    result = metric.result();
    assertEquals(result.toString(), "(5, 100, 104, 102.00, 1.41)");
  }

  @Test
  public void testScaleFactor() {
    metric = new TimeMetric(SCALE_FACTOR);
    metric.addTransaction(START_TIME, STOP_TIME);
    TimeMetric.Result result = metric.result();
    assertEquals(result.min(), 1);
  }
}
