//
// Copyright 2011 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//
package com.xavax.json;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Test cases for the JSONPath class.
 */
public class JSONPathTest {
  private final static String P123 = "123";
  private final static String P123_456_789 = ".123.456.789.";
  private final static String P456 = "456";
  private final static String P789 = "789";
  private final static String ABC_DEF_GHI = "abc.def.ghi";
  private final static String JKL = "jkl";
  private final static String ELLIPSIS2 = "..";
  private final static String EMPTY = "";
  private final static String EXPECT1 = "abc.def.ghi.123.456.789";

  /**
   * Test the JSONPath constructors.
   */
  @Test
  public void testPath() {
    JSONPath path = new JSONPath(ABC_DEF_GHI);
    assertNotNull(path);
    assertEquals(path.size(), 3);
    path.append(JKL);
    assertEquals(path.size(), 4);
    path.append(P123).append(P456).append(P789);
    assertEquals(path.size(), 7);
    path = JSONPath.create(ABC_DEF_GHI, P123_456_789);
    assertEquals(path.size(), 6);
    assertEquals(path.get(4), P456);
    path = JSONPath.create(null, ABC_DEF_GHI, ELLIPSIS2, EMPTY, P123_456_789);
    assertEquals(path.size(), 6);
    assertEquals(path.get(4), P456);
  }

  /**
   * Test the append method.
   */
  @Test
  public void testAppend() {
    final JSONPath path1 = new JSONPath(ABC_DEF_GHI);
    final JSONPath path2 = new JSONPath(P123_456_789);
    path1.append(path2);
    assertEquals(path1.size(), 6);
    assertEquals(path1.get(4), P456);
  }

  /**
   * Test the toString method.
   */
  @Test
  public void testToString() {
    final JSONPath path = JSONPath.create(ABC_DEF_GHI, P123_456_789);
    final String result = path.toString();
    assertEquals(result, EXPECT1);
  }
}
