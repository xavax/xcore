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
 * EventQueue manages a queue of events. Rather than broadcasting the events to
 * a list of observers, EventQueue saves the event in a FIFO queue until a
 * client requests the next event of a specific type. EventQueue maintains a map
 * of FIFO queues keyed by the event type.
 */
public class EventQueue {
  protected Map<Integer, List<Event>> eventMap;

  /**
   * Construct an EventQueue.
   */
  public EventQueue()
  {
    eventMap = null;
  }

  /**
   * Enqueue an event. The event becomes the tail of a FIFO queue of events of
   * the same type.
   *
   * @param event the event to be enqueued.
   */
  public void enqueue(final Event event)
  {
    if ( event != null ) {
      final Integer key = new Integer(event.type());
      synchronized ( this ) {
	if ( eventMap == null ) {
	  eventMap = CollectionFactory.hashMap();
	}
	List<Event> observers = eventMap.get(key);
	if ( observers == null ) {
	  observers = CollectionFactory.linkedList();
	  eventMap.put(key, observers);
	}
	observers.add(event);
      }
    }
  }

  /**
   * Dequeue and return an event of the specified type. The head of the FIFO
   * queue of events of the specified type is removed from the queue and
   * returned. If there are no events in the queue, return null.
   *
   * @param type the type of event to dequeue.
   * @return an event of the specified type, or null if there are no events
   *         waiting in the queue.
   */
  public Event dequeue(final int type)
  {
    Event result = null;
    final Integer key = new Integer(type);
    synchronized ( this ) {
      if ( eventMap != null ) {
	final List<Event> observers = eventMap.get(key);
	if ( observers != null && !observers.isEmpty() ) {
	  result = (Event) observers.remove(0);
	}
      }
    }
    return result;
  }

  /**
   * Flush all events of the specified type.
   *
   * @param type the type of event to dequeue.
   */
  public void flush(final int type)
  {
    if ( eventMap != null ) {
      synchronized ( this ) {
	final Integer key = new Integer(type);
	eventMap.remove(key);
      }
    }
  }

  /**
   * Flush all events.
   */
  public void flush()
  {
    synchronized ( this ) {
      eventMap = null;
    }
  }
}
