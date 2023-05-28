package com.xavax.concurrent;

import com.xavax.util.Joiner;

import static com.xavax.concurrent.Constants.*;
import static com.xavax.util.Constants.*;

/**
 * BitSetPage encapsulates an array of bytes used to represent a portion
 * of a bit set.
 */
class BitSetPage extends AbstractPage {
  final static int BIT_MAP_INDEX_MASK = (1 << LOG2_BITS_PER_BYTE) - 1;
  final static int PAGE_BUFFER_SIZE = 8192;

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
   *
   * @param parent  the parent pageable object.
   */
  public BitSetPage(final ConcurrentBitSet parent) {
    super(parent);
    bits = new byte[parent.getPageSize() >> LOG2_BITS_PER_BYTE];
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
    for ( int i = firstByte; run && i < bits.length; ++i ) {
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
    for ( int i = firstByte; run && i < bits.length; ++i ) {
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
  @Override
  public String toString() {
    return doJoin(Joiner.create(PAGE_BUFFER_SIZE)).toString();
  }

  /**
   * Join this object to the specified joiner.
   *
   * @param joiner  the joiner to use.
   * @return the joiner.
   */
  @Override
  public Joiner doJoin(final Joiner joiner) {
    boolean first = true;
    joiner.appendRaw(LEFT_BRACKET_STRING);
    for ( final byte b : bits ) {
	if ( first ) {
	  first = false;
	}
	else {
	  joiner.appendRaw(PERIOD);
	}
	joiner.appendRaw(NIBBLES[(b & 0xF0) >>> 4])
	      .appendRaw(NIBBLES[b & 0x0F]);
    }
    joiner.appendRaw(RIGHT_BRACKET_STRING);
    return joiner;
  }
}

