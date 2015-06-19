//
// Copyright 2004, 2013 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//
package com.xavax.event;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Test cases for classes in the event package.
 */
public class EventTest  {

  /**
   * Test Event methods.
   */
  @Test
  public void testEvents()
  {
    final Broadcaster broadcaster = new BroadcastHelper();
    final BasicEvent[] event = new BasicEvent[5];
    final SampleObserver[] observer = new SampleObserver[5];
    for ( int i= 0; i < 5; ++i ) {
      observer[i] = new SampleObserver(broadcaster, i+1);
      event[i] = new BasicEvent(i+1);
    }
    final int maxCount = 10;
    for ( int i = 0; i < maxCount; ++i ) {
      for ( int j = 0; j < 5; ++j ) {
	broadcaster.broadcast(event[j]);
      }
    }
    for ( int i = 0; i < 5; ++i ) {
      final int count = observer[i].count();
      assertEquals(maxCount, count);
    }
  }

  /**
   * Test event queues.
   */
  @Test
  public void testEventQueue()
  {
    final int maxCount = 10;
    TimeEvent event = null;
    final EventQueue queue = new EventQueue();
    for ( int i = 0; i < maxCount; ++i ) {
      final Integer info = new Integer(i);
      for ( int j = 0; j < 5; ++j ) {
	event = new TimeEvent(j, info);
	queue.enqueue(event);
      }
    }
    for ( int i = 0; i < 5; ++i ) {
      int count = 0;
      do {
	event = (TimeEvent) queue.dequeue(i);
	if ( event != null ) {
	  assertEquals(event.type(), i);
	  ++count;
	}
      } while ( event != null );
      assertEquals(maxCount, count);
    }
  }
}
