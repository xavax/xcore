package com.xavax.concurrent;

import static com.xavax.concurrent.ConcurrentBitSetConstants.BITS_PER_PAGE;
import static com.xavax.concurrent.ConcurrentBitSetConstants.LOG2_BITS_PER_PAGE;

import com.xavax.util.AbstractJoinableObject;
import com.xavax.util.Joinable;
import com.xavax.util.Joiner;

/**
 * Segment encapsulates a fixed-size segment of the bit set. Segment sizes are
 * always a power of 2 to simplify calculations.
 */
class BitMapSegment extends AbstractJoinableObject implements Joinable {
  final static int BIT_INDEX_MASK = (1 << LOG2_BITS_PER_PAGE) - 1;
  final static int SEGMENT_BUFFER_SIZE = 8192;

  private int pageCount;
  private final BitSetPage[] map;
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
  public BitMapSegment(final ConcurrentBitSet parent, final int logSize) {
    this.parent = parent;
    final int size = 1 << (logSize - LOG2_BITS_PER_PAGE);
    map = new BitSetPage[size];
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
    final BitSetPage page = getPageContaining(index, false);
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
    final BitSetPage page = getPageContaining(index, value);
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
	final BitSetPage page = getPage(pageIndex, true);
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
	final BitSetPage page = getPage(pageIndex, false);
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
	final BitSetPage page = map[pageIndex];
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
	final BitSetPage page = map[pageIndex];
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
  @Override
  public String toString() {
    return doJoin(Joiner.create(SEGMENT_BUFFER_SIZE)).toString();
  }

  /**
   * Join this object to the specified joiner.
   *
   * @param joiner  the joiner to use.
   * @return the joiner.
   */
  @Override
  public Joiner doJoin(final Joiner joiner) {
    joiner.append("pageCount", pageCount)
          .append("map", (Object) map);
    return joiner;
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
  BitSetPage getPageContaining(final int index, final boolean require) {
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
  BitSetPage getPage(final int mapIndex, final boolean require) {
    BitSetPage page = map[mapIndex];
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
  BitSetPage createPage(final int mapIndex) {
    BitSetPage page = null;
    synchronized ( map ) {
      page = map[mapIndex];
      if ( page == null ) {
        page = new BitSetPage();
        map[mapIndex] = page;
        ++pageCount;
        parent.metrics.pageCreated();
      }
    }
    return page;
  }
}
