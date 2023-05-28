//
// Copyright 2010 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//

package com.xavax.logger;

import java.io.FileNotFoundException;
import java.util.Date;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
// import org.junit.Before;
// import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test cases for the XLogger class.
 */
@SuppressWarnings({"PMD.MoreThanOneLogger", "PMD.TooManyMethods"})
public class XLoggerTest {
  private final static Logger LOGGER = Logger.getLogger(XLoggerTest.class);

  private final static String EMPTY = "";
  private final static String ENTER = "enter";
  private final static String FORMAT = "p1=[%s], p2=[%d] p3=[%f]";
  private final static String LEAVE = "leave";
  private final static String METHOD = "format";
  private final static String MESSAGE = "file not found";
  private final static String NULL_INDICATOR = "<null>";
  private final static String PARAM1 = "param1";
  private final static String UNKNOWN = "<unknown>";
  private final static String PREFIX = "XLoggerTest.format: ";
  private final static String UPREFIX = UNKNOWN + ".format: ";
  private final static String LEAVE_MESSAGE = "leave, return value = [";
  private final static String RESULT = "result";
  private final static String OUTPUT = "p1=[param1], p2=[123] p3=[5.678000]";
  private final static String EXPECT1 = "XLoggerTest.format: " + MESSAGE;
  private final static String EXPECT2 = PREFIX + OUTPUT;
  private final static String EXPECT3 = PREFIX + LEAVE_MESSAGE + RESULT + "]";
  private final static String EXPECT4 = PREFIX + LEAVE_MESSAGE + NULL_INDICATOR + "]";
  private final static String EXPECT5 =
      "XLoggerTest." + UNKNOWN + ": " + LEAVE_MESSAGE + RESULT + "]";
  private final static String EXPECT6 = UPREFIX + MESSAGE;
  private final static String EXPECT7 = UPREFIX + OUTPUT;
  private final static String EXPECT8 = UPREFIX + ENTER;
  private final static String EXPECT9 = UPREFIX + LEAVE;
  private final static String EXPECT10 = UPREFIX + LEAVE_MESSAGE + "false]";
  private final static String EXPECT11 = UPREFIX + LEAVE_MESSAGE + RESULT + "]";
  private final static String EXPECT12 = UPREFIX + LEAVE_MESSAGE + "null]";

  @Mock
  private Logger logger;

  /**
   * Common set up for all test cases.
   */
  // @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  /**
   * Test the logger.
   */
  // @Test
  public void testTraceLogging() {
    foo();
  }

  /**
   * Test the logger.
   */
  public void foo() {
    baz();
  }

  /**
   * Test the logger.
   */
  @SuppressWarnings({
    "PMD.AvoidInstantiatingObjectsInLoops",
    "PMD.AvoidThrowingNullPointerException",
    "PMD.SystemPrintln"
  })
  public void baz() {
    final Date begin = new Date();
    for ( int n = 0; n < 1000; ++n ) {
      final Exception npe = new NullPointerException();
      final StackTraceElement[] stack = npe.getStackTrace();
      final StackTraceElement ste = stack[1];
      String fullname = ste.getClassName();
      final int position = fullname.lastIndexOf('.');
      final String className = position > 0 ? fullname.substring(position + 1) : fullname;
      fullname = format("%s.%d:%s.%s", ste.getFileName(), ste.getLineNumber(),
	  className, ste.getMethodName());
    }
    final Date end = new Date();
    final long elapsed = end.getTime() - begin.getTime();
    System.out.println("elapsed time: " + elapsed + " msec.");
  }

  /**
   * Format a string.
   *
   * @param format  the format template.
   * @param params  substitution parameters.
   * @return a formatted string.
   */
  private String format(final String format, final Object... params) {
    return String.format(format, params);
  }

  /**
   * Test format.
   */
  // @Test
  public void testFormat() {
    String result = XLogger.format(LOGGER, METHOD, MESSAGE);
    assertEquals(result, EXPECT1);
    result = XLogger.format(null, METHOD, MESSAGE);
    assertEquals(result, EMPTY);
  }

  /**
   * Test format varargs.
   */
  // @Test
  public void testFormatVarargs() {
    String result = XLogger.format(LOGGER, METHOD, FORMAT, PARAM1, 123, 5.678);
    assertEquals(result, EXPECT2);
    result = XLogger.format(null, METHOD, FORMAT, PARAM1, 123, 5.678);
    assertEquals(result, EMPTY);
  }

  /**
   * Test formatLeave.
   */
  // @Test
  public void testFormatLeave() {
    String result = XLogger.formatLeave(LOGGER, METHOD, RESULT);
    assertEquals(result, EXPECT3);
    result = XLogger.formatLeave(LOGGER, METHOD, null);
    assertEquals(result, EXPECT4);
    result = XLogger.formatLeave(LOGGER, null, RESULT);
    assertEquals(result, EXPECT5);
    result = XLogger.formatLeave(null, METHOD, RESULT);
    assertEquals(result, EMPTY);
  }

  /**
   * Test the debug methods.
   */
  // @Test
  public void testDebug() {
    XLogger.debug(null, METHOD, MESSAGE);
    verify(logger, times(0)).debug(any());
    XLogger.debug(logger, METHOD, MESSAGE);
    verify(logger, times(0)).debug(any());
    XLogger.debug(null, METHOD, FORMAT, PARAM1, 123, 5.678);
    verify(logger, times(0)).debug(any());
    XLogger.debug(logger, METHOD, FORMAT, PARAM1, 123, 5.678);
    verify(logger, times(0)).debug(any());
    when(logger.isDebugEnabled()).thenReturn(true);
    XLogger.debug(logger, METHOD, MESSAGE);
    verify(logger, times(1)).debug(EXPECT6);
    XLogger.debug(logger, METHOD, FORMAT, PARAM1, 123, 5.678);
    verify(logger, times(1)).debug(EXPECT7);
  }

  /**
   * Test the trace method.
   */
  // @Test
  public void testTrace() {
    XLogger.trace(null, METHOD, MESSAGE);
    verify(logger, times(0)).trace(any());
    XLogger.trace(logger, METHOD, MESSAGE);
    verify(logger, times(0)).trace(any());
    when(logger.isTraceEnabled()).thenReturn(true);
    XLogger.trace(logger, METHOD, MESSAGE);
    verify(logger, times(1)).trace(EXPECT6);
  }

  /**
   * Test the enter method.
   */
  // @Test
  public void testEnter() {
    XLogger.enter(null, METHOD);
    verify(logger, times(0)).trace(any());
    XLogger.enter(logger, METHOD);
    verify(logger, times(0)).trace(any());
    when(logger.isTraceEnabled()).thenReturn(true);
    XLogger.enter(logger, METHOD);
    verify(logger, times(1)).trace(EXPECT8);
  }

  /**
   * Test the leave method.
   */
  // @Test
  public void testLeave() {
    XLogger.leave(null, METHOD);
    verify(logger, times(0)).trace(any());
    XLogger.leave(null, METHOD, false);
    verify(logger, times(0)).trace(any());
    XLogger.leave(null, METHOD, RESULT);
    verify(logger, times(0)).trace(any());
    XLogger.leave(logger, METHOD);
    verify(logger, times(0)).trace(any());
    XLogger.leave(logger, METHOD, false);
    verify(logger, times(0)).trace(any());
    XLogger.leave(logger, METHOD, RESULT);
    verify(logger, times(0)).trace(any());
    when(logger.isTraceEnabled()).thenReturn(true);
    XLogger.leave(logger, METHOD);
    verify(logger, times(1)).trace(EXPECT9);
    XLogger.leave(logger, METHOD, false);
    verify(logger, times(1)).trace(EXPECT10);
    XLogger.leave(logger, METHOD, RESULT);
    verify(logger, times(1)).trace(EXPECT11);
    XLogger.leave(logger, METHOD, null);
    verify(logger, times(1)).trace(EXPECT12);
  }

  /**
   * Test the info methods.
   */
  // @Test
  public void testInfo() {
    final Exception cause = new FileNotFoundException(MESSAGE);
    XLogger.info(null, METHOD, MESSAGE);
    verify(logger, times(0)).info(any());
    XLogger.info(logger, METHOD, MESSAGE);
    verify(logger, times(0)).info(any());
    XLogger.info(null, METHOD, cause, MESSAGE);
    verify(logger, times(0)).info(any());
    XLogger.info(logger, METHOD, cause, MESSAGE);
    verify(logger, times(0)).info(any());
    XLogger.info(null, METHOD, FORMAT, PARAM1, 123, 5.678);
    verify(logger, times(0)).info(any());
    XLogger.info(logger, METHOD, FORMAT, PARAM1, 123, 5.678);
    verify(logger, times(0)).info(any());
    XLogger.info(null, METHOD, cause, FORMAT, PARAM1, 123, 5.678);
    verify(logger, times(0)).info(any());
    XLogger.info(logger, METHOD, cause, FORMAT, PARAM1, 123, 5.678);
    verify(logger, times(0)).info(any());
    when(logger.isEnabledFor(Level.INFO)).thenReturn(true);
    XLogger.info(logger, METHOD, MESSAGE);
    verify(logger, times(1)).info(EXPECT6);
    XLogger.info(logger, METHOD, FORMAT, PARAM1, 123, 5.678);
    verify(logger, times(1)).info(EXPECT7);
    XLogger.info(logger, METHOD, cause, MESSAGE);
    verify(logger, times(1)).info(EXPECT6);
    XLogger.info(logger, METHOD, cause, FORMAT, PARAM1, 123, 5.678);
    verify(logger, times(1)).info(EXPECT7);
  }

  /**
   * Test the error methods.
   */
  // @Test
  public void testError() {
    final Exception cause = new FileNotFoundException(MESSAGE);
    XLogger.error(null, METHOD, MESSAGE);
    verify(logger, times(0)).error(any());
    XLogger.error(logger, METHOD, MESSAGE);
    verify(logger, times(0)).error(any());
    XLogger.error(null, METHOD, cause, MESSAGE);
    verify(logger, times(0)).error(any());
    XLogger.error(logger, METHOD, cause, MESSAGE);
    verify(logger, times(0)).error(any());
    XLogger.error(null, METHOD, FORMAT, PARAM1, 123, 5.678);
    verify(logger, times(0)).error(any());
    XLogger.error(logger, METHOD, FORMAT, PARAM1, 123, 5.678);
    verify(logger, times(0)).error(any());
    XLogger.error(null, METHOD, cause, FORMAT, PARAM1, 123, 5.678);
    verify(logger, times(0)).error(any());
    XLogger.error(logger, METHOD, cause, FORMAT, PARAM1, 123, 5.678);
    verify(logger, times(0)).error(any());
    when(logger.isEnabledFor(Level.ERROR)).thenReturn(true);
    XLogger.error(logger, METHOD, MESSAGE);
    verify(logger, times(1)).error(EXPECT6);
    XLogger.error(logger, METHOD, FORMAT, PARAM1, 123, 5.678);
    verify(logger, times(1)).error(EXPECT7);
    XLogger.error(logger, METHOD, cause, MESSAGE);
    verify(logger, times(1)).error(EXPECT6);
    XLogger.error(logger, METHOD, cause, FORMAT, PARAM1, 123, 5.678);
    verify(logger, times(1)).error(EXPECT7);
  }

  /**
   * Test the fatal methods.
   */
  // @Test
  public void testFatal() {
    final Exception cause = new FileNotFoundException(MESSAGE);
    XLogger.fatal(null, METHOD, MESSAGE);
    verify(logger, times(0)).fatal(any());
    XLogger.fatal(null, METHOD, cause, MESSAGE);
    verify(logger, times(0)).fatal(any());
    XLogger.fatal(null, METHOD, FORMAT, PARAM1, 123, 5.678);
    verify(logger, times(0)).fatal(any());
    XLogger.fatal(null, METHOD, cause, FORMAT, PARAM1, 123, 5.678);
    verify(logger, times(0)).fatal(any());
    XLogger.fatal(logger, METHOD, MESSAGE);
    verify(logger, times(1)).fatal(EXPECT6);
    XLogger.fatal(logger, METHOD, FORMAT, PARAM1, 123, 5.678);
    verify(logger, times(1)).fatal(EXPECT7);
    XLogger.fatal(logger, METHOD, cause, MESSAGE);
    verify(logger, times(1)).fatal(EXPECT6);
    XLogger.fatal(logger, METHOD, cause, FORMAT, PARAM1, 123, 5.678);
    verify(logger, times(1)).fatal(EXPECT7);
  }

  /**
   * Test the warn methods.
   */
  // @Test
  public void testWarn() {
    final Exception cause = new FileNotFoundException(MESSAGE);
    XLogger.warn(null, METHOD, MESSAGE);
    verify(logger, times(0)).warn(any());
    XLogger.warn(logger, METHOD, MESSAGE);
    verify(logger, times(0)).warn(any());
    XLogger.warn(null, METHOD, cause, MESSAGE);
    verify(logger, times(0)).warn(any());
    XLogger.warn(logger, METHOD, cause, MESSAGE);
    verify(logger, times(0)).warn(any());
    XLogger.warn(null, METHOD, FORMAT, PARAM1, 123, 5.678);
    verify(logger, times(0)).warn(any());
    XLogger.warn(logger, METHOD, FORMAT, PARAM1, 123, 5.678);
    verify(logger, times(0)).warn(any());
    XLogger.warn(null, METHOD, cause, FORMAT, PARAM1, 123, 5.678);
    verify(logger, times(0)).warn(any());
    XLogger.warn(logger, METHOD, cause, FORMAT, PARAM1, 123, 5.678);
    verify(logger, times(0)).warn(any());
    when(logger.isEnabledFor(Level.WARN)).thenReturn(true);
    XLogger.warn(logger, METHOD, MESSAGE);
    verify(logger, times(1)).warn(EXPECT6);
    XLogger.warn(logger, METHOD, FORMAT, PARAM1, 123, 5.678);
    verify(logger, times(1)).warn(EXPECT7);
    XLogger.warn(logger, METHOD, cause, MESSAGE);
    verify(logger, times(1)).warn(EXPECT6);
    XLogger.warn(logger, METHOD, cause, FORMAT, PARAM1, 123, 5.678);
    verify(logger, times(1)).warn(EXPECT7);
  }
}
