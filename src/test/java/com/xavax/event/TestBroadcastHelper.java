//
// Copyright 2004, 2013 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//
package com.xavax.event;

import org.testng.annotations.BeforeTest;
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

  private BroadcastHelper broadcaster = new BroadcastHelper();
  private final BasicEvent[] basicEvents = new BasicEvent[MAX_TYPES];
  private final TestEvent[] testEvents = new TestEvent[MAX_TYPES];
  private final SampleObserver[] observers = new SampleObserver[MAX_OBSERVERS];

  /**
   * Construct a TestBroadcastHelper.
   */
  @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
  public TestBroadcastHelper() {
    for ( int i= 0; i < MAX_OBSERVERS; ++i ) {
      observers[i] = new SampleObserver(broadcaster, i);
    }
    for ( int i= 0; i < MAX_TYPES; ++i ) {
      basicEvents[i] = new BasicEvent(i);
      testEvents[i] = new TestEvent(i, "Event" + i);
    }
  }

  @BeforeTest
  public void setUp() {
    broadcaster = new BroadcastHelper();
  }
  
  /**
   * Test broadcast methods.
   */
  @Test
  public void testBroadcast()
  {
    for ( int i = 0; i < MAX_OBSERVERS; ++i ) {
      broadcaster.attach(i, observers[i]);
    }
    for ( int i = 0; i < MAX_COUNT; ++i ) {
      sendBasicEvents();
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
    sendBasicEvents();
    checkObservers(0);
  }

  @Test
  public void testExemplars() {
    resetObservers();
    for ( int i= 0; i < MAX_OBSERVERS; ++i ) {
      broadcaster.attach(testEvents[i], observers[i]);
    }
    for ( int i = 0; i < MAX_COUNT; ++i ) {
      sendBasicEvents();
    }
    checkObservers(0);
    for ( int i = 0; i < MAX_COUNT; ++i ) {
      sendTestEvents();
    }
    checkObservers(MAX_COUNT);
    resetObservers();
    for ( int i= 0; i < MAX_OBSERVERS; ++i ) {
      broadcaster.detach(testEvents[i], observers[i]);
    }
    sendTestEvents();
    checkObservers(0);
    resetObservers();
    for ( int i= 0; i < MAX_OBSERVERS; ++i ) {
      broadcaster.attach(testEvents[i], observers[i]);
    }
    for ( int i = 0; i < MAX_COUNT; ++i ) {
      sendTestEvents();
    }
    checkObservers(MAX_COUNT);
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
  private void sendBasicEvents() {
    for ( final BasicEvent event : basicEvents ) {
	broadcaster.broadcast(event);
    }
  }

  /**
   * Send all test events.
   */
  private void sendTestEvents() {
    for ( final TestEvent event : testEvents ) {
	broadcaster.broadcast(event);
    }
  }

  public static class TestEvent extends BasicEvent {
    private final String name;

    public TestEvent(final int type, final String name) {
      super(type);
      this.name = name;
    }

    public String getName() {
      return name;
    }

    @Override
    public boolean matches(final Event event) {
      return super.matches(event) && event instanceof TestEvent &&
	  name != null && name.equals(((TestEvent) event).getName());
    }
  }
}
