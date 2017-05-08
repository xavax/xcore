//
// Copyright 2015 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//

package com.xavax.concurrent;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

import com.xavax.exception.RangeException;
import com.xavax.util.Joinable;
import com.xavax.util.Joiner;

import static com.xavax.util.Constants.*;

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
public class ConcurrentBitSet implements Joinable {
  final static int LOG2_BITS_PER_BYTE = 3;
  final static int LOG2_BITS_PER_INT  = 5;
  final static int LOG2_BITS_PER_LONG = 6;
  final static int LOG2_BITS_PER_PAGE = 9;
  final static int BITS_PER_INT = 1 << LOG2_BITS_PER_INT;
  final static int BITS_PER_LONG = 1 << LOG2_BITS_PER_LONG;
  final static int BITS_PER_PAGE = 1 << LOG2_BITS_PER_PAGE;
  final static int DEFAULT_BUFFER_SIZE = 32768;

  final static int LOG2_DEFAULT_SEGMENT_SIZE = 16;
  final static int LOG2_MAX_SEGMENT_SIZE = BITS_PER_INT + LOG2_BITS_PER_PAGE;
  final static long MAX_SEGMENT_SIZE = 1 << LOG2_MAX_SEGMENT_SIZE;
  final static long DEFAULT_INITIAL_SIZE = 1 << 24;

  private int logSegmentSize;
  private int currentMapSize;
  private long segmentMask;
  // private long segmentSize;
  // private long maxBitIndex;
  final private InternalMetrics metrics = new InternalMetrics();
  private ReentrantLock segmentMapLock;
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
  public ConcurrentBitSet(final long initialSize) {
    this(initialSize, LOG2_DEFAULT_SEGMENT_SIZE);
  }

  /**
   * Construct a ConcurrentBitSet with the specified segment size and initial
   * bit set size.
   *
   * @param initialSize     the initial size of the bit set.
   * @param logSegmentSize  log2 of the segment size.
   */
  public ConcurrentBitSet(final long initialSize, final int logSegmentSize) {
    if ( initialSize < 0 ) {
      throw new RangeException(0, Long.MAX_VALUE, initialSize);
    }
    if ( logSegmentSize < 0 || logSegmentSize > LOG2_MAX_SEGMENT_SIZE ) {
      throw new RangeException(0, LOG2_MAX_SEGMENT_SIZE, logSegmentSize);
    }
    this.logSegmentSize = logSegmentSize;
    final long segmentSize = 1 << this.logSegmentSize;
    this.segmentMask = segmentSize - 1;
    final int size = (int) (initialSize + segmentSize - 1) >>> logSegmentSize;
    this.currentMapSize = size;
    this.segmentMap = new SegmentMapEntry[size];
    this.segmentMapLock = new ReentrantLock();
    initMap(segmentMap, 0, size);
  }

  /**
   * Initialize a portion of a segment map with new segment map entries.
   *
   * @param map    the map to be updated.
   * @param start  the starting map index.
   * @param end    the ending map index.
   */
  private void initMap(SegmentMapEntry[] map, final int start, final int end) {
    for ( int i = start; i < end; ++i ) {
      final SegmentMapEntry entry = new SegmentMapEntry();
      map[i] = entry;
    }
  }

  /**
   * Returns the value of the bit at the specified index.
   *
   * @param index  the index of the desired bit.
   * @return the value of the bit at the specified index.
   */
  public boolean get(final long index) {
    if ( index < 0 ) {
      throw new RangeException(0, Long.MAX_VALUE, index);
    }
    metrics.incrementOperations();
    final Segment segment = getSegment(index, false);
    return segment != null && segment.get((int) (index & segmentMask));
  }

  /**
   * Sets the value of the bit at the specified index.
   *
   * @param index  the index of the bit to be set.
   * @param value  the new value of the specified bit.
   */
  public void set(final long index, final boolean value) {
    if ( index < 0 ) {
      throw new RangeException(0, Long.MAX_VALUE, index);
    }
    metrics.incrementOperations();
    final Segment segment = getSegment(index, value);
    if ( segment != null ) {
      segment.set((int) (index & segmentMask), value);
    }
  }

  /**
   * Sets the value of the bit at the specified index.
   *
   * @param index  the index of the bit to be set.
   */
  public void set(final long index) {
    set(index, true);
  }

  /**
   * Sets a range of bits beginning with the bit at fromIndex and
   * ending with the bit at toIndex (exclusive).
   *
   * @param fromIndex  index of the first bit to set.
   * @param toIndex    index of the last bit to set.
   */
  public void set(final int fromIndex, final int toIndex) {
    if ( fromIndex < 0 ) {
      throw new RangeException(0, Long.MAX_VALUE, fromIndex);
    }
    if ( toIndex <= fromIndex ) {
      throw new RangeException(fromIndex + 1, Long.MAX_VALUE, toIndex);
    }
    // int segmentIndex = (int) (fromIndex >>> logSegmentSize);
  }

  /**
   * Clears the value of the bit at the specified index.
   *
   * @param index  the index of the bit to be set.
   */
  public void clear(final long index) {
    set(index, false);
  }

  /**
   * Finds the next set bit beginning with the bit at fromIndex.
   *
   * @param fromIndex  the index of the bit to begin the search.
   * @return the index of the next set bit.
   */
  public long nextSetBit(final long fromIndex) {
    if ( fromIndex < 0 ) {
      throw new RangeException(0, Long.MAX_VALUE, fromIndex);
    }
    final long result = 0;
    
    return result;
  }

  /**
   * Finds the next clear bit beginning with the bit at fromIndex.
   *
   * @param fromIndex  the index of the bit to begin the search.
   * @return the index of the next clear bit.
   */
  public long nextClearBit(final long fromIndex) {
    if ( fromIndex < 0 ) {
      throw new RangeException(0, Long.MAX_VALUE, fromIndex);
    }
    final long result = 0;
    
    return result;
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
  Segment getSegment(final long index, final boolean require) {
    final int segmentIndex = (int) index >>> logSegmentSize;
    if ( segmentIndex > currentMapSize ) {
      resize(segmentIndex);
    }
    final SegmentMapEntry entry = segmentMap[segmentIndex];
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
  void resize(final long size) {
    try {
      segmentMapLock.lock();
      // Check again since the map may already be resized by another thread.
      if ( size > currentMapSize ) {
	int newSize = currentMapSize;
	do {
	  newSize *= 2;
	} while ( size > newSize );
	final SegmentMapEntry[] map = Arrays.copyOf(segmentMap, newSize);
	initMap(map, currentMapSize, newSize);
	segmentMap = map;
	currentMapSize = newSize;
      }
    }
    finally {
      segmentMapLock.unlock();
      metrics.segmentMapLocked();
    }
  }

  /**
   * Returns a string representation of this bit set. NOTE: This is mainly for
   * testing and not recommended for general use.
   *
   * @return a string representation of this bit set.
   */
  public String toString() {
    return join(Joiner.create(DEFAULT_BUFFER_SIZE)).toString();
  }

  /**
   * Join this object to the specified joiner.
   *
   * @param joiner  the joiner to use.
   * @return the joiner.
   */
  @Override
  public Joiner join(final Joiner joiner) {
    return joiner == null ? null :
      joiner.beginObject()
            .append((Object[]) segmentMap)
            .endObject();
  }

  /**
   * SegmentMapEntry encapsulates an entry in the segment map (a reference to a
   * segment). It reduces thread contention by allowing us to synchronize on one
   * entry rather than the entire segment map.
   */
  static class SegmentMapEntry implements Joinable {
    final static int DEFAULT_BUFFER_SIZE = 8192;

    private Segment segment;

    /**
     * Get the segment for this map entry.
     *
     * @return the segment.
     */
    public Segment get() {
      Segment result;
      synchronized (this) {
	result = this.segment;
      }
      return result;
    }

    /**
     * Set the segment for this map entry.
     *
     * @param segment the new segment for this map entry.
     */
    public void set(final Segment segment) {
      synchronized (this) {
	this.segment = segment;
      }
    }

    /**
     * Returns a string representation of this map entry.
     *
     * @return a string representation of this map entry.
     */
    public String toString() {
      return join(Joiner.create(DEFAULT_BUFFER_SIZE)).toString();
    }

    /**
     * Join this object to the specified joiner.
     *
     * @param joiner  the joiner to use.
     * @return the joiner.
     */
    @Override
    public Joiner join(final Joiner joiner) {
      return joiner == null ? null : joiner.append(segment);
    }
  }

  /**
   * Segment encapsulates a fixed-size segment of the bit set. Segment sizes are
   * always a power of 2 to simplify calculations.
   */
  static class Segment implements Joinable {
    final static int BIT_INDEX_MASK = (1 << LOG2_BITS_PER_PAGE) - 1;
    final static int DEFAULT_BUFFER_SIZE = 8192;

    private int pageCount;
    private final Page[] map;
    private final ConcurrentBitSet parent;

    /**
     * Construct a segment with a bitmap of size 2 ^ logSize. The bitmap
     * is stored in an array of Page objects. Each Page has a small array
     * of longs that store BITS_PER_PAGE bits, so the array size is:
     *   2 ^ (logSize - log2(BITS_PER_PAGE))
     *
     * @param parent  the parent of this segment.
     * @param logSize log2 of the size of the bitmap.
     */
    public Segment(final ConcurrentBitSet parent, final int logSize) {
      this.parent = parent;
      final int size = 1 << (logSize - LOG2_BITS_PER_PAGE);
      map = new Page[size];
    }

    /**
     * Returns the value of the bit at the specified index in this segment.
     * If the corresponding page does not exist, return false.
     *
     * @param index  the index of the desired bit in this segment.
     * @return the value of the bit at the specified index.
     */
    public boolean get(final int index) {
      boolean result = false;
      final int bitIndex = index & BIT_INDEX_MASK;
      final Page page = getPageContaining(index, false);
      if ( page != null ) {
	result = page.get(bitIndex);
      }
      return result;
    }

    /**
     * Sets the bit at the specified into to the specified value. If the
     * page does not exist, only create it if the specified value is true;
     * otherwise, do nothing.
     * 
     * @param index  the index of the desired bit in this segment.
     * @param value  the new value of the bit.
     */
    public void set(final int index, final boolean value) {
      final int bitIndex = index & BIT_INDEX_MASK;
      final Page page = getPageContaining(index, value);
      if ( page != null ) {
	page.set(bitIndex, value);
      }
    }

    /**
     * Sets a range of bits beginning with the bit at fromIndex and
     * ending with the bit at toIndex (inclusive).
     *
     * @param fromIndex  index of the first bit to set.
     * @param toIndex    index of the last bit to set.
     */
    public void set(final int fromIndex, final int toIndex) {
      // assert(fromIndex >= 0 && toIndex > fromIndex);
      int pageIndex = fromIndex >>> LOG2_BITS_PER_PAGE;
      final int lastPage = toIndex >>> LOG2_BITS_PER_PAGE;
      int firstBit = fromIndex & BIT_INDEX_MASK;
      final int lastBit = toIndex & BIT_INDEX_MASK;
      final int end = BITS_PER_PAGE - 1;
      for ( ; pageIndex <= lastPage; ++pageIndex ) {
	final Page page = getPage(pageIndex, true);
	if ( page != null ) {
	  page.set(firstBit, pageIndex == lastPage ? lastBit : end);
	}
	firstBit = 0;
      }
    }

    /**
     * Clears a range of bits beginning with the bit at fromIndex and
     * ending with the bit at toIndex (inclusive).
     *
     * @param fromIndex  index of the first bit to clear.
     * @param toIndex    index of the last bit to clear.
     */
    public void clear(final int fromIndex, final int toIndex) {
      // assert(fromIndex >= 0 && toIndex > fromIndex);
      int pageIndex = fromIndex >>> LOG2_BITS_PER_PAGE;
      final int lastPage = toIndex >>> LOG2_BITS_PER_PAGE;
      int firstBit = fromIndex & BIT_INDEX_MASK;
      final int lastBit = toIndex & BIT_INDEX_MASK;
      final int end = BITS_PER_PAGE - 1;
      for ( ; pageIndex <= lastPage; ++pageIndex ) {
	final Page page = getPage(pageIndex, false);
	if ( page != null ) {
	  page.clear(firstBit, pageIndex == lastPage ? lastBit : end);
	}
	firstBit = 0;
      }
    }

    /**
     * Finds the next set bit starting at fromIndex.
     *
     * @param fromIndex  the index of the bit to begin searching.
     * @return the index of the next set bit, or -1 if not found.
     */
    public int nextSetBit(final int fromIndex) {
      int result = -1;
      int bitIndex = fromIndex & BIT_INDEX_MASK;
      int pageIndex = fromIndex >>> LOG2_BITS_PER_PAGE;
      for ( ; pageIndex < map.length; ++pageIndex ) {
	final Page page = map[pageIndex];
	if ( page != null ) {
	  final int index = page.nextSetBit(bitIndex);
	  if ( index >= 0 ) {
	    result = index;
	    break;
	  }
	  bitIndex = 0;
	}
      }
      if ( result >= 0 ) {
	result += pageIndex << LOG2_BITS_PER_PAGE;
      }
      return result;
    }

    /**
     * Finds the next clear bit starting at fromIndex.
     *
     * @param fromIndex  the index of the bit to begin searching.
     * @return the index of the next clear bit, or -1 if not found.
     */
    public int nextClearBit(final int fromIndex) {
      int result = -1;
      int bitIndex = fromIndex & BIT_INDEX_MASK;
      int pageIndex = fromIndex >>> LOG2_BITS_PER_PAGE;
      for ( ; pageIndex < map.length; ++pageIndex ) {
	final Page page = map[pageIndex];
	if ( page == null ) {
	  result = 0;
	  break;
	}
	else {
	  final int index = page.nextClearBit(bitIndex);
	  if ( index >= 0 ) {
	    result = index;
	    break;
	  }
	}
	bitIndex = 0;
      }
      if ( result >= 0 ) {
	result += pageIndex << LOG2_BITS_PER_PAGE;
      }
      return result;
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
      return join(Joiner.create(DEFAULT_BUFFER_SIZE)).toString();
    }

    /**
     * Join this object to the specified joiner.
     *
     * @param joiner  the joiner to use.
     * @return the joiner.
     */
    @Override
    public Joiner join(final Joiner joiner) {
      return joiner == null ? null :
	joiner.append((Object[]) map);
    }

    /**
     * Returns the page that contains the bit at a specified index. If
     * the page does not exist and require is true, create the page and
     * update the map.
     *
     * @param index    the index of the desired bit.
     * @param require  true if a non-existent entry should be created.
     * @return the page containing the specified bit, or null if no
     *         such page exists and was not created.
     */
    Page getPageContaining(final int index, final boolean require) {
      return getPage(index >>> LOG2_BITS_PER_PAGE, require);
    }

    /**
     * Returns the page at the specified index in the page map. If the
     * page does not exist and require is true, create the page.
     *
     * @param require   true if a missing page should be created.
     * @param mapIndex  the index of the desired page.
     * @return the page at the specified index.
     */
    Page getPage(final int mapIndex, final boolean require) {
      Page page = map[mapIndex];
      if ( page == null && require ) {
	page = createPage(mapIndex);
      }
      return page;
    }

    /**
     * Create a new page at the specified index in the page map.
     *
     * @param mapIndex  the index for the new page.
     * @return a new page.
     */
    Page createPage(final int mapIndex) {
      Page page = null;
      synchronized ( map ) {
        page = map[mapIndex];
        if ( page == null ) {
          page = new Page();
          map[mapIndex] = page;
          ++pageCount;
          parent.metrics.pageCreated();
        }
      }
      return page;
    }
  }

  /**
   * Page encapsulates a small array of longs used to represent a portion
   * of a bit set.
   */
  static class Page implements Joinable {
    final static int BIT_MAP_ARRAY_SIZE = 1 << (LOG2_BITS_PER_PAGE - LOG2_BITS_PER_BYTE);
    final static int BIT_MAP_INDEX_MASK = (1 << LOG2_BITS_PER_BYTE) - 1;
    final static int DEFAULT_BUFFER_SIZE = 8192;

    private final static String[] NIBBLES = new String[] {
        "0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111",
        "1000", "1001", "1010", "1011", "1100", "1101", "1110", "1111"
    };

    private final static short[] LEFT_MASKS = new short[] {
      0xFF, 0x7F, 0x3F, 0x1F, 0x0F, 0x07, 0x03, 0x01
    };

    private final static short[] RIGHT_MASKS = new short[] {
      0x80, 0xC0, 0xE0, 0xF0, 0xF8, 0xFC, 0xFE, 0xFF
    };

    private byte[] bits;

    /**
     * Construct a Page.
     */
    public Page() {
      bits = new byte[BIT_MAP_ARRAY_SIZE];
    }

    /**
     * Returns the value of the specified bit as a boolean.
     *
     * @param index  the index of a bit within the page.
     * @return the value of the specified bit.
     */
    public boolean get(final int index) {
      // assert(index >= 0 && index < BITS_PER_PAGE);
      return (bits[index >>> LOG2_BITS_PER_BYTE] & (1 << (~index & BIT_MAP_INDEX_MASK))) != 0;
    }

    /**
     * Sets the value of the specified bit.
     *
     * @param index  the index of a bit within the page.
     * @param flag   the new value of the specified bit.
     */
    public void set(final int index, final boolean flag) {
      // assert(index >= 0 && index < BITS_PER_PAGE);
      final int byteIndex = index >>> LOG2_BITS_PER_BYTE;
      final int mask = 1 << (~index & BIT_MAP_INDEX_MASK);
      synchronized (this) {
	final boolean current = (bits[byteIndex] & mask) != 0;
	if ( flag != current ) {
	  bits[byteIndex] ^= mask;
	}
      }
    }

    /**
     * Sets a range of bits beginning with the bit at fromIndex and
     * ending with the bit at toIndex (inclusive).
     *
     * @param fromIndex  index of the first bit to set.
     * @param toIndex    index of the last bit to set.
     */
    public void set(final int fromIndex, final int toIndex) {
      // assert(fromIndex >= 0 && toIndex > fromIndex);
      final int firstByte = fromIndex >>> LOG2_BITS_PER_BYTE;
      final int lastByte = toIndex >>> LOG2_BITS_PER_BYTE;
      final int leftMask = LEFT_MASKS[fromIndex & BIT_MAP_INDEX_MASK];
      final int rightMask = RIGHT_MASKS[toIndex & BIT_MAP_INDEX_MASK];
      synchronized ( this ) {
	for ( int i = firstByte; i <= lastByte; ++i ) {
	  if ( i == firstByte ) {
	    bits[i] |= leftMask;
	  }
	  else if ( i == lastByte ) {
	    bits[i] |= rightMask;
	  }
	  else {
	    bits[i] = (byte) 0x0FF;
	  }
	}
      }
    }

    /**
     * Clears a range of bits beginning with the bit at fromIndex and
     * ending with the bit at toIndex (inclusive).
     *
     * @param fromIndex  index of the first bit to clear.
     * @param toIndex    index of the last bit to clear.
     */
    public void clear(final int fromIndex, final int toIndex) {
      if ( fromIndex >= 0 && toIndex > fromIndex ) {
	final int firstByte = fromIndex >>> LOG2_BITS_PER_BYTE;
	final int lastByte = toIndex >>> LOG2_BITS_PER_BYTE;
	final int leftMask = ~LEFT_MASKS[fromIndex & BIT_MAP_INDEX_MASK] & 0x0FF;
	final int rightMask = ~RIGHT_MASKS[toIndex & BIT_MAP_INDEX_MASK] & 0x0FF;
    	synchronized (this) {
    	  for ( int i = firstByte; i <= lastByte; ++i ) {
    	    if ( i == firstByte ) {
    	      bits[i] &= leftMask;
    	    }
    	    else if ( i == lastByte ) {
    	      bits[i] &= rightMask;
    	    }
    	    else {
    	      bits[i] = 0;
    	    }
    	  }
    	}
      }
    }

    /**
     * Finds the next set bit starting at fromIndex.
     *
     * @param fromIndex  the index of the bit to begin searching.
     * @return the index of the next set bit, or -1 if not found.
     */
    public int nextSetBit(final int fromIndex) {
      boolean run = true;
      int result = -1;
      final int firstByte = fromIndex >>> LOG2_BITS_PER_BYTE;
      final int leftMask = LEFT_MASKS[fromIndex & BIT_MAP_INDEX_MASK];
      for ( int i = firstByte; run && i < BIT_MAP_ARRAY_SIZE; ++i ) {
	final int masked = (i == firstByte ? leftMask : 0xFF) & bits[i];
	if ( masked != 0 ) {
	  int mask = 0x80;
	  for ( int j = 0; j < 8; ++j ) {
	    if ( (mask & masked) != 0 ) {
	      result = (i << LOG2_BITS_PER_BYTE) + j;
	      run = false;
	      break;
	    }
	    mask >>>= 1;
	  }
	}
      }
      return result;
    }

    /**
     * Finds the next clear bit starting at fromIndex.
     *
     * @param fromIndex  the index of the bit to begin searching.
     * @return the index of the next clear bit, or -1 if not found.
     */
    public int nextClearBit(final int fromIndex) {
      boolean run = true;
      int result = -1;
      final int firstByte = fromIndex >>> LOG2_BITS_PER_BYTE;
      final int leftMask = LEFT_MASKS[fromIndex & BIT_MAP_INDEX_MASK];
      for ( int i = firstByte; run && i < BIT_MAP_ARRAY_SIZE; ++i ) {
	final int masked = (i == firstByte ? leftMask : 0xFF) & ~bits[i];
	if ( masked != 0 ) {
	  int mask = 0x80;
	  for ( int j = 0; j < 8; ++j ) {
	    if ( (mask & masked) != 0 ) {
	      result = (i << LOG2_BITS_PER_BYTE) + j;
	      run = false;
	      break;
	    }
	    mask >>>= 1;
	  }
	}
      }
      return result;
    }

    /**
     * Returns a string representation of this page.
     *
     * @return a string representation of this page.
     */
    public String toString() {
      return join(Joiner.create(DEFAULT_BUFFER_SIZE)).toString();
    }

    /**
     * Join this object to the specified joiner.
     *
     * @param joiner  the joiner to use.
     * @return the joiner.
     */
    @Override
    public Joiner join(final Joiner joiner) {
      if ( joiner != null ) {
	toString(joiner.getBuilder());
      }
      return joiner;
    }

    /**
     * Format a string representation of this page using the supplied builder.
     *
     * @param builder  the builder.
     * @return the builder.
     */
    public StringBuilder toString(final StringBuilder builder) {
      if ( builder != null ) {
	boolean first = true;
	builder.append(LEFT_BRACKET);
	for ( final byte b : bits ) {
	  if ( first ) {
	    first = false;
	  }
	  else {
	    builder.append(PERIOD);
	  }
	  builder.append(NIBBLES[(b & 0xF0) >>> 4])
	         .append(NIBBLES[b & 0x0F]);
	}
	builder.append(RIGHT_BRACKET);
      }
      return builder;
    }
  }

  /**
   * Metrics is a public snapshot of the internal metrics.
   */
  public static class Metrics implements Joinable {
    private final static int DEFAULT_BUFFER_SIZE = 128;

    private final long pagesCreated;
    private final long segmentMapLocks;
    private final long segmentsCreated;
    private final long totalOperations;

    /**
     * Construct a snapshot of the internal metrics.
     * 
     * @param metrics the internal metrics.
     */
    Metrics(final InternalMetrics metrics) {
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
      // return toString(new StringBuilder(DEFAULT_BUFFER_SIZE)).toString();
      return join(Joiner.create(DEFAULT_BUFFER_SIZE)).toString();
    }

    /**
     * Join this object to the specified joiner.
     *
     * @param joiner  the joiner to use.
     * @return the joiner.
     */
    @Override
    public Joiner join(final Joiner joiner) {
      return joiner == null ? null :
	joiner.appendRaw("{ ")
	      .append("pc", pagesCreated)
	      .append("sc", segmentsCreated)
	      .append("sel", segmentMapLocks)
	      .append("ops", totalOperations)
	      .appendRaw(" }");
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
}
