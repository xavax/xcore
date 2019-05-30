//
// Copyright 2015 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//

package com.xavax.concurrent;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

import static org.testng.Assert.*;

/**
 * Test the ConcurrentBitSet.Segment class.
 */
public class SegmentTest {
  private final static String EXPECTED = "pageCount: 0, pages: [<null>, <null>,";

  private ConcurrentBitSet bitSet;
  private BitSetSegment segment;

  /**
   * Set up performed before each test.
   */
  @BeforeMethod
  public void setUp() {
    bitSet = new ConcurrentBitSet();
    segment = (BitSetSegment) bitSet.getSegment(0, true);
  }

  /**
   * Test the getPage method.
   */
//  @Test
  public void testGetPage() {
    assertEquals(segment.pageCount(), 0);
    final int maxIndex = bitSet.getPageSize();
    for ( int index = 0; index < maxIndex; ++index ) {
      final int bitIndex = bitSet.indexToBitIndex(index);
      BitSetPage entry = segment.getPageContaining(bitIndex, false);
      assertNull(entry);
      assertEquals(segment.pageCount(), index);
      entry = segment.getPageContaining(bitIndex, true);
      assertNotNull(entry);
      assertEquals(segment.pageCount(), index + 1);
      entry = segment.getPageContaining(bitIndex, true);
      assertNotNull(entry);
      assertEquals(segment.pageCount(), index + 1);
    }
  }

  /**
   * Test the get and set methods.
   */
  @Test
  public void testGetSet() {
    assertEquals(segment.pageCount(), 0);
    final int maxIndex = bitSet.getPageSize();
    for ( int index = 0; index < maxIndex; ++index ) {
      assertFalse(segment.get(index));
      segment.set(index, false);
      assertFalse(segment.get(index));
      segment.set(index, true);
      assertTrue(segment.get(index));
      segment.set(index, false);
      assertFalse(segment.get(index));
    }
  }

  /**
   * Test the toString method.
   */
//  @Test
  public void testToString() {
    final String result = segment.toString();
    assertEquals(result.substring(0, EXPECTED.length()), EXPECTED);
  }

  /**
   * Test the set multiple bits method.
   */
//  @Test
  public void testSetBits() {
    segment.set(0,2222);
    assertTrue(segment.get(2221));
    assertTrue(segment.get(2222));
    assertFalse(segment.get(2223));
    assertFalse(segment.get(2224));
  }

  /**
   * Test the clear multiple bits method.
   */
//  @Test
  public void testClearBits() {
    segment.set(0,4095);
    segment.clear(555,2222);
    assertTrue(segment.get(553));
    assertTrue(segment.get(554));
    assertFalse(segment.get(555));
    assertFalse(segment.get(556));
    assertFalse(segment.get(2221));
    assertFalse(segment.get(2222));
    assertTrue(segment.get(2223));
    assertTrue(segment.get(2224));
  }

  /**
   * Test the nextSetBit method.
   */
//  @Test
  public void testNextSetBit() {
    segment.set(0, true);
    segment.set(1111, true);
    segment.set(2222, true);
    segment.set(3333, true);
    segment.set(4444, true);
    segment.set(5555, true);
    segment.set(6666, true);
    long next = segment.nextSetBit(0);
    assertEquals(next, 0);
    next = segment.nextSetBit(1);
    assertEquals(next, 1111);
    assertEquals(segment.nextSetBit(1112), 2222);
    assertEquals(segment.nextSetBit(2223), 3333);
    assertEquals(segment.nextSetBit(3334), 4444);
    assertEquals(segment.nextSetBit(4445), 5555);
    assertEquals(segment.nextSetBit(5556), 6666);
    assertEquals(segment.nextSetBit(6667), -1);
  }

  /**
   * Test the nextClearBit method.
   */
//  @Test
  public void testNextClearBit() {
    segment.set(0,8191);
    segment.set(0, false);
    segment.set(1111, false);
    segment.set(2222, false);
    segment.set(3333, false);
    segment.set(4444, false);
    segment.set(5555, false);
    segment.set(6666, false);
    segment.set(7777, false);
    assertFalse(segment.get(6666));
    long next = segment.nextClearBit(0);
    assertEquals(next, 0);
    next = segment.nextClearBit(1);
    assertEquals(next, 1111);
    assertEquals(segment.nextClearBit(1112), 2222);
    assertEquals(segment.nextClearBit(2223), 3333);
    assertEquals(segment.nextClearBit(3334), 4444);
    assertEquals(segment.nextClearBit(4445), 5555);
    assertEquals(segment.nextClearBit(5556), 6666);
    assertEquals(segment.nextClearBit(6667), 7777);
    assertEquals(segment.nextClearBit(7778), 8192);
  }
}
