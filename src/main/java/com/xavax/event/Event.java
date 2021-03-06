//
// Copyright 2006 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//

package com.xavax.event;

/**
 * Event is the interface for objects used by broadcasters and observers
 * to communicate events.
 */
public interface Event {
  /**
   * Returns the type of this event.
   *
   * @return the type of this event.
   */
  int type();

  /**
   * Returns the source of this event.
   *
   * @return the source of this event.
   */
  Broadcaster source();

  /**
   * Sets the source of this event.
   *
   * @param source  the source of this event.
   */
  void source(Broadcaster source);
}
