package com.xavax.exception;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test cases for the RangeException class.
 */
public class RangeExceptionTest {
  private final static int MIN = 1;
  private final static int MAX = 10;
  private final static int VALUE = 11;
  private final static String NAME = "param";

  private final RangeException exception =
      new RangeException(MIN, MAX, VALUE, NAME);

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

  /**
   * Test the getName method.
   */
  @Test
  public void testGetName() {
    assertEquals(exception.getName(), NAME);
  }
}
