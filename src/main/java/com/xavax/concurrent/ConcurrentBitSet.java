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
import com.xavax.util.AbstractJoinableObject;
import com.xavax.util.Joinable;
import com.xavax.util.Joiner;

import static com.xavax.concurrent.ConcurrentBitSetConstants.*;

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
public class ConcurrentBitSet extends AbstractJoinableObject implements Joinable {

  private int logSegmentSize;
  private int currentMapSize;
  private long segmentMask;
  // private long segmentSize;
  // private long maxBitIndex;
  final InternalMetrics metrics = new InternalMetrics();
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
  @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
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
    final BitSetSegment segment = getSegment(index, false);
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
    final BitSetSegment segment = getSegment(index, value);
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
      throw new RangeException(0, Long.MAX_VALUE, fromIndex);
    }
    // TODO: finish implementing this method.    
    return 0;
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
  BitSetSegment getSegment(final long index, final boolean require) {
    final int segmentIndex = (int) index >>> logSegmentSize;
    if ( segmentIndex > currentMapSize ) {
      resize(segmentIndex);
    }
    final SegmentMapEntry entry = segmentMap[segmentIndex];
    BitSetSegment segment = entry.get();
    if ( segment == null && require ) {
      synchronized ( entry ) {
	segment = entry.get();
	if ( segment == null ) {
	  segment = new BitSetSegment(this, logSegmentSize);
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
  @Override
  public String toString() {
    return doJoin(Joiner.create(BITSET_BUFFER_SIZE)).toString();
  }

  /**
   * Join this object to the specified joiner.
   *
   * @param joiner  the joiner to use.
   * @return the joiner.
   */
  @Override
  public Joiner doJoin(final Joiner joiner) {
    joiner.append("currentMapSize", currentMapSize)
          .append("logSegmentSize", logSegmentSize)
          .append("segmentMap", (Object[]) segmentMap);
    return joiner;
  }

  /**
   * SegmentMapEntry encapsulates an entry in the segment map (a reference to a
   * segment). It reduces thread contention by allowing us to synchronize on one
   * entry rather than the entire segment map.
   */
  static class SegmentMapEntry extends AbstractJoinableObject implements Joinable {
    final static int SEGMAP_BUFFER_SIZE = 8192;

    private BitSetSegment segment;

    /**
     * Get the segment for this map entry.
     *
     * @return the segment.
     */
    public BitSetSegment get() {
      BitSetSegment result;
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
    public void set(final BitSetSegment segment) {
      synchronized (this) {
	this.segment = segment;
      }
    }

    /**
     * Returns a string representation of this map entry.
     *
     * @return a string representation of this map entry.
     */
    @Override
    public String toString() {
      return doJoin(Joiner.create(SEGMAP_BUFFER_SIZE)).toString();
    }

    /**
     * Join this object to the specified joiner.
     *
     * @param joiner  the joiner to use.
     * @return the joiner.
     */
    @Override
    public Joiner doJoin(final Joiner joiner) {
      joiner.append("segment", segment);
      return joiner;
    }
  }



  /**
   * Metrics is a public snapshot of the internal metrics.
   */
  public static class Metrics extends AbstractJoinableObject implements Joinable {
    private final static int METRICS_BUFFER_SIZE = 128;

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
    @Override
    public String toString() {
      // return toString(new StringBuilder(DEFAULT_BUFFER_SIZE)).toString();
      return doJoin(Joiner.create(METRICS_BUFFER_SIZE)).toString();
    }

    /**
     * Join this object to the specified joiner.
     *
     * @param joiner  the joiner to use.
     * @return the joiner.
     */
    @Override
    public Joiner doJoin(final Joiner joiner) {
      joiner.appendRaw("{ ")
	  .append("pc",  pagesCreated)
	  .append("sc",  segmentsCreated)
	  .append("sel", segmentMapLocks)
	  .append("ops", totalOperations)
	  .appendRaw(" }");
      return joiner;
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
