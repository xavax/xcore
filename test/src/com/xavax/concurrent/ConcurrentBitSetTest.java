//
// Copyright 2015 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//

package com.xavax.concurrent;

import java.util.Random;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

import com.xavax.concurrent.ConcurrentBitSet;
import com.xavax.exception.RangeException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertNotNull;

public class ConcurrentBitSetTest {
  private final static long SMALL_BIT_SET_SIZE = 1 << 20;
  private final static long LARGE_BIT_SET_SIZE = 1 << 32;
  private final static int LOG2_SEGMENT_SIZE = 18;
  private final static int INVALID_SEGMENT_SIZE = 99;
  private final static int RANDOM_BOUNDS = 4000000;
  private final static int RANDOM_COUNT = 1000000;
  private final static String EXPECTED = "[null, null";

  private ConcurrentBitSet bitSet;

  @BeforeMethod
  public void beforeMethod() {
    bitSet = new ConcurrentBitSet(SMALL_BIT_SET_SIZE);
  }

  @Test
  public void testConstructors() {
    bitSet = new ConcurrentBitSet(SMALL_BIT_SET_SIZE, LOG2_SEGMENT_SIZE);
    assertNotNull(bitSet);
    bitSet = new ConcurrentBitSet(SMALL_BIT_SET_SIZE);
    assertNotNull(bitSet);
    bitSet = new ConcurrentBitSet();
    assertNotNull(bitSet);
  }

  @Test(expectedExceptions = RangeException.class)
  public void testSegmentSizeTooLarge() {
    bitSet = new ConcurrentBitSet(SMALL_BIT_SET_SIZE, INVALID_SEGMENT_SIZE);
  }

  @Test(expectedExceptions = RangeException.class)
  public void testSegmentSizeNegative() {
    bitSet = new ConcurrentBitSet(SMALL_BIT_SET_SIZE, -1);
  }

  @Test(expectedExceptions = RangeException.class)
  public void testInitialSizeNegative() {
    bitSet = new ConcurrentBitSet(-1, LOG2_SEGMENT_SIZE);
  }

  @Test
  public void testMetrics() {
    ConcurrentBitSet.Metrics metrics = bitSet.getMetrics();
    assertEquals(metrics.getPagesCreated(), 0);
    assertEquals(metrics.getSegmentMapLocks(), 0);
    assertEquals(metrics.getSegmentsCreated(), 0);
    assertEquals(metrics.getTotalOperations(), 0);
  }

  @Test
  public void testSmallSet() {
    testSet(SMALL_BIT_SET_SIZE);
  }

  @Test
  public void testLargeSet() {
    testSet(LARGE_BIT_SET_SIZE);
  }

  private void testSet(long max) {
    for ( long l = 0; l < max; ++l ) {
      bitSet.set(l, false);
      assertFalse(bitSet.get(l));
      bitSet.set(l, true);
      assertTrue(bitSet.get(l));
    }
    showMetrics();
  }

  @Test
  public void testRandomBits() {
    boolean[] data = new boolean[RANDOM_BOUNDS];
    Random random = new Random();
    for ( int i = 0; i < RANDOM_COUNT; ++i ) {
      int index = random.nextInt(RANDOM_BOUNDS);
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

  @Test
  public void testToString() {
    String s = bitSet.toString().substring(0, EXPECTED.length());
    assertEquals(s, EXPECTED);
  }
}
