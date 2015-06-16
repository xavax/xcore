//
// Copyright 2011 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//
package com.xavax.json;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.xavax.json.JSON;
import com.xavax.json.JSONArray;
import com.xavax.json.JSONParser;

import static org.testng.Assert.*;

/**
 * Test cases for the JSON parser.
 */
public class TestJSONParser {
  // Featuring the infamous rapper TuPie, because FindBugs
  // would not stop complaining about "coarse values of pi". :(
  private final static double TUPIE = 6.28;
  private JSONParser parser;
  private final String person1 =
      "{name: {first: 'Jack', last: 'Brown'}}";
  private final String person2 =
      "{name: {first: 'Judy', last: 'Jetson'}}";
  private final String person3 =
      "{name: {first: 'Cosmo', last: 'Spacely'}}";
  private final String inputATL =
      "{name: 'Atlanta', x: 3.0000, y: 4.0000, pop: 4500000}";
  private final String inputBHM =
      "{name: 'Birmingham', x: 1.0000, y: 2.0000, pop: 1250000, people: [" +
	  person1 + "," + person2 + "," + person3 + "]}";
  private final String inputCLT =
      "{name: 'Charlotte', x: 5.0000, y: 6.0000, pop: 2000000}";
  private final String input2a =
      "[" + inputATL + "," + inputBHM + "," + inputCLT + "]";
  private final String input2b =
      "{cities:" + input2a + "}";

  @BeforeMethod
  public void setUp() {
    parser = new JSONParser();
  }

  @Test
  public void testObject() {
    final String input = "{ foo: '123', bar: \"abc\", baz: \"123\" }";
    JSON result = parser.parse(input);
    assertTrue(parser.isValid());
    assertNotNull(result);
  }

  @Test
  public void testErrors() {
    JSON result = parser.parse("{ foo bar: \"abc\" }");
    assertTrue(!parser.isValid());
    assertEquals(parser.getErrors().size(), 1);
    result = parser.parse("{ foo bar: \"abc\", baz: 123 }");
    assertTrue(!parser.isValid());
    assertEquals(parser.getErrors().size(), 1);
    Long baz = result.getLong("baz");
    assertEquals(baz.intValue(), 123);
    result = parser.parse("{ foo: '123', bar: \"abc\", ");
    assertTrue(!parser.isValid());
    assertEquals(parser.getErrors().size(), 1);
    result = parser.parse("{ foo: '123', bar: \"abc\", }");
    assertTrue(!parser.isValid());
    assertEquals(parser.getErrors().size(), 1);
    result = parser.parse("{ foo: '123', bar: \"abc\", baz }");
    assertTrue(!parser.isValid());
    assertEquals(parser.getErrors().size(), 1);
    result = parser.parse("{ foo: '123', bar: \"abc\", baz: }");
    assertTrue(!parser.isValid());
    assertEquals(parser.getErrors().size(), 1);
    result = parser.parse("{ foo: '123', bar: \"abc\", baz:, abc: 'abc'}");
    assertTrue(!parser.isValid());
    assertEquals(parser.getErrors().size(), 1);
    assertEquals(result.getString("abc"), "abc");
    result = parser.parse("{ foo: '123', bar: \"abc\" } abc");
    assertTrue(!parser.isValid());
    assertEquals(parser.getErrors().size(), 1);
    result = parser.parse("{ foo: 123zxx }");
    assertTrue(!parser.isValid());
    assertEquals(parser.getErrors().size(), 1);
    result = parser
	.parse("{ foo: 1.2.345, bar: -1.-2, baz: 1.2e03e12, abc: 'abc' }");
    assertTrue(!parser.isValid());
    assertEquals(parser.getErrors().size(), 3);
    String abc = result.getString("abc");
    assertEquals(abc, "abc");
    String boolInput = "{ t: True, f: FALSE, n: Null, no: nay }";
    result = parser.parse(boolInput);
    assertFalse(parser.isValid());
    assertEquals(parser.getErrors().size(), 4);
    parser.ignoreCase(true);
    result = parser.parse(boolInput);
    assertFalse(parser.isValid());
    assertEquals(parser.getErrors().size(), 1);
    StringReader reader = new StringReader("{ foo bar: \"abc\" }");
    JSONParser parser2 = new JSONParser(reader, "test.json");
    result = parser2.parse();
    assertTrue(!parser2.isValid());
    assertEquals(parser2.getErrors().size(), 1);
  }

  @Test
  public void testIdentifiers() {
    final String input = "{ _foo: '123', 'bar': \"abc\", \"baz\": \"123\", foo_2: '123' }";
    JSON result = parser.parse(input);
    assertTrue(parser.isValid());
    assertNotNull(result);
  }

  @Test
  public void testWords() {
    JSON result = parser.parse("{ t: true, f: false, n: null }");
    assertTrue(parser.isValid());
    Boolean b = result.getBoolean("t");
    assertEquals(b, Boolean.TRUE);
    b = result.getBoolean("f");
    assertEquals(b, Boolean.FALSE);
    assertTrue(result.containsKey("n"));
    Object o = result.get("n");
    assertNull(o);
  }

  @Test
  public void testNumeric() {
    JSON result = parser.parse("{ foo: 123, bar: -123, baz: 123 }");
    assertTrue(parser.isValid());
    long foo = result.getLong("foo");
    assertEquals(foo, 123);
    result = parser.parse("{ fab: 1.2345, tupie: 6.28, rad: -0.987 }");
    assertTrue(parser.isValid());
    Double tupie = result.getDouble("tupie");
    assertEquals(tupie, TUPIE);
    Double rad = result.getDouble("rad");
    assertEquals(rad, -0.987);
    result = parser.parse("{ fab: 1.234e+05, xyz: -0.123e-12, abc: 1.234e05 }");
    assertTrue(parser.isValid());
    Double fab = result.getDouble("fab");
    assertEquals(fab, 1.234e+05);
    Double xyz = result.getDouble("xyz");
    assertEquals(xyz, -0.123e-12);
    Double abc = result.getDouble("abc");
    assertEquals(abc, 1.234e+05);
    result = parser
	.parse("{ zero: 0, foo: 01, fab: .123, xyz: 0., baz: 12e+10 }");
    assertFalse(parser.isValid());
    assertEquals(parser.errorCount(), 1);
  }

  @Test
  public void testEscape() {
    final String input1 = "{ foo: 'Phil\\'s Bar', bar: \"ab\\\"cd\", baz: \"123\" }";
    final String input2 = "{ foo: '\\\b\\\f\\\n\\\r\\\t' }";
    JSON result = parser.parse(input1);
    assertNotNull(result);
    assertTrue(parser.isValid());
    result = parser.parse(input2);
    assertTrue(parser.isValid());
  }

  @Test
  public void testUnicode() {
    final String input = "{ exclamation: '\\u0021', pi: '\\u03C0', omega:'\\u03A9', cent:'\\u00A2', baz: '\\u010Z \\uFFFF'}";
    JSON result = parser.parse(input);
    assertNotNull(result);
    assertFalse(parser.isValid());
    assertEquals(parser.errorCount(), 2);
  }

  @Test
  public void testNesting() {
    final String input = "{ foo: { x: \"1\", y: \"2\" }, bar: { x: \"3\", y: \"4\" }, baz:{x:\"5\",y:\"6\"}}";
    JSON result = parser.parse(input);
    assertNotNull(result);
    assertTrue(parser.isValid());
  }

  @Test
  public void testArrays() {
    JSON result = parser.parse(input2b);
    assertNotNull(result);
    assertTrue(parser.isValid());
    JSONArray jarray = parser.parseArray(input2a);
    assertNotNull(jarray);
    assertEquals(jarray.size(), 3);
    List<String> l = jarray.flatten();
    assertEquals(l.size(), 3);
    String s = l.get(2);
    assertEquals(s.length(), 42);
    JSON result2 = parser.parse(s);
    assertNotNull(result2);
    assertTrue(parser.isValid());
    String city = result2.getString("name");
    assertEquals(city, "Charlotte");
  }

  @Test
  public void testEmptyObjects() {
    final String input = "{ foo: {}, bar: {}, baz: {}}";
    JSON result = parser.parse(input);
    assertNotNull(result);
    assertTrue(parser.isValid());
  }

  @Test
  public void testEmptyArrays() {
    final String input = "{ foo: [], bar: [], baz: []}";
    JSON result = parser.parse(input);
    assertNotNull(result);
    assertTrue(parser.isValid());
  }

  @Test
  public void testFileInput() throws FileNotFoundException {
    final String filename = "test/data/test.json";
    Reader reader = new FileReader(filename);
    JSONParser parser2 = new JSONParser(reader, filename);
    JSON result = parser2.parse();
    assertNotNull(result);
    assertTrue(parser2.isValid());
  }
}
