//
// Copyright 2010 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//

package com.xavax.base;

import org.testng.annotations.Test;

import com.xavax.base.XObject;

/**
 * Test cast for XObject.
 *
 * @author alvitar@xavax.com
 */
public class TestXObject extends XObject {

  @Test
  public void test()
  {
    Example example = new Example();
    example.hello();
  }

  public class Example extends XObject {
    public void hello()
    {
      final String method = "hello";
      enter(method);
      debug(method, "a log message from Example");
      leave(method);
    }
  }
}
