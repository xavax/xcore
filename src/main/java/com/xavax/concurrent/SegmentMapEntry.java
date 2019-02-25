package com.xavax.concurrent;

import com.xavax.util.AbstractJoinableObject;
import com.xavax.util.Joiner;

/**
 * SegmentMapEntry encapsulates an entry in the segment map (a reference to a
 * segment). It reduces thread contention by allowing us to synchronize on one
 * entry rather than the entire segment map.
 */
public class SegmentMapEntry<T extends AbstractPage> extends AbstractJoinableObject {
  final static int SEGMAP_BUFFER_SIZE = 8192;

  private AbstractSegment<T> segment;

  /**
   * Get the segment for this map entry.
   *
   * @return the segment.
   */
  public AbstractSegment<T> get() {
    AbstractSegment<T> result;
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
  public void set(final AbstractSegment<T> segment) {
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
