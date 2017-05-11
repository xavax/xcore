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
import static org.testng.Assert.assertNotNull;

import static com.xavax.concurrent.ConcurrentBitSet.*;

/**
 * Test the ConcurrentBitSet.SegmentMapEntry class.
 */
@SuppressWarnings("PMD.TooManyStaticImports")
public class SegmentMapEntryTest {
  private final static int DEFAULT_SIZE = LOG2_DEFAULT_SEGMENT_SIZE;
  private final static String EXPECTED1 = "segment: <null>";
  private final static String EXPECTED2 = "segment: (pageCount: 0, [<null>,";
  private ConcurrentBitSet bitSet;
  private SegmentMapEntry entry;

  /**
   * Set up performed before each test.
   */
  @BeforeMethod
  public void setup() {
    bitSet = new ConcurrentBitSet();
    entry = new SegmentMapEntry();
  }

  /**
   * Test the set method.
   */
  @Test
  public void testSet() {
    Segment segment = entry.get();
    assertNull(segment);
    entry.set(null);
    segment = entry.get();
    assertNull(segment);
    final Segment segment2 = new Segment(bitSet, DEFAULT_SIZE);
    entry.set(segment2);
    segment = entry.get();
    assertEquals(segment, segment2);
    entry.set(segment);
  }

  /**
   * Test the toString method.
   */
  @Test
  public void testToString() {
    String result = entry.toString();
    assertEquals(result, EXPECTED1);
    final Segment segment = new Segment(bitSet, LOG2_DEFAULT_SEGMENT_SIZE);
    entry.set(segment);
    result = entry.toString();
    assertEquals(result.substring(0, EXPECTED2.length()), EXPECTED2);
  }
}
