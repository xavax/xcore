//
// Copyright 2011 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//
package com.xavax.json;

import org.testng.annotations.Test;

import com.xavax.json.JSONPath;

import static org.testng.Assert.*;

public class TestJSONPath {

  @Test
  public void testPath() {
    JSONPath path = new JSONPath("abc.def.ghi");
    assertNotNull(path);
    assertEquals(path.size(), 3);
    path.append("jkl");
    assertEquals(path.size(), 4);
    path.append("123").append("456").append("789");
    assertEquals(path.size(), 7);
    path = JSONPath.create("abc.def.ghi", ".123.456.789.");
    assertEquals(path.size(), 6);
    assertEquals(path.get(4), "456");
  }
}
