//
// Copyright 2006 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//

package com.xavax.event;

import java.util.List;
import java.util.Map;

import com.xavax.util.CollectionFactory;

/**
 * BroadcastHelper is a basic broadcaster of events. The development
 * of a broadcaster is simplified by delegating  all <i>Broadcaster</i>
 * methods to an instance of BroadcastHelper. Broadcast helper maintains
 * a <i>Map</i> of observers indexed by event type. Each map entry is a
 * <i>List</i> of observers for that event type.
 */
public class BroadcastHelper implements Broadcaster {
  /**
   * Construct a BroadcastHelper.
   */
  public BroadcastHelper()
  {
    observerMap = null;
  }

  /**
   * Attach an observer to this broadcaster's list of observers.
   *
   * @param type  the event type to be observed.
   * @param observer  the observer to be attached.
   */
  public void attach(int type, Observer observer)
  {
    if ( observerMap == null ) {
      observerMap = CollectionFactory.treeMap();
    }
    Integer t = new Integer(type);
    synchronized (this) {
      List<Observer> l = observerMap.get(t);
      if ( l == null ) {
	l = CollectionFactory.arrayList();
	observerMap.put(t, l);
      }
      l.add(observer);
    }
  }

  /**
   * Detach an observer from this broadcaster's list of observers.
   *
   * @param type  the event type being observed.
   * @param observer  the observer to be detached.
   */
  public void detach(int type, Observer observer)
  {
    if ( observerMap != null ) {
      Integer t = new Integer(type);
      synchronized (this) {
	List<Observer> l = observerMap.get(t);
	if ( l != null ) {
	  l.remove(observer);
	}
      }
    }
  }

  /**
   * Broadcast an event to the observers of the event type.
   *
   * @param event   the event to be broadcast.
   */
  public void broadcast(Event event)
  {
    if ( observerMap != null ) {
      int type = event.type();
      Integer t = new Integer(type);
      synchronized (this) {
	List<Observer> list = observerMap.get(t);
	if ( list != null ) {
	  for ( Observer observer : list ) {
	    observer.notify(event);
	  }
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
  public void broadcast(Broadcaster source, Event event)
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
  public void broadcast(Broadcaster source, int type)
  {
    Event event = new BasicEvent(type);
    broadcast(source, event);
  }

  protected Map<Integer,List<Observer>> observerMap;
}
