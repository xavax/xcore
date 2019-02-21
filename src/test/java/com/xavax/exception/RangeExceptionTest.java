package com.xavax.exception;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Test cases for the RangeException class.
 */
public class RangeExceptionTest {
  private final static int MIN = 1;
  private final static int MAX = 10;
  private final static int VALUE = 11;

  private final RangeException exception =
      new RangeException(MIN, MAX, VALUE);

  /**
   * Test the getAttempted method.
   */
  @Test
  public void testGetAttempted() {
    assertEquals(exception.getAttempted(), VALUE);
  }

  /**
   * Test the getMaximum method.
   */
  @Test
  public void testGetMaximum() {
    assertEquals(exception.getMaximum(), MAX);
  }

  /**
   * Test the getMinimum method.
   */
  @Test
  public void testGetMinimum() {
    assertEquals(exception.getMinimum(), MIN);
  }
}
