//
// Copyright 2010 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//

package com.xavax.logger;

import java.util.Date;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

import static org.testng.Assert.*;

public class TestXLogger {

  @BeforeMethod
  public void setUp()
  {
  }

  @Test
  public void testLogging()
  {
    foo();
  }

  public void foo()
  {
    baz();
  }

  public void baz()
  {
    Date begin = new Date();
    for ( int n = 0; n < 1000; ++n ) {
      Exception e = new NullPointerException();
      StackTraceElement[] stack = e.getStackTrace();
      StackTraceElement ste = stack[1];
      String s = ste.getClassName();
      int i = s.lastIndexOf('.');
      String className = i > 0 ? s.substring(i + 1) : s;
      s = format("%s.%d:%s.%s", ste.getFileName(), ste.getLineNumber(),
	  className, ste.getMethodName());
    }
    Date end = new Date();
    long elapsed = end.getTime() - begin.getTime();
    System.out.println("elapsed time: " + elapsed + " msec.");
  }

  public String format(String format, Object... params)
  {
    String s = String.format(format, params);
    return s;
  }

  @Test
  public void testFormatVarargs()
  {
    final String method = "testFormatVarargs";
    String s = XLogger.format(logger, method, "p1=[%s], p2=[%d] p3=[%f]",
	"param1", 123, 5.678);
    assertEquals(s, expect);
  }

  private final static Logger logger = Logger.getLogger(TestXLogger.class);

  private final static String expect =
      "TestXLogger.testFormatVarargs: p1=[param1], p2=[123] p3=[5.678000]";
}
