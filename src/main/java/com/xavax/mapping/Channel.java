package com.xavax.mapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.xavax.util.AbstractJoinableObject;
import com.xavax.util.Joiner;

/**
 * Channel models a source or destination for mapping of annotated
 * Java objects.
 */
public class Channel extends AbstractJoinableObject {
  private final static String CLASSNAME = Channel.class.getSimpleName();
  private final static String NAME = "name";
  private final static Logger logger = LoggerFactory.getLogger(Channel.class);

  protected String name;

  /**
   * Construct a Channel.
   *
   * @param name  the name of the Channel.
   */
  public Channel(final String name) {
    this.name = name;
  }

  /**
   * Returns the name of this channel.
   *
   * @return the name of this channel.
   */
  public String getName() {
    return name;
  }

  /**
   * Output this object to the specified joiner.
   *
   * @param joiner  the joiner to use for output.
   * @return this joiner.
   */
  @Override
  public Joiner doJoin(final Joiner joiner) {
    joiner.append(NAME, name);
    return joiner;
  }
}
