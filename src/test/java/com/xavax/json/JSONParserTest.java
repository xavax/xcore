//
// Copyright 2011 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//
package com.xavax.json;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Test cases for the JSON parser.
 */
@SuppressWarnings({"PMD.AvoidFileStream", "PMD.TooManyMethods"})
public class JSONParserTest {

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
  private final static String INPUT3B  = "{ $foo: 123 }";
  private final static String INPUT3C  = "{ @foo: 123 }";
  private final static String INPUT3D  = "{ : 123 }";
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
  private final static String INPUT9C  = "[123, 456, 789]";
  private final static String INPUT10A = "{ foo: {}, bar: {}, baz: {}}";
  private final static String INPUT11A = "{ foo: [], bar: [], baz: []}";
  private final static String INPUT12A = "foo: 1.2345, bar: -1, baz: 1.2e03, abc: 'abc' }";
  private final static String INPUT12B = "{ foo: 1.2345, bar: -1, baz: 1.2e03, abc: 'abc'";
  private final static String INPUT12C = "{ inner: { foo: [ 'abc', 'def' }";
  private final static String INPUT13  = "[{$match: {results: {$exists: true}}},{$sort: {'_id.requested_at': -1}},{$limit: 1},{$unwind: {path: \"$results\",includeArrayIndex: \"index\"}},{$group: {_id: {requested_at: '$_id.requested_at',hostname: '$results.appliance_hostname',result: '$results.result'},count: {$sum: 1}}}]";

  private JSONParser parser;

  /**
   * Test setup.
   */
  @BeforeMethod
  public void setUp() {
    parser = new JSONParser().quiet(false);
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
    assertFalse(parser.isValid());
    assertEquals(parser.getErrors().size(), 1);
    result = parser.parse(INPUT2B);
    assertFalse(parser.isValid());
    assertEquals(parser.getErrors().size(), 1);
    assertEquals(result.getLong(BAZ).intValue(), BAZ_VALUE);
    result = parser.parse(INPUT2C);
    assertFalse(parser.isValid());
    assertEquals(parser.getErrors().size(), 1);
    result = parser.parse(INPUT2D);
    assertFalse(parser.isValid());
    assertEquals(parser.getErrors().size(), 1);
    result = parser.parse(INPUT2E);
    assertFalse(parser.isValid());
    assertEquals(parser.getErrors().size(), 1);
    result = parser.parse(INPUT2F);
    assertFalse(parser.isValid());
    assertEquals(parser.getErrors().size(), 1);
    result = parser.parse(INPUT2G);
    assertFalse(parser.isValid());
    assertEquals(parser.getErrors().size(), 1);
    assertEquals(result.getString(ABC), ABC);
    result = parser.parse(INPUT2H);
    assertFalse(parser.isValid());
    assertEquals(parser.getErrors().size(), 1);
    result = parser.parse(INPUT2I);
    assertFalse(parser.isValid());
    assertEquals(parser.getErrors().size(), 1);
    result = parser.parse(INPUT2J);
    assertFalse(parser.isValid());
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
    result = parser.parse(INPUT12A);
    assertEquals(parser.getErrors().size(), 1);
    result = parser.parse(INPUT12B);
    assertEquals(parser.getErrors().size(), 1);
    result = parser.parse(INPUT12C);
    assertEquals(parser.getErrors().size(), 2);
    final StringReader reader = new StringReader(INPUT2A);
    final JSONParser parser2 = new JSONParser(reader, SOURCE);
    result = parser2.parse();
    assertFalse(parser2.isValid());
    assertEquals(parser2.getErrors().size(), 1);
  }

  /**
   * Test aborting on first error.
   */
  @Test
  public void testAbortOnError() {
    JSON result = parser.parse(INPUT2J);
    assertNotNull(result);
    assertFalse(parser.isValid());
    assertEquals(parser.getErrors().size(), 3);
    parser.abortOnError(true);
    result = parser.parse(INPUT2J);
    assertNotNull(result);
    assertFalse(parser.isValid());
    assertEquals(parser.getErrors().size(), 1);
  }

  /**
   * Test parsing identifiers.
   */
  @Test
  public void testIdentifiers() {
    JSON result = parser.parse(INPUT3A);
    assertTrue(parser.isValid());
    assertNotNull(result);
    result = parser.parse(INPUT3B);
    assertTrue(parser.isValid());
    assertNotNull(result);
    result = parser.parse(INPUT3C);
    assertFalse(parser.isValid());
    assertNotNull(result);
    result = parser.parse(INPUT3D);
    assertFalse(parser.isValid());
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
    assertTrue(flag);
    flag = result.getBoolean("f");
    assertFalse(flag);
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
    JSONArray jarray = parser.parseArray(INPUT9A);
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
    jarray = parser.parseArray(INPUT9C);
    assertNotNull(jarray);
    assertEquals(jarray.size(), 3);
    assertEquals(jarray.get(2), Long.valueOf(789));
    parser.allowCompoundIdentifiers(true);
    final JSONArray result3 = parser.parseArray(INPUT13);
    assertNotNull(result3);
    assertTrue(parser.isValid());
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
   * @throws IOException 
   */
  @Test
  public void testFileInput() throws IOException {
    Reader reader = new FileReader(FILENAME);
    final JSONParser parser2 = new JSONParser(reader, FILENAME);
    JSON result = parser2.parse();
    assertNotNull(result);
    assertTrue(parser2.isValid());
    reader.close();
    reader = new FileReader(FILENAME);
    final BufferedReader buffer = new BufferedReader(reader);
    result = JSONParser.parse(buffer, FILENAME);
    assertTrue(result instanceof JSON);
    result = JSONParser.parse(null, FILENAME);
    assertNull(result);
  }
}
