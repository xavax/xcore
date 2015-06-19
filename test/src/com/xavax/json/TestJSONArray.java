//
// Copyright 2011 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//
package com.xavax.json;

import java.util.List;
import java.util.Map;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Test cases for the JSONArray class.
 */
public class TestJSONArray {
  private JSONParser parser;

  private final static String INPUT_ATL =
      "{name: 'Atlanta', x: 3.0000, y: 4.0000, pop: 4500000}";
  private final static String INPUT_BHM =
      "{name: 'Birmingham', x: 1.0000, y: 2.0000, pop: 1250000}";
  private final static String INPUT_CHI =
      "{name: 'Chicago', x: 7.0000, y: 3.0000, pop: 7000000}";
  private final static String INPUT_CLT =
      "{name: 'Charlotte', x: 5.0000, y: 6.0000, pop: 2000000}";
  private final static String INPUT_LAX =
      "{name: 'Los Angeles', x: 2.0000, y: 7.0000, pop: 8000000}";
  private final static String INPUT_NYC =
      "{name: 'New York', x: 1.0000, y: 1.0000, pop: 11000000}";
  private final static String CITIES1 =
      "[" + INPUT_ATL + "," + INPUT_BHM + "," + INPUT_CLT + "]";
  private final static String CITIES2 =
      "[" + INPUT_CHI + "," + INPUT_LAX + "," + INPUT_NYC + "]";  

  /**
   * Test setup.
   */
  @BeforeMethod
  public void setUp() {
    parser = new JSONParser();
  }

  /**
   * Test the addAll method.
   */
  @Test
  public void testAddAll() {
    final JSONArray ja1 = parser.parseArray(CITIES1);
    assertTrue(parser.isValid());
    List<String> list = ja1.flatten();
    assertEquals(list.size(), 3);
    final JSONArray ja2 = parser.parseArray(CITIES2);
    assertTrue(parser.isValid());
    list = ja2.flatten();
    assertEquals(list.size(), 3);
    assertTrue(ja1.addAll(ja2));
    list = ja1.flatten();
    assertEquals(list.size(), 6);
  }

  /**
   * Test the flatten method.
   */
  @Test
  public void testFlatten() {
    final JSONArray array = parser.parseArray(CITIES1);
    assertTrue(parser.isValid());
    final List<String> list = array.flatten();
    assertEquals(list.size(), 3);
    assertEquals(list.get(0).length(), 40);
  }

  /**
   * Test the asMap method.
   */
  @Test
  public void testAsMap() {
    final JSONArray array = parser.parseArray(CITIES1);
    assertTrue(parser.isValid());
    final Map<String, String> map = array.asMap();
    assertEquals(map.size(), 3);
    assertEquals(map.get("0").length(), 40);
  }
}
