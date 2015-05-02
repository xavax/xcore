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
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertFalse;
import static com.xavax.concurrent.ConcurrentBitSet.*;

public class SegmentTest {
  private final static int MAX_BIT_INDEX = 1 << LOG2_DEFAULT_SEGMENT_SIZE;
  private final static int MAX_ENTRY_INDEX = 1 << (LOG2_DEFAULT_SEGMENT_SIZE - LOG2_BITS_PER_PAGE);
  private final static String EXPECTED = "[null, null";

  private ConcurrentBitSet bitSet;
  private Segment segment;

  @BeforeMethod
  public void setup() {
    bitSet = new ConcurrentBitSet();
    segment = new Segment(bitSet, LOG2_DEFAULT_SEGMENT_SIZE);
  }

  @Test
  public void testGetEntry() {
    assertEquals(segment.pageCount(), 0);
    for ( int index = 0; index < MAX_ENTRY_INDEX; ++index ) {
      int bitIndex = index << LOG2_BITS_PER_PAGE;
      Page entry = segment.getPage(bitIndex, false);
      assertNull(entry);
      assertEquals(segment.pageCount(), index);
      entry = segment.getPage(bitIndex, true);
      assertNotNull(entry);
      assertEquals(segment.pageCount(), index + 1);
      entry = segment.getPage(bitIndex, true);
      assertNotNull(entry);
      assertEquals(segment.pageCount(), index + 1);
    }
  }

  @Test
  public void testSet() {
    assertEquals(segment.pageCount(), 0);
    for ( int index = 0; index < MAX_BIT_INDEX; ++index ) {
      assertFalse(segment.get(index));
      segment.set(index, false);
      assertFalse(segment.get(index));
      segment.set(index, true);
      assertTrue(segment.get(index));
      segment.set(index, false);
      assertFalse(segment.get(index));
    }
  }

  @Test
  public void testToString() {
    String s = segment.toString().substring(0, EXPECTED.length());
    assertEquals(s, EXPECTED);
  }
}
