package com.xavax.metrics;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Test case for the Timer class.
 */
public class TimerTest {

  /**
   * Test the constructors.
   */
  @Test
  public void testConstructorts() {
    final long currentTime = System.nanoTime();
    final Timer timer1 = new Timer();
    assertTrue(timer1.getMetric() instanceof TimeMetric);
    assertTrue(timer1.getStartTime() >= currentTime);
    final TimeMetric metric = new TimeMetric();
    final Timer timer2 = new Timer(metric);
    assertEquals(timer2.getMetric(), metric);
    final Timer timer3 = new Timer(timer2);
    assertEquals(timer3.getMetric(), metric);
  }

  /**
   * Test the start method.
   */
  @Test
  public void testStart() {
    final long currentTime = System.nanoTime();
    final Timer timer1 = new Timer();
    timer1.start();
    assertTrue(timer1.getStartTime() >= currentTime);
  }

  /**
   * Test the stop method.
   */
  @Test
  public void testStop() {
    final Timer timer1 = new Timer();
    timer1.start();
    timer1.stop();
    final TimeMetric.Result result = timer1.getMetric().result();
    assertNotNull(result);
    assertEquals(result.count(), 1);
    assertEquals(result.min(), result.max());
  }
}
