//
// Copyright 2011 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//
package com.xavax.json;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test cases for the JSONArray class.
 */
public class JSONArrayTest {
  private JSONParser parser;

  private final static String ATL = "Atlanta";
  private final static String BHM = "Birmingham";
  private final static String CHI = "Chicago";
  private final static String CLT = "Charlotte";
  private final static String LAX = "Los Angeles";
  private final static String NYC = "New York";
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
  private final static String[] NAMES = new String[] {
      ATL, BHM, CHI, CLT, LAX, NYC
  };

  private final static String EXPECT1 = "[]";
  private final static String EXPECT2 =
      "[{pop:4500000,name:'Atlanta',x:3.0,y:4.0}," +
      "{pop:1250000,name:'Birmingham',x:1.0,y:2.0}," +
      "{pop:2000000,name:'Charlotte',x:5.0,y:6.0}]";
  private final static String EXPECT3 =
      "{0:'{pop:4500000,name:\\'Atlanta\\',x:3.0,y:4.0}'," +
      "1:'{pop:1250000,name:\\'Birmingham\\',x:1.0,y:2.0}'," +
      "2:'{pop:2000000,name:\\'Charlotte\\',x:5.0,y:6.0}'}";

  /**
   * Test setup.
   */
  @Before
  public void setUp() {
    parser = new JSONParser();
  }

  /**
   * Test constructors.
   */
  @Test
  public void testConstructors() {
    JSONArray jsarray = new JSONArray(Arrays.asList((Object[]) NAMES));
    List<String> result = jsarray.flatten();
    assertEquals(result.size(), 6);
    assertEquals(result.get(0), ATL);
    jsarray = new JSONArray(ATL, BHM, CHI, CLT, LAX, NYC);
    result = jsarray.flatten();
    assertEquals(result.size(), 6);
    assertEquals(result.get(0), ATL);
    final JSONArray jsarray2 = new JSONArray(jsarray);
    result = jsarray2.flatten();
    assertEquals(result.size(), 6);
    assertEquals(result.get(0), ATL);
  }

  /**
   * Test add methods.
   */
  @Test
  public void testAdd() {
    JSONArray array = new JSONArray();
    array.add(Arrays.asList((Object[]) NAMES));
    Object object = array.get(0);
    assertTrue(object instanceof JSONArray);
    final JSON json = parser.parse(INPUT_ATL);
    assertTrue(parser.isValid());
    array = new JSONArray();
    array.add(json);
    object = array.get(0);
    assertTrue(object instanceof JSON);
  }

  /**
   * Test the addAll method.
   */
  @Test
  public void testAddAll() {
    final JSONArray ja1 = parser.parseArray(CITIES1);
    assertTrue(parser.isValid());
    final JSONArray ja2 = parser.parseArray(CITIES2);
    assertTrue(parser.isValid());
    assertTrue(ja1.addAll(ja2));
    final JSONArray ja3 = new JSONArray(ATL, BHM, ja1, CHI, CLT, ja2);
    final JSONArray ja4 = new JSONArray();
    ja4.addAll(ja3);
    final List<String> list = ja4.flatten();
    assertEquals(list.size(), 6);
  }

  /**
   * Test the flatten method.
   */
  @Test
  public void testFlatten() {
    final JSONArray ja1 = parser.parseArray(CITIES1);
    assertTrue(parser.isValid());
    final JSONArray ja2 = parser.parseArray(CITIES2);
    assertTrue(parser.isValid());
    final JSONArray array = new JSONArray(ATL, BHM, null, ja1, null, ja2);
    final List<String> list = array.flatten();
    assertEquals(list.size(), 6);
    assertEquals(list.get(0).length(), 7);
  }

  /**
   * Test the asMap and add(Map) methods.
   */
  @Test
  public void testAsMap() {
    final JSONArray array = parser.parseArray(CITIES1);
    assertTrue(parser.isValid());
    final Map<String, String> map = array.asMap();
    assertEquals(map.size(), 3);
    assertEquals(map.get("0").length(), 40);
    final JSONArray array2 = new JSONArray();
    array2.add(map);
    final List<String> list = array2.flatten();
    assertEquals(list.size(), 1);
    final String result = list.get(0);
    assertEquals(result, EXPECT3);
  }

  /**
   * Test the toString method.
   */
  @Test
  public void testToString() {
    JSONArray array = parser.parseArray(CITIES1);
    assertTrue(parser.isValid());
    String result = array.toString();
    assertEquals(result, EXPECT2);
    array = new JSONArray();
    result = array.toString();
    assertEquals(result, EXPECT1);
  }
}
