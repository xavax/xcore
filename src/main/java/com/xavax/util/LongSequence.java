package com.xavax.util;

import java.util.Random;

/**
 * LongSequence generates a sequence of long values.
 */
public class LongSequence extends Sequence<Long> {
  final static Random random = new Random();

  /**
   * Returns a random value.
   *
   * @return a random value.
   */
  @Override
  Long random() {
    return random.nextLong();
  }

  /**
   * Returns the next value in the sequence.
   *
   * @return the next value in the sequence.
   */
  @Override
  Long getNext() {
    return ++current;
  }
}
