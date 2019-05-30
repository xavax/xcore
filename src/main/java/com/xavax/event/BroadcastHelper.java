//
// Copyright 2006 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//

package com.xavax.event;

import java.util.Iterator;
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
  protected Map<Integer,List<Observation>> observerMap;

  /**
   * Construct a BroadcastHelper.
   */
  public BroadcastHelper()
  {
    observerMap = CollectionFactory.treeMap();
  }

  /**
   * Attach an observer to this broadcaster's list of observers.
   * Any event matching the type will be observed.
   *
   * @param eventType  the event type to be observed.
   * @param observer   the observer to be notified of events.
   */
  public void attach(final int eventType, final Observer observer)
  {
    attach(new Observation(eventType, observer));
  }

  /**
   * Attach an observer to this broadcaster's list of observers.
   * Only events matching the exemplar will be observed.
   *
   * @param exemplar  the type of event to be observed.
   * @param observer  the observer to be notified of events.
   */
  public void attach(final Event exemplar, final Observer observer) {
    attach(new Observation(exemplar, observer));
  }

  /**
   * Attach an observer to this broadcaster's list of observers.
   *
   * @param observation  the observation.
   */
  void attach(final Observation observation) {
    final Integer key = Integer.valueOf(observation.getType());
    synchronized (this) {
      List<Observation> observers = observerMap.get(key);
      if ( observers == null ) {
	observers = CollectionFactory.arrayList();
	observerMap.put(key, observers);
      }
      observers.add(observation);
    }
  }

  /**
   * Detach an observer from this broadcaster's list of observers.
   *
   * @param eventType  the event type being observed.
   * @param observer   the observer to be detached.
   */
  public void detach(final int eventType, final Observer observer)
  {
    detach(new Observation(eventType, observer));
  }

  /**
   * Detach an observer from this broadcaster's list of observers.
   *
   * @param exemplar  the type of event to be observed.
   * @param observer  the observer to be detached.
   */
  public void detach(final Event exemplar, final Observer observer) {
    detach(new Observation(exemplar, observer));
  }

  /**
   * Detach an observer from this broadcaster's list of observers.
   *
   * @param observation  the observation.
   */
  void detach(final Observation observation) {
    final Integer key = Integer.valueOf(observation.getType());
    List<Observation> observers = null;
    synchronized ( this ) {
      observers = observerMap.get(key);
    }
    if ( observers != null ) {
      synchronized ( observers ) {
	final Iterator<Observation> iterator = observers.iterator();
	while ( iterator.hasNext() ) {
	  final Observation candidate = iterator.next();
	  if ( candidate.matches(observation) ) {
	    iterator.remove();
	  }
	}
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
    List<Observation> observers = null;
    synchronized ( this ) {
      observers = observerMap.get(key);
    }
    if ( observers != null ) {
      synchronized ( observers ) {
	for ( final Observation observation : observers ) {
	  if ( observation.matches(event) ) {
	    final Observer observer = observation.getObserver();
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

  /**
   * Observation describes an observer and the type of
   * event it is observing.
   */
  public static class Observation {
    private final int type;
    private final Event exemplar;
    private final Observer observer;

    /**
     * Construct an observation.
     *
     * @param type      the event type observed.
     * @param observer  the observer.
     */
    public Observation(final int type, final Observer observer) {
      this.type = type;
      this.exemplar = null;
      this.observer = observer;
    }

    /**
     * Construct an observation.
     *
     * @param exemplar  the exemplar event.
     * @param observer  the observer.
     */
    public Observation(final Event exemplar, final Observer observer) {
      this.type = exemplar.type();
      this.exemplar = exemplar;
      this.observer = observer;
    }

    /**
     * Returns the matching event type.
     *
     * @return the matching event type.
     */
    public int getType() {
      return type;
    }

    /**
     * Returns the event exemplar.
     *
     * @return the event exemplar.
     */
    public Event getExemplar() {
      return exemplar;
    }

    /**
     * Returns the observer.
     *
     * @return the observer.
     */
    public Observer getObserver() {
      return observer;
    }

    public boolean matches(final Observation observation) {
      return type == observation.type &&
	  (exemplar == null || exemplar.matches(observation.exemplar));
    }

    public boolean matches(final Event event) {
      return type == event.type() &&
	  (exemplar == null || exemplar.matches(event));
    }
  }
}
