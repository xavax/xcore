//
// Copyright 2015, 2019 by Xavax, Inc. All Rights Reserved.
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

import static com.xavax.concurrent.Constants.*;

/**
 * PageableObject is a base class for large and possibly sparse entities such
 * as large maps or collections that need to be managed in parts to avoid
 * concurrency problems. The entity consists of segments and pages. Each
 * segment and each page can be locked without locking the entire entity.
 * A mechanism is provided to create segments and pages as needed, as well
 * as a functional mechanism to locate items in pages.
 *
 * Page Size is the number of objects (not bytes) per page.
 * Segment Size is the number of pages per segment.
 *
 * @param <T> the page type.
 */
@SuppressWarnings("PMD.TooManyMethods")
public abstract class AbstractPageableObject<T extends AbstractPage> extends AbstractJoinableObject {
  final static int BUFFER_SIZE = 32768;

  private final static String INITIAL_SIZE = "initial size";
  private final static String LOG2_PAGE_SIZE = "log2 page size";
  private final static String LOG2_SEGMENT_SIZE = "log2 segment dize";
  private final static String LOG2_TOTAL_SIZE = "log2 total size";
  
  private int currentMapSize;
  protected final int logPageSize;
  protected final int logSegmentSize;
  protected final int pageSize;
  protected final int pagesPerSegment;
  protected final long pageMask;
  protected final long segmentMask;
  protected final long segmentSize;

  private final ReentrantLock segmentMapLock;
  private final InternalMetrics metrics = new InternalMetrics();
  private SegmentMapEntry<T>[] segmentMap;

  /**
   * Construct a ConcurrentBitSet with the specified segment size and initial
   * bit set size.
   *
   * @param initialSize     the initial size of the entity in objects (not bytes).
   * @param logPageSize     log2 of the page size (objects per page).
   * @param logSegmentSize  log2 of the segment size (pages per segment).
   */
  @SuppressWarnings("unchecked")
  protected AbstractPageableObject(final long initialSize, 
                           final int logPageSize,
                           final int logSegmentSize) {
    if ( initialSize <= 0 ) {
      throw new RangeException(1, Long.MAX_VALUE, initialSize, INITIAL_SIZE);
    }
    if ( logPageSize <= 0 ) {
      throw new RangeException(1, LOG2_MAX_SIZE, logPageSize, LOG2_PAGE_SIZE);
    }
    if ( logSegmentSize <= 0 ) {
      throw new RangeException(1, LOG2_MAX_SIZE, logSegmentSize, LOG2_SEGMENT_SIZE);
    }
    final int logTotalSize = logPageSize + logSegmentSize;
    if ( logTotalSize  > LOG2_MAX_SIZE ) {
      throw new RangeException(1, LOG2_MAX_SIZE, logTotalSize, LOG2_TOTAL_SIZE);
    }
    this.logPageSize = logPageSize;
    this.logSegmentSize = logSegmentSize;
    pageSize = 1 << logPageSize;
    pageMask = pageSize - 1;
    pagesPerSegment = 1 << (logSegmentSize - logPageSize);
    segmentSize = 1 << logSegmentSize;
    segmentMask = (segmentSize - 1) & ~pageMask;
    final int size = (int) (initialSize + segmentSize - 1) >>> logSegmentSize;
    currentMapSize = size;
    segmentMapLock = new ReentrantLock();
    segmentMap = (SegmentMapEntry<T>[]) new SegmentMapEntry[size];
    initMap(segmentMap, 0, size);
  }

  /**
   * Initialize a portion of a segment map with new segment map entries.
   *
   * @param map    the map to be updated.
   * @param start  the starting map index.
   * @param end    the ending map index.
   */
  @SuppressWarnings({ "unchecked", "rawtypes", "PMD.AvoidInstantiatingObjectsInLoops" })
  private void initMap(SegmentMapEntry<T>[] map, final int start, final int end) {
    for ( int i = start; i < end; ++i ) {
      final SegmentMapEntry<T> entry = new SegmentMapEntry();
      map[i] = entry;
    }
  }

  /**
   * Returns a new segment.
   *
   * @param parent  the parent pageable object.
   * @return a new segment.
   */
  abstract AbstractSegment<T> createSegment(AbstractPageableObject<T> parent);
  
  /**
   * Returns the segment containing the specified object. If the segment does
   * not exist and require is true, create a new segment.
   *
   * @param index    the desired segment.
   * @param require  true if a nonexistent segment should be created.
   * @return the segment containing the specified object.
   */
  AbstractSegment<T> getSegment(final int index, final boolean require) {
    if ( index > currentMapSize ) {
      resize(index);
    }
    final SegmentMapEntry<T> entry = segmentMap[index];
    AbstractSegment<T> segment = entry.get();
    if ( segment == null && require ) {
      synchronized ( entry ) {
	segment = entry.get();
	if ( segment == null ) {
	  segment = createSegment(this);
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
   * @param size  the new minimum size.
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
	final SegmentMapEntry<T>[] map = Arrays.copyOf(segmentMap, newSize);
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
   * Verify that the input has only one bit set and return log2 of
   * the input or zero if more than one bit was set in the input.
   *
   * @param input  the input value.
   * @return log2 of the input.
   */
  public int log2(final long input) {
    int result = 0;
    // This is a safe cast because the max is Long.SIZE which is
    // typically 64 and far less than Integer.MAX.
    final int tmp = (int) Long.lowestOneBit(input);
    if ( tmp == input ) {
      result = Integer.numberOfTrailingZeros(tmp);
    }
    return result;
  }

  /**
   * Returns the item index within a page for the specified item.
   *
   * @param index  the global index of the item.
   * @return the item index within a page for the specified item.
   */
  public int indexToItemIndex(final long index) {
    return (int)(index & pageMask);
  }

  /**
   * Returns the page index within a segment for the specified item.
   *
   * @param index  the global item index.
   * @return the page index within a segment for the specified item.
   */
  public int indexToPageIndex(final long index) {
    return (int) ((index >>> logPageSize) & pageMask);
  }

  /**
   * Returns the segment index for the specified item.
   *
   * @param index  the global item index.
   * @return the segment index for the specified item.
   */
  public int indexToSegmentIndex(final long index) {
    return (int) (index >>> logSegmentSize);
  }

  /**
   * Returns the number of items per page.
   *
   * @return the number of items per page.
   */
  public int getPageSize() {
    return pageSize;
  }

  /**
   * Returns log2 of the page size.
   *
   * @return log2 of the page size.
   */
  public int getLogPageSize() {
    return logPageSize;
  }

  /**
   * Returns the number of pages per segment.
   *
   * @return the number of pages per segment.
   */
  public int getPagesPerSegment() {
    return pagesPerSegment;
  }

  /**
   * Increment the counter for total operations (bit get and set operations).
   */
  public void incrementOperations() {
    metrics.incrementOperations();
  }

  /**
   * Increment the counter for pages created.
   */
  public void pageCreated() {
    metrics.pageCreated();
  }

  /**
   * Increment the counter for segment map locks.
   */
  public void segmentMapLocked() {
    metrics.segmentMapLocked();
  }

  /**
   * Increment the counter for segments created.
   */
  public void segmentCreated() {
    metrics.segmentCreated();
  }

  /**
   * Returns a snapshot of the performance metrics.
   *
   * @return a snapshot of the performance metrics.
   */
  public Metrics getMetrics() {
    return new Metrics(metrics);
  }

  /**
   * Returns a string representation of this bit set. NOTE: This is mainly for
   * testing and not recommended for general use.
   *
   * @return a string representation of this bit set.
   */
  @Override
  public String toString() {
    return doJoin(Joiner.create(BUFFER_SIZE)).toString();
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
   * InternalMetrics encapsulates the counters used to collect metrics on the
   * performance of PageableObject.
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
      joiner.append("pc",  pagesCreated)
	    .append("sc",  segmentsCreated)
	    .append("sel", segmentMapLocks)
	    .append("ops", totalOperations);
      return joiner;
    }
  }
}
