//
// Copyright 2015 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//

package com.xavax.concurrent;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

import com.xavax.concurrent.ConcurrentBitSet;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertNotNull;
import static com.xavax.concurrent.ConcurrentBitSet.*;

public class SegmentMapEntryTest {
  private final static String EXPECTED = "null";
  private final static int DEFAULT_SIZE = ConcurrentBitSet.LOG2_DEFAULT_SEGMENT_SIZE;

  private ConcurrentBitSet bitSet;
  private SegmentMapEntry entry;

  @BeforeMethod
  public void setup() {
    bitSet = new ConcurrentBitSet();
    entry = new SegmentMapEntry();
  }

  @Test
  public void testSet() {
    Segment segment = entry.get();
    assertNull(segment);
    entry.set(null);
    segment = entry.get();
    assertNull(segment);
    Segment segment2 = new Segment(bitSet, DEFAULT_SIZE);
    entry.set(segment2);
    segment = entry.get();
    assertEquals(segment, segment2);
    entry.set(segment);
  }

  @Test
  public void testToString() {
    assertEquals(entry.toString(), EXPECTED);
    Segment segment = new Segment(bitSet, LOG2_DEFAULT_SEGMENT_SIZE);
    entry.set(segment);
    assertNotNull(entry.toString());
  }
}
