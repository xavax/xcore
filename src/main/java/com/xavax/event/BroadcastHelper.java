//
// Copyright 2006 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//

package com.xavax.event;

import java.util.List;
import java.util.Map;

import com.xavax.util.CollectionFactory;
import com.xavax.util.Joinable;
import com.xavax.util.Joiner;

/**
 * BroadcastHelper is a basic broadcaster of events. The development
 * of a broadcaster is simplified by delegating  all <i>Broadcaster</i>
 * methods to an instance of BroadcastHelper. Broadcast helper maintains
 * a <i>Map</i> of observers indexed by event type. Each map entry is a
 * <i>List</i> of observers for that event type.
 */
public class BroadcastHelper implements Broadcaster, Joinable {
  protected Map<Integer,List<Observer>> observerMap;

  /**
   * Construct a BroadcastHelper.
   */
  public BroadcastHelper()
  {
    observerMap = CollectionFactory.treeMap();
  }

  /**
   * Attach an observer to this broadcaster's list of observers.
   *
   * @param type  the event type to be observed.
   * @param observer  the observer to be attached.
   */
  public void attach(final int type, final Observer observer)
  {
    final Integer key = Integer.valueOf(type);
    synchronized (this) {
      List<Observer> observers = observerMap.get(key);
      if ( observers == null ) {
	observers = CollectionFactory.arrayList();
	observerMap.put(key, observers);
      }
      observers.add(observer);
    }
  }

  /**
   * Detach an observer from this broadcaster's list of observers.
   *
   * @param type  the event type being observed.
   * @param observer  the observer to be detached.
   */
  public void detach(final int type, final Observer observer)
  {
    final Integer key = Integer.valueOf(type);
    synchronized ( this ) {
      final List<Observer> observers = observerMap.get(key);
      if ( observers != null ) {
	observers.remove(observer);
      }
    }
  }

  /**
   * Broadcast an event to the observers of the event type.
   *
   * @param event   the event to be broadcast.
   */
  public void broadcast(final Event event)
  {
    final int type = event.type();
    final Integer key = Integer.valueOf(type);
    synchronized ( this ) {
      final List<Observer> observers = observerMap.get(key);
      if ( observers != null ) {
	for ( final Observer observer : observers ) {
	  observer.notify(event);
	}
      }
    }
  }

  /**
   * Broadcast an event to the observers of the event type. The
   * source parameter overrides the source specified in the event.
   *
   * @param source  the broadcaster source.
   * @param event   the event to be broadcast.
   */
  public void broadcast(final Broadcaster source, final Event event)
  {
    event.source(source);
    broadcast(event);
  }

  /**
   * Creates and broadcasts a BasicEvent of the specified type.
   *
   * @param source  the broadcaster source.
   * @param type    the type of event to be broadcast.
   */
  public void broadcast(final Broadcaster source, final int type)
  {
    broadcast(source, new BasicEvent(type));
  }

  /**
   * Returns a string representation of this BroadcastHelper.
   *
   * @return a string representation of this BroadcastHelper.
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
    joiner.appendRaw("BroadcastHelper");
    return joiner;
  }
}
