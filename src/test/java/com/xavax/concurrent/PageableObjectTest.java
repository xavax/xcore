package com.xavax.concurrent;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PageableObjectTest {

  private final static int LOG2_PAGE_SIZE = 10;
  private final static int LOG2_SEGMENT_SIZE = 18;
  private final static long INDEX1 = 9999999999l;
  private final static long PAGE_INDEX1 = 760;
  private final static long SEGMENT_INDEX1 = 38146;
  private final static long PAGE_SIZE = 1 << LOG2_PAGE_SIZE;
  private final static long SEGMENT_SIZE = 1 << LOG2_SEGMENT_SIZE;
  private final static long INITIAL_SIZE = 1 << 24;

  private ConcurrentBitSet bitSet;

  @Before
  public void setUp() {
    bitSet = new ConcurrentBitSet(INITIAL_SIZE, LOG2_PAGE_SIZE, LOG2_SEGMENT_SIZE);
  }

  @Test
  public void indexToPageIndex() {
    assertEquals(bitSet.indexToPageIndex(INDEX1), PAGE_INDEX1);
  }

  @Test
  public void indexToSegmentIndex() {
    assertEquals(bitSet.indexToSegmentIndex(INDEX1), SEGMENT_INDEX1);
  }

  @Test
  public void log2() {
    assertEquals(bitSet.log2(PAGE_SIZE), LOG2_PAGE_SIZE);
    assertEquals(bitSet.log2(SEGMENT_SIZE), LOG2_SEGMENT_SIZE);
    assertEquals(bitSet.log2(PAGE_SIZE - 1), 0);
  }
}
