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
public class TestBroadcastHelper  {
  private final static int MAX_COUNT = 10;
  private final static int MAX_OBSERVERS = 5;
  private final static int MAX_TYPES = 5;
  private final static String EXPECT1 = "BroadcastHelper";

  private final BroadcastHelper broadcaster = new BroadcastHelper();
  private final BasicEvent[] events = new BasicEvent[5];
  private final SampleObserver[] observers = new SampleObserver[5];

  /**
   * Construct a TestBroadcastHelper.
   */
  public TestBroadcastHelper() {
    for ( int i= 0; i < MAX_OBSERVERS; ++i ) {
      observers[i] = new SampleObserver(broadcaster, i);
      events[i] = new BasicEvent(i);
    }
  }

  /**
   * Test broadcast methods.
   */
  @Test
  public void testBroadcast()
  {
    for ( int i = 0; i < MAX_COUNT; ++i ) {
      sendEvents();
    }
    checkObservers(MAX_COUNT);
    resetObservers();
    for ( int i = 0; i < MAX_COUNT; ++i ) {
      for ( int type = 0; type < MAX_TYPES; ++type ) {
	broadcaster.broadcast(broadcaster, type);
      }
    }
    checkObservers(MAX_COUNT);
    resetObservers();
    for ( int i= 0; i < 5; ++i ) {
      broadcaster.detach(i, observers[i]);
    }
    sendEvents();
    checkObservers(0);
    for ( int i= 0; i < 5; ++i ) {
      broadcaster.attach(i, observers[i]);
    }
    for ( int i = 0; i < MAX_COUNT; ++i ) {
      sendEvents();
    }
    checkObservers(MAX_COUNT);
    resetObservers();
    broadcaster.detach(MAX_TYPES, observers[0]);
    broadcaster.broadcast(broadcaster, MAX_TYPES);
    checkObservers(0);
  }

  /**
   * Test the toString method.
   */
  @Test
  public void testToString() {
    assertEquals(broadcaster.toString(), EXPECT1);
  }

  /**
   * Check each observer count.
   */
  private void checkObservers(final int count) {
    for ( final SampleObserver observer : observers ) {
      assertEquals(count, observer.count());
    }
  }

  /**
   * Reset each observer.
   */
  private void resetObservers() {
    for ( final SampleObserver observer : observers ) {
      observer.reset();
    }
  }

  /**
   * Send all events.
   */
  private void sendEvents() {
    for ( final BasicEvent event : events ) {
	broadcaster.broadcast(event);
    }
  }
}
