//
// Copyright 2006 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//

package com.xavax.event;

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;

import com.xavax.util.CollectionFactory;

/**
 * EventQueue manages a queue of events. Rather than broadcasting the events to
 * a list of observers, EventQueue saves the event in a FIFO queue until a
 * client requests the next event of a specific type. EventQueue maintains a map
 * of FIFO queues keyed by the event type.
 */
public class EventQueue {
  final ReentrantLock mapLock;
  final protected Map<Integer, ConcurrentLinkedQueue<Event>> queueMap;

  /**
   * Construct an EventQueue.
   */
  public EventQueue()
  {
    mapLock = new ReentrantLock();
    queueMap = CollectionFactory.hashMap();
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
      final ConcurrentLinkedQueue<Event> queue = getQueue(event.type(), true);
      queue.add(event);
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
    final ConcurrentLinkedQueue<Event> queue = getQueue(type, false);
    if ( queue != null ) {
      result = queue.poll();
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
    final ConcurrentLinkedQueue<Event> queue = getQueue(type, false);
    if ( queue != null ) {
      queue.clear();
    }
  }

  /**
   * Flush all events.
   */
  public void flush()
  {
    for ( final ConcurrentLinkedQueue<Event> queue : queueMap.values() ) {
      queue.clear();
    }
  }

  /**
   * Get the specified queue from the queue map. If the queue does
   * not exist and the create flag is true, create it.
   *
   * @param type    the event type.
   * @param create  true if a non-existent queue should be created.
   * @return the specified queue from the queue map.
   */
  private ConcurrentLinkedQueue<Event> getQueue(final int type, final boolean create) {
    final Integer key = new Integer(type);
    ConcurrentLinkedQueue<Event> queue = queueMap.get(key);
    if ( queue == null && create ) {
      mapLock.lock();
      queue = queueMap.get(key);
      if ( queue == null ) {
	queue = CollectionFactory.concurrentLinkedQueue();
	queueMap.put(key, queue);
      }
      mapLock.unlock();
    }
    return queue;
  }
}