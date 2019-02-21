package com.xavax.event;

import static org.testng.Assert.*;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test cases for the EventQueue class.
 */
public class EventQueueTest {
  private final static int MAX_EVENT_TYPES = 10;
  private final static int MAX_EVENTS = 5;

  private EventQueue queue;

  /**
   * Common test setup.
   */
  @BeforeMethod
  public void setUp() {
    queue = new EventQueue();
  }

  /**
   * Add some test events to the queue.
   */
  @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
  private void addEvents() {
    TimeEvent event = null;
    for ( int type = 0; type < MAX_EVENT_TYPES; ++type ) {
      final Integer info = Integer.valueOf(type);
      for ( int count = 0; count < MAX_EVENTS; ++count ) {
	event = new TimeEvent(type, info);
	queue.enqueue(event);
      }
    }
  }

  /**
   * Test event queues.
   */
  @Test
  public void testEventQueue()
  {
    queue.enqueue(null);
    // Dequeue invalid type.
    assertNull(queue.dequeue(MAX_EVENT_TYPES));
    addEvents();
    Event event = null;
    for ( int type = 0; type < MAX_EVENT_TYPES; ++type ) {
      int count = 0;
      do {
	event = queue.dequeue(type);
	if ( event != null ) {
	  assertEquals(event.type(), type);
	  ++count;
	}
      } while ( event != null );
      assertEquals(MAX_EVENTS, count);
    }
  }

  /**
   * Test the flush methods.
   */
  @Test
  public void testFlush() {
    // Flush an invalid type.
    queue.flush(MAX_EVENT_TYPES);
    addEvents();
    assertNotNull(queue.dequeue(0));
    queue.flush(0);
    assertNull(queue.dequeue(0));
    assertNotNull(queue.dequeue(1));
    queue.flush();
    assertNull(queue.dequeue(1));
    assertNull(queue.dequeue(2));
  }
}
