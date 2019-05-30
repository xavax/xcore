//
// Copyright 2006 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//

package com.xavax.event;

/**
 * Broadcaster is an interface that should be implemented by classes
 * that will broadcast events to observers.
 */
public interface Broadcaster {
  /**
   * Attach an observer to this broadcaster's list of observers.
   * Any event matching the type will be observed.
   *
   * @param eventType  the event type to be observed.
   * @param observer   the observer to be notified of events.
   */
  void attach(int eventType, Observer observer);

  /**
   * Attach an observer to this broadcaster's list of observers.
   * Only events matching the exemplar will be observed.
   *
   * @param exemplar  the type of event to be observed.
   * @param observer  the observer to be notified of events.
   */
  void attach(Event exemplar, Observer observer);

  /**
   * Broadcast an event to the observers of the event type.
   *
   * @param event   the event to be broadcast.
   */
  void broadcast(Event event);

  /**
   * Detach an observer from this broadcaster's list of observers.
   *
   * @param type  the event type being observed.
   * @param observer  the observer to be detached.
   */
  void detach(int type, Observer observer);
}
