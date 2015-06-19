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

import static org.testng.Assert.*;

/**
 * Test cases for the JSON parser.
 */
public class TestJSONParser {

  private final static int    BAZ_VALUE = 123;
  private final static double ABC_VALUE = 1.234e+05;
  private final static double FAB_VALUE = 1.234e+05;
  private final static double RAD_VALUE = -0.987;
  private final static double XYZ_VALUE = -0.123e-12;

  // Featuring the infamous rapper TuPie, because FindBugs
  // would not stop complaining about "coarse values of pi". :(
  private final static double TUPIE_VALUE = 6.28;

  private final static String ABC = "abc";
  private final static String BAZ = "baz";
  private final static String FAB_NAME = "fab";
  private final static String FILENAME = "test/data/test.json";
  private final static String FOO = "foo";
  private final static String RAD_NAME = "rad";
  private final static String SOURCE = "test.json";
  private final static String TUPIE_NAME = "tupie";
  private final static String XYZ_NAME = "xyz";

  private final static String PERSON1 = "{name: {first: 'Jack', last: 'Brown'}}";
  private final static String PERSON2 = "{name: {first: 'Judy', last: 'Jetson'}}";
  private final static String PERSON3 = "{name: {first: 'Cosmo', last: 'Spacely'}}";

  private final static String CITY_ATL =
      "{name: 'Atlanta', x: 3.0000, y: 4.0000, pop: 4500000}";
  private final static String CITY_BHM =
      "{name: 'Birmingham', x: 1.0000, y: 2.0000, pop: 1250000, people: [" +
	  PERSON1 + "," + PERSON2 + "," + PERSON3 + "]}";
  private final static String CITY_CLT =
      "{name: 'Charlotte', x: 5.0000, y: 6.0000, pop: 2000000}";

  private final static String INPUT1   = "{ foo: '123', bar: \"abc\", baz: \"123\" }";
  private final static String INPUT2A  = "{ foo bar: \"abc\" }";
  private final static String INPUT2B  = "{ foo bar: \"abc\", baz: 123 }";
  private final static String INPUT2C  = "{ foo: '123', bar: \"abc\", ";
  private final static String INPUT2D  = "{ foo: '123', bar: \"abc\", }";
  private final static String INPUT2E  = "{ foo: '123', bar: \"abc\", baz }";
  private final static String INPUT2F  = "{ foo: '123', bar: \"abc\", baz: }";
  private final static String INPUT2G  = "{ foo: '123', bar: \"abc\", baz:, abc: 'abc'}";
  private final static String INPUT2H  = "{ foo: '123', bar: \"abc\" } abc";
  private final static String INPUT2I  = "{ foo: 123zxx }";
  private final static String INPUT2J  = "{ foo: 1.2.345, bar: -1.-2, baz: 1.2e03e12, abc: 'abc' }";
  private final static String INPUT2K  = "{ t: True, f: FALSE, n: Null, no: nay }";
  private final static String INPUT3A  = "{ _foo: '123', 'bar': \"abc\", \"baz\": \"123\", foo_2: '123' }";
  private final static String INPUT4A  = "{ t: true, f: false, n: null }";
  private final static String INPUT5A  = "{ foo: 123, bar: -123, baz: 123 }";
  private final static String INPUT5B  = "{ fab: 1.2345, tupie: 6.28, rad: -0.987 }";
  private final static String INPUT5C  = "{ fab: 1.234e+05, xyz: -0.123e-12, abc: 1.234e05 }";
  private final static String INPUT5D  = "{ zero: 0, foo: 01, fab: .123, xyz: 0., baz: 12e+10 }";
  private final static String INPUT6A  = "{ foo: 'Phil\\'s Bar', bar: \"ab\\\"cd\", baz: \"123\" }";
  private final static String INPUT6B  = "{ foo: '\\\b\\\f\\\n\\\r\\\t' }";
  private final static String INPUT7A  = "{ exclamation: '\\u0021', pi: '\\u03C0', omega:'\\u03A9', cent:'\\u00A2', baz: '\\u010Z \\uFFFF'}";
  private final static String INPUT8A  = "{ foo: { x: \"1\", y: \"2\" }, bar: { x: \"3\", y: \"4\" }, baz:{x:\"5\",y:\"6\"}}";
  private final static String INPUT9A  = "[" + CITY_ATL + "," + CITY_BHM + "," + CITY_CLT + "]";
  private final static String INPUT9B  = "{cities:" + INPUT9A + "}";
  private final static String INPUT10A = "{ foo: {}, bar: {}, baz: {}}";
  private final static String INPUT11A = "{ foo: [], bar: [], baz: []}";


  private JSONParser parser;

  /**
   * Test setup.
   */
  @BeforeMethod
  public void setUp() {
    parser = new JSONParser();
  }

  /**
   * Test parsing a simple object.
   */
  @Test
  public void testObject() {
    final JSON result = parser.parse(INPUT1);
    assertTrue(parser.isValid());
    assertNotNull(result);
  }

  /**
   * Test various error scenarios.
   */
  @Test
  public void testErrors() {
    JSON result = parser.parse(INPUT2A);
    assertTrue(!parser.isValid());
    assertEquals(parser.getErrors().size(), 1);
    result = parser.parse(INPUT2B);
    assertTrue(!parser.isValid());
    assertEquals(parser.getErrors().size(), 1);
    assertEquals(result.getLong(BAZ).intValue(), BAZ_VALUE);
    result = parser.parse(INPUT2C);
    assertTrue(!parser.isValid());
    assertEquals(parser.getErrors().size(), 1);
    result = parser.parse(INPUT2D);
    assertTrue(!parser.isValid());
    assertEquals(parser.getErrors().size(), 1);
    result = parser.parse(INPUT2E);
    assertTrue(!parser.isValid());
    assertEquals(parser.getErrors().size(), 1);
    result = parser.parse(INPUT2F);
    assertTrue(!parser.isValid());
    assertEquals(parser.getErrors().size(), 1);
    result = parser.parse(INPUT2G);
    assertTrue(!parser.isValid());
    assertEquals(parser.getErrors().size(), 1);
    assertEquals(result.getString(ABC), ABC);
    result = parser.parse(INPUT2H);
    assertTrue(!parser.isValid());
    assertEquals(parser.getErrors().size(), 1);
    result = parser.parse(INPUT2I);
    assertTrue(!parser.isValid());
    assertEquals(parser.getErrors().size(), 1);
    result = parser.parse(INPUT2J);
    assertTrue(!parser.isValid());
    assertEquals(parser.getErrors().size(), 3);
    final String abc = result.getString(ABC);
    assertEquals(abc, ABC);
    result = parser.parse(INPUT2K);
    assertFalse(parser.isValid());
    assertEquals(parser.getErrors().size(), 4);
    parser.ignoreCase(true);
    result = parser.parse(INPUT2K);
    assertFalse(parser.isValid());
    assertEquals(parser.getErrors().size(), 1);
    final StringReader reader = new StringReader(INPUT2A);
    final JSONParser parser2 = new JSONParser(reader, SOURCE);
    result = parser2.parse();
    assertTrue(!parser2.isValid());
    assertEquals(parser2.getErrors().size(), 1);
  }

  /**
   * Test parsing identifiers.
   */
  @Test
  public void testIdentifiers() {
    final JSON result = parser.parse(INPUT3A);
    assertTrue(parser.isValid());
    assertNotNull(result);
  }

  /**
   * Test parsing words.
   */
  @Test
  public void testWords() {
    final JSON result = parser.parse(INPUT4A);
    assertTrue(parser.isValid());
    Boolean flag = result.getBoolean("t");
    assertEquals(flag, Boolean.TRUE);
    flag = result.getBoolean("f");
    assertEquals(flag, Boolean.FALSE);
    assertTrue(result.containsKey("n"));
    assertNull(result.get("n"));
  }

  /**
   * Test parsing numeric values.
   */
  @Test
  public void testNumeric() {
    JSON result = parser.parse(INPUT5A);
    assertTrue(parser.isValid());
    final long foo = result.getLong(FOO);
    assertEquals(foo, 123);
    result = parser.parse(INPUT5B);
    assertTrue(parser.isValid());
    assertEquals(result.getDouble(TUPIE_NAME), TUPIE_VALUE);
    assertEquals(result.getDouble(RAD_NAME), RAD_VALUE);
    result = parser.parse(INPUT5C);
    assertTrue(parser.isValid());
    assertEquals(result.getDouble(FAB_NAME), FAB_VALUE);
    assertEquals(result.getDouble(XYZ_NAME), XYZ_VALUE);
    assertEquals(result.getDouble(ABC), ABC_VALUE);
    result = parser.parse(INPUT5D);
    assertFalse(parser.isValid());
    assertEquals(parser.errorCount(), 1);
  }

  /**
   * Test escape sequences in strings.
   */
  @Test
  public void testEscape() {
    JSON result = parser.parse(INPUT6A);
    assertNotNull(result);
    assertTrue(parser.isValid());
    result = parser.parse(INPUT6B);
    assertTrue(parser.isValid());
  }

  /**
   * Test Unicode escape sequences.
   */
  @Test
  public void testUnicode() {
    final JSON result = parser.parse(INPUT7A);
    assertNotNull(result);
    assertFalse(parser.isValid());
    assertEquals(parser.errorCount(), 2);
  }

  /**
   * Test parsing nested objects.
   */
  @Test
  public void testNesting() {
    final JSON result = parser.parse(INPUT8A);
    assertNotNull(result);
    assertTrue(parser.isValid());
  }

  /**
   * Test parsing arrays.
   */
  @Test
  public void testArrays() {
    final JSON result = parser.parse(INPUT9B);
    assertNotNull(result);
    assertTrue(parser.isValid());
    final JSONArray jarray = parser.parseArray(INPUT9A);
    assertNotNull(jarray);
    assertEquals(jarray.size(), 3);
    final List<String> list = jarray.flatten();
    assertEquals(list.size(), 3);
    final String embedded = list.get(2);
    assertEquals(embedded.length(), 42);
    final JSON result2 = parser.parse(embedded);
    assertNotNull(result2);
    assertTrue(parser.isValid());
    assertEquals(result2.getString("name"), "Charlotte");
  }

  /**
   * Test parsing empty objects.
   */
  @Test
  public void testEmptyObjects() {
    final JSON result = parser.parse(INPUT10A);
    assertNotNull(result);
    assertTrue(parser.isValid());
  }

  /**
   * Test parsing empty arrays.
   */
  @Test
  public void testEmptyArrays() {
    final JSON result = parser.parse(INPUT11A);
    assertNotNull(result);
    assertTrue(parser.isValid());
  }

  /**
   * Test parsing input file.
   *
   * @throws FileNotFoundException
   */
  @Test
  public void testFileInput() throws FileNotFoundException {
    final String filename = FILENAME;
    final Reader reader = new FileReader(filename);
    final JSONParser parser2 = new JSONParser(reader, filename);
    final JSON result = parser2.parse();
    assertNotNull(result);
    assertTrue(parser2.isValid());
  }
}
