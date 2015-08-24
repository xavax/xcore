//
//Copyright 2014 by Xavax, Inc. All Rights Reserved.
//Use of this software is allowed under the Xavax Open Software License.
//http://www.xavax.com/xosl.html
//
package com.xavax.event;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Test cases for the TimeEvent class.
 */
public class TimeEventTest {
  private final static int EVENT_TYPE = 7;
  private final static long BIRTHDAY = -329916304000L;
  private final static String EXPECTED =
      "type: 7, source: <null>, info: info, timestamp: 1959-07-19T12:34:56.000";
  private final static String INFO = "info";

  private final BroadcastHelper broadcaster = new BroadcastHelper();

  /**
   * Test the constructors and accessor methods.
   */
  @Test
  public void testConstructors() {
    TimeEvent event = new TimeEvent(EVENT_TYPE, INFO, BIRTHDAY);
    assertTrue(event instanceof TimeEvent);
    assertEquals(event.type(), EVENT_TYPE);
    assertNull(event.source());
    event = new TimeEvent(EVENT_TYPE, INFO);
    assertNotNull(event);
  }

  /**
   * Test accessor methods.
   */
  @Test
  public void testAccessors() {
    final TimeEvent event = new TimeEvent(EVENT_TYPE, INFO);
    event.source(broadcaster);
    assertEquals(event.source(), broadcaster);
    assertEquals(event.info(), INFO);
    event.info(null);
    assertNull(event.info());
    event.timestamp(BIRTHDAY);
    assertEquals(event.timestamp(), BIRTHDAY);
  }

  /**
   * Test the toString method.
   */
  @Test
  public void testToString() {
    final TimeEvent event = new TimeEvent(EVENT_TYPE, INFO, BIRTHDAY);
    final String result = event.toString();
    assertEquals(result, EXPECTED);
  }
}
