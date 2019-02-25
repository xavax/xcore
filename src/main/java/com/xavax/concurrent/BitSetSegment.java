//
// Copyright 2015, 2019 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//

package com.xavax.concurrent;

/**
 * Segment encapsulates a fixed-size segment of the bit set. Segment sizes are
 * always a power of 2 to simplify calculations.
 */
class BitSetSegment extends AbstractSegment<BitSetPage> {
  final long bitIndexMask;

  /**
   * Construct a segment with a bitmap of size 2 ^ logSize. The bitmap
   * is stored in an array of Page objects. Each Page has a small array
   * of longs that store BITS_PER_PAGE bits, so the array size is:
   *   2 ^ (logSize - log2(BITS_PER_PAGE))
   *
   * @param parent  the parent of this segment.
   */
  public BitSetSegment(final ConcurrentBitSet parent) {
    super(parent);
    bitIndexMask = parent.getPageSize() - 1;
  }

  /**
   * Create a new page to be added to the page map.
   *
   * @return a new page.
   */
  protected BitSetPage createPage() {
    return new BitSetPage((ConcurrentBitSet) parent);
  }

  /**
   * Create an array of pages.
   *
   * @param size  the size of the array.
   * @return an array of pages.
   */
  protected BitSetPage[] createPageArray(final int size) {
    return new BitSetPage[size];
  }

  /**
   * Returns the value of the bit at the specified index in this segment.
   * If the corresponding page does not exist, return false.
   *
   * @param index  the index of the desired bit in this segment.
   * @return the value of the bit at the specified index.
   */
  public boolean get(final long index) {
    boolean result = false;
    final int bitIndex = parent.indexToItemIndex(index);
    final int pageIndex = parent.indexToPageIndex(index);
    final BitSetPage page = getPage(pageIndex, false);
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
    final int bitIndex = (int) (index & bitIndexMask);
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
    int pageIndex = parent.indexToPageIndex(fromIndex);
    final int lastPage = parent.indexToPageIndex(toIndex);
    int firstBit = (int)(fromIndex & bitIndexMask);
    final int lastBit = (int)(toIndex & bitIndexMask);
    final int end = parent.getPageSize() - 1;
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
    int pageIndex = parent.indexToPageIndex(fromIndex);
    final int lastPage = parent.indexToPageIndex(toIndex);
    int firstBit = (int)(fromIndex & bitIndexMask);
    final int lastBit = (int)(toIndex & bitIndexMask);
    final int end = parent.getPageSize() - 1;
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
    int pageIndex = parent.indexToPageIndex(fromIndex);
    int bitIndex = parent.indexToItemIndex(fromIndex);
    for ( ; pageIndex < pageCount; ++pageIndex ) {
	final BitSetPage page = pages[pageIndex];
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
	result += pageIndex << parent.getLogPageSize();
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
    int pageIndex = parent.indexToPageIndex(fromIndex);
    int bitIndex = parent.indexToItemIndex(fromIndex);
    for ( ; pageIndex < pageCount; ++pageIndex ) {
      final BitSetPage page = pages[pageIndex];
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
      result += pageIndex << parent.getLogPageSize();
    }
    return result;
  }
}
