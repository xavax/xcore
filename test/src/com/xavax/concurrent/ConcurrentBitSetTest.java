//
// Copyright 2015 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//

package com.xavax.concurrent;

import java.util.Random;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

import com.xavax.exception.RangeException;

import static org.testng.Assert.*;
import static com.xavax.util.Constants.*;

/**
 * Test cases for ConcurrentBitSet.
 */
public class ConcurrentBitSetTest {
  private final static long SMALL_BIT_SET_SIZE = 1 << 20;
  private final static long LARGE_BIT_SET_SIZE = 1 << 32;
  private final static int LOG2_SEGMENT_SIZE = 18;
  private final static int INVALID_SEGMENT_SIZE = 99;
  private final static int RANDOM_BOUNDS = 4000000;
  private final static int RANDOM_COUNT = 1000000;
  private final static String EXPECTED =
      "([" + NULL_INDICATOR + COMMA_SEPARATOR + NULL_INDICATOR;

  private ConcurrentBitSet bitSet;

  /**
   * Set up performed before each test.
   */
  @BeforeMethod
  public void beforeMethod() {
    bitSet = new ConcurrentBitSet(SMALL_BIT_SET_SIZE);
  }

  /**
   * Test the constructors.
   */
  @Test
  public void testConstructors() {
    bitSet = new ConcurrentBitSet(SMALL_BIT_SET_SIZE, LOG2_SEGMENT_SIZE);
    assertNotNull(bitSet);
    bitSet = new ConcurrentBitSet(SMALL_BIT_SET_SIZE);
    assertNotNull(bitSet);
    bitSet = new ConcurrentBitSet();
    assertNotNull(bitSet);
  }

  /**
   * Test the error of having a segment size that is too large.
   */
  @Test(expectedExceptions = RangeException.class)
  public void testSegmentSizeTooLarge() {
    bitSet = new ConcurrentBitSet(SMALL_BIT_SET_SIZE, INVALID_SEGMENT_SIZE);
  }

  /**
   * Test the error of having a negative segment size.
   */
  @Test(expectedExceptions = RangeException.class)
  public void testSegmentSizeNegative() {
    bitSet = new ConcurrentBitSet(SMALL_BIT_SET_SIZE, -1);
  }

  /**
   * Test the error of having the initial size negative.
   */
  @Test(expectedExceptions = RangeException.class)
  public void testInitialSizeNegative() {
    bitSet = new ConcurrentBitSet(-1, LOG2_SEGMENT_SIZE);
  }

  /**
   * Test the getMetrics method.
   */
  @Test
  public void testMetrics() {
    final ConcurrentBitSet.Metrics metrics = bitSet.getMetrics();
    assertEquals(metrics.getPagesCreated(), 0);
    assertEquals(metrics.getSegmentMapLocks(), 0);
    assertEquals(metrics.getSegmentsCreated(), 0);
    assertEquals(metrics.getTotalOperations(), 0);
  }

  /**
   * Test setting a small block of bits.
   */
  @Test
  public void testSmallSet() {
    testSet(SMALL_BIT_SET_SIZE);
  }

  /**
   * Test setting a very large block of bits.
   */
  @Test
  public void testLargeSet() {
    testSet(LARGE_BIT_SET_SIZE);
  }

  /**
   * Test setting a block of max bits.
   *
   * @param max  the number of bits to set.
   */
  private void testSet(final long max) {
    for ( long l = 0; l < max; ++l ) {
      bitSet.set(l, false);
      assertFalse(bitSet.get(l));
      bitSet.set(l, true);
      assertTrue(bitSet.get(l));
    }
    showMetrics();
  }

  /**
   * Test setting random bits then verify that those bits were set.
   */
  @Test
  public void testRandomBits() {
    boolean[] data = new boolean[RANDOM_BOUNDS];
    final Random random = new Random();
    for ( int i = 0; i < RANDOM_COUNT; ++i ) {
      final int index = random.nextInt(RANDOM_BOUNDS);
      data[index] = true;
      bitSet.set(index, true);
    }
    for ( int i = 0; i < RANDOM_BOUNDS; ++i ) {
      assertEquals(data[i], bitSet.get(i));
    }
    showMetrics();
  }

  private void showMetrics() {
    System.out.println("metrics: " + bitSet.getMetrics().toString());
  }

  /**
   * Test the toString method.
   */
  @Test
  public void testToString() {
    final String result = bitSet.toString().substring(0, EXPECTED.length());
    assertEquals(result, EXPECTED);
  }
}
