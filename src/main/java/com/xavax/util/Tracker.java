package com.xavax.util;

/**
 * Tracker keeps track of the levels of nested items and
 * whether we currently need a separator at each level.
 */
public class Tracker {
  private final static int MAX_LEVEL = Long.SIZE - 1;

  private int level;
  private long flags;
  private String[] stack = new String[MAX_LEVEL + 1];
  private final Joiner parent;

  /**
   * Construct a Tracker with the specified separator.
   *
   * @param parent  the parent Joiner.
   */
  @SuppressWarnings("PMD.AccessorMethodGeneration")
  public Tracker(final Joiner parent) {
    this.parent = parent;
    stack[0] = parent.getFormat().getSeparator();
  }

  /**
   * Increment the level and push a separator onto the stack.
   *
   * @param separator  the new separator.
   */
  @SuppressWarnings("PMD.AccessorMethodGeneration")
  public void push(final String separator) {
    if ( level < MAX_LEVEL ) {
	stack[++level] = separator == null ? parent.getFormat().getSeparator() : separator;
	clearFlag();
    }
  }

  /**
   * Decrement the level.
   */
  public void pop() {
    if ( level > 0 ) {
	--level;
	setFlag();
    }
  }

  /**
   * Add a separator to the output if needed.
   */
  public void addSeparator() {
    if ( isSet() ) {
	parent.appendRaw(stack[level]);
	clearFlag();
    }
  }

  /**
   * Sets the separator for the current level.
   *
   * @param separator  the new separator.
   */
  public void setSeparator(final String separator) {
     stack[level] = separator;
  }

  /**
   * Set the flag for this level to indicate an item was
   * added and the output now needs a separator.
   */
  public void setFlag() {
    flags |= 1 << level;
  }

  /**
   * Clear the flag for this level to indicate a separator
   * is not needed.
   */
  public void clearFlag() {
    flags &= ~(1 << level);
  }

  /**
   * Returns the flag for this level.
   * @return the flag for this level.
   */
  public boolean isSet() {
    return (flags & (1 << level)) != 0;
  }

  /**
   * Returns the current level. This is only for testing.
   * @return the current level.
   */
  int getLevel() {
    return level;
  }

  /**
   * Sets the level. This is only for testing.
   *
   * @param level  the new level.
   */
  void setLevel(final int level) {
    this.level = level;
  }
}
