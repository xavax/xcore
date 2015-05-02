//
// Copyright 2015 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//

package com.xavax.concurrent;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

/**
 * ConcurrentBitSet encapsulates and manages an extendable bit set. The size
 * of the bit set is extended as necessary after it is created. To use memory
 * efficiently, the bit set is stored as a sparse array of segments which
 * consist of a sparse array of pages. Each page encapsulates a block of
 * 512 bits (although this can be adjusted at compile time). Segments and
 * pages that do not exist are assumed to contain false (clear) bits, and
 * are created only when an attempt is made to set a bit to true in a
 * corresponding region of the bit set.
 * 
 * To reduce thread contention, synchronization is performed at the page
 * level when setting or clearing a bit in an existing page. If a page needs
 * to be created, only the page map entry in the segment is locked, and
 * only long enough to update the map. If a segment must be created, only
 * the corresponding segment map entry is locked. If the entire segment map
 * must grow, the segment map is locked but can continue to be used to find
 * existing segments. Only subsequent attempts to enlarge the map will block
 * waiting for a resize.
 * 
 * @author alvitar@xavax.com Phillip L Harbison
 */
public class ConcurrentBitSet {
  final static int LOG2_BITS_PER_INT = 5;
  final static int LOG2_BITS_PER_LONG = 6;
  final static int LOG2_BITS_PER_PAGE = 9;
  final static int BITS_PER_INT = 1 << LOG2_BITS_PER_INT;
  final static int BITS_PER_LONG = 1 << LOG2_BITS_PER_LONG;
  final static int BITS_PER_PAGE = 1 << LOG2_BITS_PER_PAGE;

  final static int LOG2_DEFAULT_SEGMENT_SIZE = 16;
  final static int LOG2_MAX_SEGMENT_SIZE = BITS_PER_INT + LOG2_BITS_PER_PAGE;
  final static long MAX_SEGMENT_SIZE = 1 << LOG2_MAX_SEGMENT_SIZE;
  final static long DEFAULT_INITIAL_SIZE = 1 << 24;

  private int logSegmentSize;
  private int currentMapSize;
  private long segmentMask;
  private long segmentSize;
  private InternalMetrics metrics = new InternalMetrics();
  private SegmentMapEntry[] segmentMap;

  /**
   * Construct a ConcurrentBitSet with the default initial bit set size and
   * segment size.
   */
  public ConcurrentBitSet() {
    this(DEFAULT_INITIAL_SIZE, LOG2_DEFAULT_SEGMENT_SIZE);
  }

  /**
   * Construct a ConcurrentBitSet with the specified initial bit set size and
   * default segment size.
   *
   * @param initialSize the initial size of the bit set.
   */
  public ConcurrentBitSet(long initialSize) {
    this(initialSize, LOG2_DEFAULT_SEGMENT_SIZE);
  }

  /**
   * Construct a ConcurrentBitSet with the specified segment size and initial
   * bit set size.
   *
   * @param initialSize     the initial size of the bit set.
   * @param logSegmentSize  log2 of the segment size.
   */
  public ConcurrentBitSet(long initialSize, int logSegmentSize) {
    this.logSegmentSize = logSegmentSize <= LOG2_MAX_SEGMENT_SIZE
	? logSegmentSize : LOG2_DEFAULT_SEGMENT_SIZE;
    this.segmentSize = 1 << this.logSegmentSize;
    this.segmentMask = this.segmentSize - 1;
    int size = (int) (initialSize + segmentSize - 1) >> logSegmentSize;
    this.currentMapSize = size;
    this.segmentMap = new SegmentMapEntry[size];
    initMap(segmentMap, 0, size);
  }

  /**
   * Initialize a portion of a segment map with new segment map entries.
   *
   * @param map    the map to be updated.
   * @param start  the starting map index.
   * @param end    the ending map index.
   */
  private void initMap(SegmentMapEntry[] map, int start, int end) {
    for ( int i = start; i < end; ++i ) {
      SegmentMapEntry entry = new SegmentMapEntry();
      map[i] = entry;
    }
  }

  /**
   * Returns the value of the bit at the specified index.
   *
   * @param index  the index of the desired bit.
   * @return the value of the bit at the specified index.
   */
  public boolean get(long index) {
    metrics.incrementOperations();
    Segment segment = getSegment(index, false);
    return segment == null ? false : segment.get((int) (index & segmentMask));
  }

  /**
   * Sets the value of the bit at the specified index.
   *
   * @param index  the index of the bit to be set.
   * @param value  the new value of the specified bit.
   */
  public void set(long index, boolean value) {
    metrics.incrementOperations();
    Segment segment = getSegment(index, value);
    if ( segment != null ) {
      int offset = (int) (index & segmentMask);
      segment.set(offset, value);
    }
  }

  /**
   * Returns a snapshot of the metrics for this bit set.
   *
   * @return a snapshot of the metrics for this bit set.
   */
  public Metrics getMetrics() {
    return new Metrics(metrics);
  }

  /**
   * Returns the segment containing the specified bit. If the segment does not
   * exist and require is true, create a new segment.
   *
   * @param index    the desired bit.
   * @param require  true if a nonexistent segment should be created.
   * @return the segment containing the specified bit.
   */
  Segment getSegment(long index, boolean require) {
    int segmentIndex = (int) index >> logSegmentSize;
    if ( segmentIndex > currentMapSize ) {
      resize(segmentIndex);
    }
    SegmentMapEntry entry = segmentMap[segmentIndex];
    Segment segment = entry.get();
    if ( segment == null && require ) {
      synchronized ( entry ) {
	segment = entry.get();
	if ( segment == null ) {
	  segment = new Segment(this, logSegmentSize);
	  entry.set(segment);
	  metrics.segmentCreated();
	}
      }
    }
    return segment;
  }

  /**
   * Resize the segment map by creating a new, larger map that is a copy of the
   * old map and initialize the additional entries with new segment map entries.
   * All old map entries remain the same. To avoid frequent resizes, the new
   * size is computed by doubling the current size until it is greater than the
   * requested size.
   *
   * @param size the new minimum size.
   */
  void resize(long size) {
    synchronized ( segmentMap ) {
      if ( size > currentMapSize ) {
	int newSize = currentMapSize;
	do {
	  newSize *= 2;
	} while ( size > newSize );
	SegmentMapEntry[] map = Arrays.copyOf(segmentMap, newSize);
	initMap(map, currentMapSize, newSize);
	segmentMap = map;
	currentMapSize = newSize;
      }
    }
    metrics.segmentMapLocked();
  }

  /**
   * Returns a string representation of this bit set. NOTE: This is mainly for
   * testing and not recommended for general use.
   *
   * @return a string representation of this bit set.
   */
  public String toString() {
    return Arrays.toString(segmentMap);
  }

  /**
   * SegmentMapEntry encapsulates an entry in the segment map (a reference to a
   * segment). It reduces thread contention by allowing us to synchronize on one
   * entry rather than the entire segment map.
   */
  static class SegmentMapEntry {
    private Segment segment = null;

    /**
     * Get the segment for this map entry.
     *
     * @return the segment.
     */
    public synchronized Segment get() {
      return segment;
    }

    /**
     * Set the segment for this map entry.
     *
     * @param segment the new segment for this map entry.
     */
    public synchronized void set(Segment segment) {
      this.segment = segment;
    }

    /**
     * Returns a string representation of this map entry.
     *
     * @return a string representation of this map entry.
     */
    public String toString() {
      return segment == null ? "null" : segment.toString();
    }
  }

  /**
   * Segment encapsulates a fixed-size segment of the bit set. Segment sizes are
   * always a power of 2 to simplify calculations.
   */
  static class Segment {
    final static int BIT_INDEX_MASK = (1 << LOG2_BITS_PER_PAGE) - 1;

    private int pageCount = 0;
    private final Page[] map;
    private final ConcurrentBitSet parent;

    /**
     * Construct a segment with a bitmap of size 2 ^ logSize. The bitmap is
     * stored in an array of Page objects. Each Page has a small
     * array of longs that store BITS_PER_ENTRY bits, so the array size is
     * logSize - log2(BITS_PER_ENTRY)
     *
     * @param parent  the parent of this segment.
     * @param logSize log2 of the size of the bitmap.
     */
    public Segment(ConcurrentBitSet parent, int logSize) {
      this.parent = parent;
      int size = 1 << (logSize - LOG2_BITS_PER_PAGE);
      map = new Page[size];
    }

    /**
     * Returns the value of the bit at the specified index in this segment. If
     * the corresponding page does not exist, return false.
     *
     * @param index  the index of the desired bit in this segment.
     * @return the value of the bit at the specified index.
     */
    public boolean get(int index) {
      boolean result = false;
      int bitIndex = index & BIT_INDEX_MASK;
      Page entry = getPage(index, false);
      if ( entry != null ) {
	result = entry.getBit(bitIndex);
      }
      return result;
    }

    /**
     * Sets the bit at the specified into to the specified value. If the bit map
     * entry does not exist, only create it if the specified value is true;
     * otherwise, do nothing.
     * 
     * @param index  the index of the desired bit in this segment.
     * @param value  the new value of the bit.
     */
    public void set(int index, boolean value) {
      int bitIndex = index & BIT_INDEX_MASK;
      Page entry = getPage(index, value);
      if ( entry != null ) {
	entry.setBit(bitIndex, value);
      }
    }

    /**
     * Returns the page that contains the bit at a specified index. If
     * the page does not exist and require is true, create the entry
     * and update the map.
     *
     * @param index    the index of the desired bit.
     * @param require  true if a non-existent entry should be created.
     * @return the page containing the specified bit, or null if no
     *         such page exists and was not created.
     */
    Page getPage(int index, boolean require) {
      int mapIndex = index >> LOG2_BITS_PER_PAGE;
      Page page = map[mapIndex];
      if ( page == null && require ) {
	synchronized ( map ) {
	  page = map[mapIndex];
	  if ( page == null ) {
	    page = new Page();
	    map[mapIndex] = page;
	    ++pageCount;
	    parent.metrics.pageCreated();
	  }
	}
      }
      return page;
    }

    /**
     * Returns the number of pages in this segment.
     * 
     * @return the number of pages in this segment.
     */
    int pageCount() {
      return pageCount;
    }

    /**
     * Returns a string representation of this segment.
     *
     * @return a string representation of this segment.
     */
    public String toString() {
      return Arrays.toString(map);
    }
  }

  /**
   * Page encapsulates a small array of longs used to represent a portion
   * of a bit set.
   */
  static class Page {
    final static int BIT_MAP_ARRAY_SIZE = 1 << (LOG2_BITS_PER_PAGE - LOG2_BITS_PER_LONG);
    final static int BIT_MAP_INDEX_MASK = (1 << LOG2_BITS_PER_LONG) - 1;

    private long[] bits;

    /**
     * Construct a Page.
     */
    public Page() {
      bits = new long[BIT_MAP_ARRAY_SIZE];
    }

    /**
     * Returns the value of the specified bit as a boolean.
     *
     * @param index  the index of a bit within the long value.
     * @return the value of the specified bit.
     */
    public synchronized boolean getBit(int index) {
      // assert(index >= 0 && index < BITS_PER_PAGE);
      return (bits[index >> LOG2_BITS_PER_LONG] & (1L << (index & BIT_MAP_INDEX_MASK))) != 0;
    }

    /**
     * Sets the value of the specified bit.
     *
     * @param index  the index of a bit within the long value.
     * @param flag   the new value of the specified bit.
     */
    public synchronized void setBit(int index, boolean flag) {
      // assert(index >= 0 && index < BITS_PER_PAGE);
      int lindex = index >> LOG2_BITS_PER_LONG;
      long mask = 1L << (index & BIT_MAP_INDEX_MASK);
      boolean current = (bits[lindex] & mask) != 0;
      if ( flag != current ) {
	bits[lindex] ^= mask;
      }
    }

    /**
     * Returns a string representation of this page.
     *
     * @return a string representation of this page.
     */
    public String toString() {
      StringBuilder sb = new StringBuilder(100);
      boolean first = true;
      sb.append("[");
      for ( long l : bits ) {
	if ( first ) {
	  first = false;
	}
	else {
	  sb.append(", ");
	}
	for ( int i = 0; i < 16; ++i ) {
	  int nibble = (int) (l & 0x0F);
	  l >>= 4;
	  if ( i != 0 ) {
	    sb.append(".");
	  }
	  sb.append(nibbles[nibble]);
	}
      }
      sb.append("]");
      return sb.toString();
    }
  }

  /**
   * Metrics is a public snapshot of the internal metrics.
   */
  public static class Metrics {
    private final long pagesCreated;
    private final long segmentMapLocks;
    private final long segmentsCreated;
    private final long totalOperations;

    /**
     * Construct a snapshot of the internal metrics.
     * 
     * @param metrics the internal metrics.
     */
    Metrics(InternalMetrics metrics) {
      pagesCreated = metrics.pagesCreated.get();
      segmentMapLocks = metrics.segmentMapLocks.get();
      segmentsCreated = metrics.segmentsCreated.get();
      totalOperations = metrics.totalOperations.get();
    }

    /**
     * Returns the number of pages created since the bit set was
     * created.
     * 
     * @return the number of pages created .
     */
    public long getPagesCreated() {
      return pagesCreated;
    }

    /**
     * Returns the number of segment map locks since the bit set was created.
     *
     * @return the number of segment map locks.
     */
    public long getSegmentMapLocks() {
      return segmentMapLocks;
    }

    /**
     * Returns the number of segments created since the bit set was created.
     *
     * @return the number of segments created.
     */
    public long getSegmentsCreated() {
      return segmentsCreated;
    }

    /**
     * Returns the total number of operations since the bit set was created.
     *
     * @return the total number of operations.
     */
    public long getTotalOperations() {
      return totalOperations;
    }

    /**
     * Returns the metrics as a string.
     *
     * @return the metrics as a string.
     */
    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("{ ")
	  .append("pc: ").append(pagesCreated)
	  .append(", sc: ").append(segmentsCreated)
	  .append(", sml: ").append(segmentMapLocks)
	  .append(", ops: ").append(totalOperations)
	  .append(" }");
      return sb.toString();
    }
  }

  /**
   * InternalMetrics encapsulates the counters used to collect metrics on the
   * performance of ConcurrentBitSet.
   */
  static class InternalMetrics {
    private final AtomicLong pagesCreated;
    private final AtomicLong segmentMapLocks;
    private final AtomicLong segmentsCreated;
    private final AtomicLong totalOperations;

    /**
     * Construct an InternalMetrics.
     */
    InternalMetrics() {
      pagesCreated    = new AtomicLong();
      segmentMapLocks = new AtomicLong();
      segmentsCreated = new AtomicLong();
      totalOperations = new AtomicLong();
    }

    /**
     * Increment the counter for total operations (bit get and set operations).
     */
    public void incrementOperations() {
      totalOperations.incrementAndGet();
    }

    /**
     * Increment the counter for pages created.
     */
    public void pageCreated() {
      pagesCreated.incrementAndGet();
    }

    /**
     * Increment the counter for segment map locks.
     */
    public void segmentMapLocked() {
      segmentMapLocks.incrementAndGet();
    }

    /**
     * Increment the counter for segments created.
     */
    public void segmentCreated() {
      segmentsCreated.incrementAndGet();
    }
  }

  private final static String[] nibbles = new String[] {
      "0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111",
      "1000", "1001", "1010", "1011", "1100", "1101", "1110", "1111"
  };

}