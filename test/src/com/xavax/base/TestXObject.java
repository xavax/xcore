//
// Copyright 2010 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//

package com.xavax.base;

import org.testng.annotations.Test;

/**
 * Test cast for XObject.
 *
 * @author alvitar@xavax.com
 */
public class TestXObject extends XObject {

  /**
   * Test the debug logging method.
   */
  @Test
  public void testLogging()
  {
    final Example example = new Example();
    example.hello();
  }

  /**
   * Example is an example use of XObject.
   */
  public class Example extends XObject {
    /**
     * Writes a "hello" message to the log at debug level.
     */
    public void hello()
    {
      final String method = "hello";
      enter(method);
      debug(method, "a log message from Example");
      leave(method);
    }
  }
}
