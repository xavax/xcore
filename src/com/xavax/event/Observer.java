//
// Copyright 2006 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//

package com.xavax.event;

/**
 * Observer is an interface that should be implemented by classes that
 * need to observe events.
 */
public interface Observer {
  /**
   * Called by a <i>Broadcaster</i> to notify this observer of an
   * <i>Event</i>.
   *
   * @param event  the event being broadcast.
   */
  public void notify(Event event);
}
