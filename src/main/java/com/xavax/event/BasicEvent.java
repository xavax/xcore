//
// Copyright 2006 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//

package com.xavax.event;

import com.xavax.util.Joinable;
import com.xavax.util.Joiner;

/**
 * BasicEvent serves as a base class for events used by broadcasters
 * and observers and can also be used as a concrete class for simple
 * events that require no additional information other than the type
 * and source of the event.
 */
public class BasicEvent implements Event, Joinable {
  protected final int type;
  protected Broadcaster source;

  /**
   * Construct a BasicEvent with the specified type.
   *
   * @param type  the type of this event.
   */
  public BasicEvent(final int type)
  {
    this(null, type);
  }

  /**
   * Construct a BasicEvent with the specified type and source.
   *
   * @param source  the source of this event. 
   * @param type  the type of this event.
   */
  public BasicEvent(final Broadcaster source, final int type)
  {
    this.type = type;
    this.source = source;
  }

  /**
   * Returns the type of this event.
   *
   * @return the type of this event.
   */
  public int type()
  {
    return this.type;
  }

  /**
   * Returns the source of this event.
   *
   * @return the source of this event.
   */
  public Broadcaster source()
  {
    return this.source;
  }

  /**
   * Sets the source of this event.
   *
   * @param source  the source of this event.
   */
  public void source(final Broadcaster source)
  {
    this.source = source;
  }

  /**
   * Returns a string representation of this event.
   *
   * @return a string representation of this event.
   */
  public String toString()
  {
    return join(Joiner.create()).toString();
  }

  /**
   * Join this object to the specified joiner.
   *
   * @param joiner  the joiner to use.
   * @return the joiner.
   */
  public Joiner join(final Joiner joiner) {
    joiner.appendField("type", type)
          .appendField("source", source);
    return joiner;
  }
}
