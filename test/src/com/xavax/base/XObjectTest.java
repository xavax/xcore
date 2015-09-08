//
// Copyright 2010 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//

package com.xavax.base;

import org.apache.log4j.Logger;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test cases for XObject.
 *
 * @author alvitar@xavax.com
 */
public class XObjectTest extends XObject {
  private final static String FORMAT = "p1=[%s], p2=[%d] p3=[%f]";
  private final static String PARAM1 = "param1";

  @Mock
  private Logger logger;

  /**
   * Common set up for all test cases.
   */
  @BeforeMethod
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  /**
   * Test the debug logging method.
   */
  @Test
  public void testLogging()
  {
    Widget widget = new Widget(null);
    assertFalse(widget.debugEnabled());
    assertFalse(widget.traceEnabled());
    widget.hello();
    widget = new Widget(logger);
    widget.hello();
    verify(logger, times(0)).debug(any());
    verify(logger, times(0)).trace(any());
    assertFalse(widget.debugEnabled());
    assertFalse(widget.traceEnabled());
    when(logger.isDebugEnabled()).thenReturn(true);
    when(logger.isTraceEnabled()).thenReturn(true);
    widget.hello();
    verify(logger, times(2)).debug(any());
    verify(logger, times(2)).trace(any());
    assertTrue(widget.debugEnabled());
    assertTrue(widget.traceEnabled());
  }

  /**
   * Widget is an example use of XObject.
   */
  public static class Widget extends XObject {
    private final static Logger LOGGER = Logger.getLogger(Widget.class);

    /**
     * Construct a Widget.
     */
    public Widget() {
      super(LOGGER);
    }

    /**
     * Construct a Widget.
     */
    public Widget(final Logger logger) {
      super(logger);
    }

    /**
     * Writes a "hello" message to the log at debug level.
     */
    public void hello()
    {
      final String method = "hello";
      enter(method);
      debug(method, "a log message from Example");
      debug(method, FORMAT, PARAM1, 123, 5.678);
      leave(method, true);
    }
  }
}
