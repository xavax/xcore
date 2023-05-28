package com.xavax.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test cases for XTestUtils class.
 */
public class TimersTest {
  private final static long SLEEP_INTERVAL = 1000;

  /**
   * Test the sleep method.
   */
  @Test
  public void sleep() {
    final long time1 = System.currentTimeMillis();
    Timers.sleep(SLEEP_INTERVAL);
    final long time2 = System.currentTimeMillis();
    assertTrue(time2 > time1 + SLEEP_INTERVAL - 1);
  }
}
