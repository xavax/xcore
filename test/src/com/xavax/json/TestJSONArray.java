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

import com.xavax.json.JSONArray;
import com.xavax.json.JSONParser;

import static org.testng.Assert.*;

public class TestJSONArray {
  private JSONParser parser;

  private final String inputATL =
      "{name: 'Atlanta', x: 3.0000, y: 4.0000, pop: 4500000}";
  private final String inputBHM =
      "{name: 'Birmingham', x: 1.0000, y: 2.0000, pop: 1250000}";
  private final String inputCLT =
      "{name: 'Charlotte', x: 5.0000, y: 6.0000, pop: 2000000}";
  private final String inputCHI =
      "{name: 'Chicago', x: 7.0000, y: 3.0000, pop: 7000000}";
  private final String inputLAX =
      "{name: 'Los Angeles', x: 2.0000, y: 7.0000, pop: 8000000}";
  private final String inputNYC =
      "{name: 'New York', x: 1.0000, y: 1.0000, pop: 11000000}";
  private final String cities1 =
      "[" + inputATL + "," + inputBHM + "," + inputCLT + "]";
  private final String cities2 =
      "[" + inputCHI + "," + inputLAX + "," + inputNYC + "]";  

  @BeforeMethod
  public void setUp() {
    parser = new JSONParser();
  }

  @Test
  public void testAddAll() {
    JSONArray ja1 = parser.parseArray(cities1);
    assertTrue(parser.isValid());
    List<String> l = ja1.flatten();
    assertEquals(l.size(), 3);
    JSONArray ja2 = parser.parseArray(cities2);
    assertTrue(parser.isValid());
    l = ja2.flatten();
    assertEquals(l.size(), 3);
    assertTrue(ja1.addAll(ja2));
    l = ja1.flatten();
    assertEquals(l.size(), 6);
  }

  @Test
  public void testFlatten() {
    JSONArray ja = parser.parseArray(cities1);
    assertTrue(parser.isValid());
    List<String> l = ja.flatten();
    assertEquals(l.size(), 3);
    String s = l.get(0);
    assertEquals(s.length(), 40);
  }

  @Test
  public void testAsMap() {
    JSONArray ja = parser.parseArray(cities1);
    assertTrue(parser.isValid());
    Map<String, String> map = ja.asMap();
    assertEquals(map.size(), 3);
    String s = map.get("0");
    assertEquals(s.length(), 40);
  }
}
