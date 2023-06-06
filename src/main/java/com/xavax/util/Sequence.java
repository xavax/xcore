package com.xavax.util;

/**
 * Sequence is an abstract base class for sequence generators.
 *
 * @param <T>  type of the sequence values.
 */
public abstract class Sequence<T> extends AbstractJoinableObject {
  private final String CURRENT = "current";

  protected T current;

  /**
   * Construct a Sequence with a random initial value.
   */
  public Sequence() {
    current = random();
  }

  /**
   * Construct a Sequence with the specified initial value.
   *
   * @param initialValue  the initial value of the sequence.
   */
  public Sequence(final T initialValue) {
    current = initialValue;
  }

  /**
   * Returns the next value of the sequence.
   *
   * @return the next value of the sequence.
   */
  public T next() {
    T result;
    synchronized (this) {
      result = current;
      current = getNext();
    }
    return result;
  }

  /**
   * Returns a random value.
   *
   * @return a random value.
   */
  abstract T random();

  /**
   * Returns the next value in the sequence.
   *
   * @return the next value in the sequence.
   */
  abstract T getNext();

  /**
   * Output this object to the specified joiner.
   *
   * @param joiner  the joiner to use for output.
   * @return this joiner.
   */
  @Override
  public Joiner doJoin(final Joiner joiner) {
    joiner.append(CURRENT, current);
    return joiner;
  }
}
