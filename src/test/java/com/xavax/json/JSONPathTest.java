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

  private static final String P123_456_789 = ".123.456.789.";
  private final static String ABC_DEF_GHI = "abc.def.ghi";
  private final static String EXPECT1 = "abc.def.ghi.123.456.789";
  private final static String EMPTY = "";

  /**
   * Test the JSONPath constructors.
   */
  @Test
  public void testPath() {
    JSONPath path = new JSONPath(ABC_DEF_GHI);
    assertNotNull(path);
    assertEquals(path.size(), 3);
    path.append("jkl");
    assertEquals(path.size(), 4);
    path.append("123").append("456").append("789");
    assertEquals(path.size(), 7);
    path = JSONPath.create(ABC_DEF_GHI, P123_456_789);
    assertEquals(path.size(), 6);
    assertEquals(path.get(4), "456");
    path = JSONPath.create(null, ABC_DEF_GHI, "..", EMPTY, P123_456_789);
    assertEquals(path.size(), 6);
    assertEquals(path.get(4), "456");
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
    assertEquals(path1.get(4), "456");
  }

  /**
   * Test the toString method.
   */
  @Test
  public void testToString() {
    final JSONPath path = JSONPath.create(ABC_DEF_GHI, P123_456_789);
    String result = path.toString();
    assertEquals(result, EXPECT1);
  }
}
