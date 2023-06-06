package com.xavax.mapping;

import com.xavax.util.AbstractJoinableObject;
import com.xavax.util.Joiner;
import com.xavax.util.LongSequence;

public class MappingContext extends AbstractJoinableObject {
  private final static String ID = "id";

  final static LongSequence sequence = new LongSequence();

  private final long id;

  /**
   * Construct a MappingContext.
   */
  public MappingContext() {
    id = sequence.next();
  }

  /**
   * Returns the ID of this context.
   *
   * @return the ID of this context.
   */
  public long id() {
    return id;
  }

  /**
   * Output this object to the specified joiner.
   *
   * @param joiner  the joiner to use for output.
   * @return this joiner.
   */
  @Override
  public Joiner doJoin(final Joiner joiner) {
    joiner.append(ID, id);
    return joiner;
  }
}
