//
// Copyright 2006 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//

package com.xavax.event;

import com.xavax.util.Dates;

/**
 * TimeEvent extends BasicEvent by adding a timestamp as well as a
 * general purpose "info" field that can store any object.
 */
public class TimeEvent extends BasicEvent {

  protected long timestamp;
  protected Object info;

  /**
   * Construct a TimeEvent with the specified type.
   *
   * @param type  the type of this event.
   * @param info  arbitrary object to associate with this event.
   */
  public TimeEvent(int type, Object info)
  {
    this(type, info, System.currentTimeMillis());
  }

  /**
   * Construct a TimeEvent with the specified type.
   *
   * @param type  the type of this event.
   * @param info  arbitrary object to associate with this event.
   * @param timestamp  the timestamp for this event.
   */
  public TimeEvent(int type, Object info, long timestamp)
  {
    super(type);
    this.info = info;
    this.timestamp = timestamp;
  }

  /**
   * Returns the info for this event.
   *
   * @return the info for this event.
   */
  public Object info()
  {
    return this.info;
  }

  /**
   * Sets the info for this event.
   *
   * @param info  arbitrary object to associate with this event.
   */
  public void info(Object info)
  {
    this.info = info;
  }

  /**
   * Returns the timestamp of this event.
   *
   * @return the timestamp of this event.
   */
  public long timestamp()
  {
    return this.timestamp;
  }

  /**
   * Sets the timestamp of this event.
   *
   * @param timestamp  the timestamp of this event.
   */
  public void timestamp(long timestamp)
  {
    this.timestamp = timestamp;
  }

  /**
   * Returns a string representation of this event.
   *
   * @return a string representation of this event.
   */
  public String toString()
  {
    String s = info != null ? info.toString() : "null";
    String result = "TE(" + type + "," + Dates.timestamp(timestamp) + "," + s + ")";
    return result;
  }
}
