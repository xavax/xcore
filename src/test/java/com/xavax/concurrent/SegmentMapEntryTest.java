//
// Copyright 2015 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//

package com.xavax.concurrent;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

/**
 * Test the ConcurrentBitSet.SegmentMapEntry class.
 */
public class SegmentMapEntryTest {
  final static int LOG2_DEFAULT_SEGMENT_SIZE = 10;

  private final static String EXPECTED1 = "segment: <null>";
  private final static String EXPECTED2 = "segment: {pageCount: 0, pages: [<null>,";
  private ConcurrentBitSet bitSet;
  private SegmentMapEntry<BitSetPage> entry;

  /**
   * Set up performed before each test.
   */
  @BeforeMethod
  public void setUp() {
    bitSet = new ConcurrentBitSet();
    entry = new SegmentMapEntry<>();
  }

  /**
   * Test the set method.
   */
  @Test
  public void testSet() {
    BitSetSegment segment = (BitSetSegment) entry.get();
    assertNull(segment);
    entry.set(null);
    segment = (BitSetSegment) entry.get();
    assertNull(segment);
    final BitSetSegment segment2 = new BitSetSegment(bitSet);
    entry.set(segment2);
    segment = (BitSetSegment) entry.get();
    assertEquals(segment, segment2);
    entry.set(segment);
  }

  /**
   * Test the toString method.
   */
//  @Test
  public void testToString() {
    String result = entry.toString();
    assertEquals(result, EXPECTED1);
    final BitSetSegment segment = new BitSetSegment(bitSet);
    entry.set(segment);
    result = entry.toString();
    assertEquals(result.substring(0, EXPECTED2.length()), EXPECTED2);
  }
}
