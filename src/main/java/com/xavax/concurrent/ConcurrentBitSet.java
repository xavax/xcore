//
// Copyright 2015, 2019 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//

package com.xavax.concurrent;

import com.xavax.exception.RangeException;

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
public class ConcurrentBitSet extends AbstractPageableObject<BitSetPage> {

  final static int LOG2_DEFAULT_PAGE_SIZE = 10;
  final static int LOG2_DEFAULT_SEGMENT_SIZE = 10;
  final static int LOG2_DEFAULT_INITIAL_SIZE = LOG2_DEFAULT_PAGE_SIZE + LOG2_DEFAULT_SEGMENT_SIZE;
  final static long DEFAULT_INITIAL_SIZE = 1 << LOG2_DEFAULT_INITIAL_SIZE;
  private final static String INDEX_PARAM = "index";
  private final static String FROM_INDEX_PARAM = "fromIndex";
  private final static String TO_INDEX_PARAM = "toIndex";

  /**
   * Construct a ConcurrentBitSet with the default initial bit set size and
   * segment size.
   */
  public ConcurrentBitSet() {
    this(DEFAULT_INITIAL_SIZE, LOG2_DEFAULT_PAGE_SIZE, LOG2_DEFAULT_SEGMENT_SIZE);
  }

  /**
   * Construct a ConcurrentBitSet with the specified initial bit set size and
   * default segment size.
   *
   * @param initialSize the initial size of the bit set.
   */
  public ConcurrentBitSet(final long initialSize) {
    this(initialSize, LOG2_DEFAULT_PAGE_SIZE, LOG2_DEFAULT_SEGMENT_SIZE);
  }

  /**
   * Construct a ConcurrentBitSet with the specified segment size and initial
   * bit set size.
   *
   * @param initialSize     the initial size of the bit set.
   * @param logPageSize     log2 of the page size (objects per page).
   * @param logSegmentSize  log2 of the segment size.
   */
  public ConcurrentBitSet(final long initialSize, final int logPageSize, final int logSegmentSize) {
    super(initialSize, logPageSize, logSegmentSize);
  }

  /**
   * Returns a new segment.
   *
   * @param parent  the parent pageable object.
   * @param size    log2 of the size of the segment.
   * @return a new segment.
   */
  @Override
  BitSetSegment createSegment(final AbstractPageableObject<BitSetPage> parent) {
    return new BitSetSegment(this);
  }

  /**
   * Returns the value of the bit at the specified index.
   *
   * @param index  the index of the desired bit.
   * @return the value of the bit at the specified index.
   */
  public boolean get(final long index) {
    if ( index < 0 ) {
      throw new RangeException(0, Long.MAX_VALUE, index, INDEX_PARAM);
    }
    incrementOperations();
    final int bitIndex = (int)(index & pageMask);
    final BitSetPage page = getPageContaining(index, false);
    return page != null && page.get(bitIndex);
  }

  /**
   * Sets the value of the bit at the specified index.
   *
   * @param index  the index of the bit to be set.
   * @param value  the new value of the specified bit.
   */
  public void set(final long index, final boolean value) {
    if ( index < 0 ) {
      throw new RangeException(0, Long.MAX_VALUE, index, INDEX_PARAM);
    }
    incrementOperations();
    final int bitIndex = (int)(index & pageMask);
    final BitSetPage page = getPageContaining(index, false);
    if ( page != null ) {
      page.set(bitIndex, value);
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
      throw new RangeException(0, Long.MAX_VALUE, fromIndex, FROM_INDEX_PARAM);
    }
    if ( toIndex <= fromIndex ) {
      throw new RangeException(fromIndex + 1, Long.MAX_VALUE, toIndex, TO_INDEX_PARAM);
    }
    // TODO: finish implementing this method.
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
      throw new RangeException(0, Long.MAX_VALUE, fromIndex, INDEX_PARAM);
    }
    // TODO: finish implementing this method.
    return 0;
  }

  /**
   * Finds the next clear bit beginning with the bit at fromIndex.
   *
   * @param fromIndex  the index of the bit to begin the search.
   * @return the index of the next clear bit.
   */
  public long nextClearBit(final long fromIndex) {
    if ( fromIndex < 0 ) {
      throw new RangeException(0, Long.MAX_VALUE, fromIndex, INDEX_PARAM);
    }
    // TODO: finish implementing this method.    
    return 0;
  }

  /**
   * Returns the bit index within a page of the given index.
   *
   * @param index  the index.
   * @return the bit index within a page.
   */
  public int indexToBitIndex(final long index) {
    return (int)(index & pageMask);
  }

  /**
   * Returns the page containing the bit at the specified index,
   * or null if the page does not exist and require is false,
   * Create a missing page if require is true.
   *
   * @param index    the index of the bit.
   * @param require  if true create a missing page.
   * @return the page containing the bit at the specified index.
   */
  public BitSetPage getPageContaining(final long index, final boolean require) {
    final int segmentIndex = indexToSegmentIndex(index);
    final int pageIndex = indexToPageIndex(index);
    final AbstractSegment<BitSetPage> segment = getSegment(segmentIndex, require);
    return segment == null ? null : segment.getPage(pageIndex, require);
  }
}
