//
// Copyright 2010 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//

package com.xavax.logger;

import java.util.Date;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Test cases for the XLogger class.
 */
public class XLoggerTest {
  private final static Logger LOGGER = Logger.getLogger(XLoggerTest.class);
  private final static String EXPECT =
      "XLoggerTest.testFormatVarargs: p1=[param1], p2=[123] p3=[5.678000]";

  /**
   * Test the logger.
   */
  @Test
  public void testLogging()
  {
    foo();
  }

  /**
   * Test the logger.
   */
  public void foo()
  {
    baz();
  }

  /**
   * Test the logger.
   */
  public void baz()
  {
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
  private String format(final String format, final Object... params)
  {
    return String.format(format, params);
  }

  /**
   * Test format varargs.
   */
  @Test
  public void testFormatVarargs()
  {
    final String method = "testFormatVarargs";
    final String result = XLogger.format(LOGGER, method, "p1=[%s], p2=[%d] p3=[%f]",
	"param1", 123, 5.678);
    assertEquals(result, EXPECT);
  }
}
