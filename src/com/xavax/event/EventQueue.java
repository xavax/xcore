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
 * EventQueue manages a queue of events. Rather than broadcasting the
 * events to a list of observers, EventQueue saves the event in a FIFO
 * queue until a client requests the next event of a specific type.
 * EventQueue maintains a map of FIFO queues keyed by the event type.
 */
public class EventQueue {
  /**
   * Construct an EventQueue.
   */
  public EventQueue()
  {
    eventMap = null;
  }

  /**
   * Enqueue an event. The event becomes the tail of a FIFO queue
   * of events of the same type.
   *
   * @param event  the event to be enqueued.
   */
  public void enqueue(Event event)
  {
    if ( event != null ) {
      if ( eventMap == null ) {
	eventMap = CollectionFactory.hashMap();
      }
      Integer t = new Integer(event.type());
      synchronized (this) {
	List<Event> l = eventMap.get(t);
	if ( l == null ) {
	  l = CollectionFactory.linkedList();
	  eventMap.put(t, l);
	}
	l.add(event);
      }
    }
  }

  /**
   * Dequeue and return an event of the specified type. The head of
   * the FIFO queue of events of the specified type is removed from
   * the queue and returned. If there are no events in the queue,
   * return null.
   *
   * @param  type  the type of event to dequeue.
   * @return an event of the specified type, or null if there are no
   *         events waiting in the queue.
   */
  public Event dequeue(int type)
  {
    Event result = null;
    if ( eventMap != null ) {
      Integer t = new Integer(type);
      synchronized (this) {
	List<Event> l = eventMap.get(t);
	if ( l != null ) {
	  if ( !l.isEmpty() ) {
	    result = (Event) l.remove(0);
	  }
	}
      }
    }
    return result;
  }

  /**
   * Flush all events of the specified type.
   *
   * @param  type  the type of event to dequeue.
   */
  public void flush(int type)
  {
    if ( eventMap != null ) {
      synchronized (this) {
	Integer t = new Integer(type);
	eventMap.remove(t);
      }
    }
  }

  /**
   * Flush all events.
   */
  public void flush()
  {
    synchronized (this) {
      eventMap = null;
    }
  }

  protected Map<Integer,List<Event>> eventMap;
}
