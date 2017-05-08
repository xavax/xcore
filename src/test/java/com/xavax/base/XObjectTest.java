//
// Copyright 2010 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//

package com.xavax.base;

import java.io.FileNotFoundException;

import org.apache.log4j.Level;
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
    Widget widget = new Widget();
    assertTrue(widget.debugEnabled());
    assertFalse(widget.traceEnabled());
    widget.hello1();
    widget = new Widget(null);
    assertFalse(widget.debugEnabled());
    assertFalse(widget.traceEnabled());
    widget = new Widget(logger);
    widget.hello1();
    verify(logger, times(0)).debug(any());
    assertFalse(widget.debugEnabled());
    when(logger.isDebugEnabled()).thenReturn(true);
    widget.hello1();
    verify(logger, times(2)).debug(any());
    assertTrue(widget.debugEnabled());
  }

  /**
   * Test the trace logging method.
   */
  @Test
  public void testTrace()
  {
    final Widget widget = new Widget(logger);
    widget.hello1();
    verify(logger, times(0)).trace(any());
    assertFalse(widget.traceEnabled());
    when(logger.isTraceEnabled()).thenReturn(true);
    widget.hello1();
    widget.hello2();
    widget.hello3();
    verify(logger, times(7)).trace(any());
    assertTrue(widget.traceEnabled());
  }

  /**
   * Test the info logging method.
   */
  @Test
  public void testInfo()
  {
    final Widget widget = new Widget(logger);
    when(logger.isEnabledFor(Level.INFO)).thenReturn(true);
    widget.testInfo();
    verify(logger, times(2)).info(any());
    verify(logger, times(2)).info(any(), any());
  }

  /**
   * Test the warn logging method.
   */
  @Test
  public void testWarn()
  {
    final Widget widget = new Widget(logger);
    when(logger.isEnabledFor(Level.WARN)).thenReturn(true);
    widget.testWarn();
    verify(logger, times(2)).warn(any());
    verify(logger, times(2)).warn(any(), any());
  }

  /**
   * Test the error logging method.
   */
  @Test
  public void testError()
  {
    final Widget widget = new Widget(logger);
    when(logger.isEnabledFor(Level.ERROR)).thenReturn(true);
    widget.testError();
    verify(logger, times(2)).error(any());
    verify(logger, times(2)).error(any(), any());
  }

  /**
   * Test the fatal logging method.
   */
  @Test
  public void testFatal()
  {
    final Widget widget = new Widget(logger);
    when(logger.isEnabledFor(Level.FATAL)).thenReturn(true);
    widget.testFatal();
    verify(logger, times(2)).fatal(any());
    verify(logger, times(2)).fatal(any(), any());
  }

  /**
   * Widget is an example use of XObject.
   */
  public static class Widget extends XObject {
    private final static String TEST_INFO = "testInfo";
    private final static String MESSAGE = "a log message from Example";
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
    public void hello1()
    {
      final String method = "hello";
      enter(method);
      debug(method, MESSAGE);
      debug(method, FORMAT, PARAM1, 123, 5.678);
      leave(method, true);
    }

    /**
     * Demonstrate tracing with no return value.
     */
    public void hello2()
    {
      final String method = "hello2";
      enter(method);
      trace(method, MESSAGE);
      leave(method);
    }

    /**
     * Demonstrate tracing with a return value.
     */
    public void hello3()
    {
      final String method = "hello2";
      enter(method);
      leave(method, 1);
    }

    /**
     * Demonstrate info method.
     */
    public void testInfo()
    {
      final String method = TEST_INFO;
      info(method, MESSAGE);
      info(method, FORMAT, PARAM1, 123, 5.678);
      final Exception cause = new FileNotFoundException(MESSAGE);
      info(method, cause, MESSAGE);
      info(method, cause, FORMAT, PARAM1, 123, 5.678);
    }

    /**
     * Demonstrate warn method.
     */
    public void testWarn()
    {
      final String method = TEST_INFO;
      warn(method, MESSAGE);
      warn(method, FORMAT, PARAM1, 123, 5.678);
      final Exception cause = new FileNotFoundException(MESSAGE);
      warn(method, cause, MESSAGE);
      warn(method, cause, FORMAT, PARAM1, 123, 5.678);
    }

    /**
     * Demonstrate error method.
     */
    public void testError()
    {
      final String method = TEST_INFO;
      error(method, MESSAGE);
      error(method, FORMAT, PARAM1, 123, 5.678);
      final Exception cause = new FileNotFoundException(MESSAGE);
      error(method, cause, MESSAGE);
      error(method, cause, FORMAT, PARAM1, 123, 5.678);
    }

    /**
     * Demonstrate fatal method.
     */
    public void testFatal()
    {
      final String method = TEST_INFO;
      fatal(method, MESSAGE);
      fatal(method, FORMAT, PARAM1, 123, 5.678);
      final Exception cause = new FileNotFoundException(MESSAGE);
      fatal(method, cause, MESSAGE);
      fatal(method, cause, FORMAT, PARAM1, 123, 5.678);
    }
  }
}
