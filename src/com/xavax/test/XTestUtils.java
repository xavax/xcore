//
// Copyright 2013 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//
package com.xavax.test;

/**
 * XCacheTestCase is a base class for XCache test cases.
 * 
 * @author alvitar@xavax.com
 */
public final class XTestUtils {

  /**
   * Private constructor provided to keep the compiler from generating
   * a public default constructor.
   */
  private XTestUtils() {}

  /**
   * Sleep the specified number of milliseconds.
   * 
   * @param interval the amount of time in milliseconds to sleep.
   */
  public static void sleep(final long interval) {
    try {
      Thread.sleep(interval);
    }
    catch (Exception e) {
      // Ignore this exception.
    }
  }
}
