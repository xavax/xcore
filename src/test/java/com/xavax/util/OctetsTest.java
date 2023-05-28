//
// Copyright 2011 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//

package com.xavax.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test cases for the Octets utility class.
 */
public class OctetsTest {
  @SuppressWarnings("PMD.AvoidUsingHardCodedIP")
  final static String EXPECTED = "01:23:45:67:89:AB:CD:EF";
  final static String ESCAPED = "01%3A23%3A45%3A67%3A89%3AAB%3ACD%3AEF";
  final static long VALUE = 0x0123456789ABCDEFL;

  /**
   * Test the toString method.
   */
  @Test
  public void testToString() {
    assertEquals(Octets.toString(VALUE, false), EXPECTED);
    assertEquals(Octets.toString(VALUE, true), ESCAPED);
  }

  /**
   * Test the toLong method.
   */
  @Test
  public void testToLong() {
    assertEquals(Octets.toLong(EXPECTED), VALUE);
  }
}
