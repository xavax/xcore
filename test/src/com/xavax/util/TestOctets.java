//
// Copyright 2011 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//

package com.xavax.util;

import org.testng.annotations.Test;

import com.xavax.util.Octets;

import static org.testng.Assert.*;

public class TestOctets {
  final static String expected = "01:23:45:67:89:AB:CD:EF";
  final static String escaped = "01%3A23%3A45%3A67%3A89%3AAB%3ACD%3AEF";
  final static long el = 0x0123456789ABCDEFL;

  @Test
  public void testToString() {
    String s = Octets.toString(el, false);
    assertEquals(s, expected);
    s = Octets.toString(el, true);
    assertEquals(s, escaped);
  }

  public void testToLong() {
    long l = Octets.toLong(expected);
    assertEquals(l, el);
  }
}
