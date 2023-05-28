//
//Copyright 2014 by Xavax, Inc. All Rights Reserved.
//Use of this software is allowed under the Xavax Open Software License.
//http://www.xavax.com/xosl.html
//
package com.xavax.event;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test cases for the BasicEvent class.
 */
public class BasicEventTest {
  private final static int EVENT_TYPE = 7;
  private final static String EXPECTED = "type: 7, source: BroadcastHelper";

  private final BroadcastHelper broadcaster = new BroadcastHelper();

  /**
   * Test the constructors and accessor methods.
   */
  @Test
  public void testConstructors() {
    BasicEvent event = new BasicEvent(broadcaster, EVENT_TYPE);
    assertTrue(event instanceof BasicEvent);
    assertEquals(event.type(), EVENT_TYPE);
    assertEquals(event.source(), broadcaster);
    event = new BasicEvent(EVENT_TYPE);
    assertEquals(event.type(), EVENT_TYPE);
    assertNull(event.source());
    event.source(broadcaster);
    assertEquals(event.source(), broadcaster);
  }

  /**
   * Test the toString method.
   */
  @Test
  public void testToString() {
    final BasicEvent event = new BasicEvent(broadcaster, EVENT_TYPE);
    final String result = event.toString();
    assertEquals(result, EXPECTED);
  }
}
