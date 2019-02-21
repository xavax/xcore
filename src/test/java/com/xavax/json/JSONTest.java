//
// Copyright 2011 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//
package com.xavax.json;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

import java.util.List;
import java.util.Map;

import com.xavax.util.CollectionFactory;

import static org.testng.Assert.*;

/**
 * Test the JSON class.
 */
@SuppressWarnings("PMD.SystemPrintln")
public class JSONTest {
  private static final String COSMO = "Cosmo";

  private static final String CHARLOTTE = "Charlotte";

  private static final String BIRMINGHAM = "Birmingham";

  private static final String ATLANTA = "Atlanta";

  // Featuring the infamous rapper TuPie, because FindBugs
  // would not stop complaining about "coarse values of pi". :(
  private final static double TUPIE = 6.28;

  private final static JSON.Format CUSTOM_FORMAT = new JSON.Format(false, "", "\"", "", "",
      "", "", "", "", "", "");

  private final static String INPUT1 =
      "{foo:{x:\"1\",y:\"2\"},bar:{x:\"3\",y:\"4\"}," +
	  "baz:{x:\"5\",y:\"6\",name:{first:\"John\",last:\"Doe\"}}," +
	  "cities:[\"Atlanta\",\"Birmingham\",\"Charlotte\"]}";
  private final static String INPUT2 =
      "{cities:[" +
	  "{name: 'Atlanta', x: 3.0000, y: 4.0000, pop: 4500000}," +
	  "{name: 'Birmingham', x: 1.0000, y: 2.0000, pop: 1250000, people: [" +
	  "{name: {first: 'Jack', last: 'Brown'}}," +
	  "{name: {first: 'Judy', last: 'Jetson'}}," +
	  "{name: {first: 'Cosmo', last: 'Spacely'}}]}," +
	  "{name: 'Charlotte', x: 5.0000, y: 6.0000, pop: 2000000}]}";
  private final static String INPUT3 =
      "{restaurants:[" +
	  "{name:'Clint\\'s Burgers'}," +
	  "{name:'Kurt\\'s Pizza'}," +
	  "{name:'Phil\\'s BBQ'}]}";
  private final static String INPUT4 =
      "{x: '100', tupie: '6.28', flag: 'true', z: 123}";
  private final static String INPUT5 =
      "{\"calendars\":[" +
	  "{\"calendarID\":\"900000002\"," +
	  	"\"calendarName\":\"My Business Calendar\"," +
	  	"\"calendarUri\":{\"href\":\"/v1_0/O/A/calendarDetail/900000002\"}}," +
	  "{\"calendarID\":\"900000000\"," +
	  	"\"calendarName\":\"My Business Calendar\"," +
	  	"\"calendarUri\":{\"href\":\"/v1_0/O/A/calendarDetail/900000000\"}}]}";
  private final static String INPUT6 =
      "{\"calendarDetail\":{" +
	  "\"calendarConfiguration\":{" +
	  	"\"timeNotationCode\":\"12h\"," +
	  	"\"categories\":[" +
	  		"{\"code\":\"B\",\"name\":\"Benefit\",\"colorCode\":\"FF0000\",\"eventTypes\":[" +
	  			"{\"code\":\"BOE\",\"name\":\"Benefits Open Enrollment\",\"colorCode\":\"FF0000\"}]}," +
	  		"{\"code\":\"H\",\"name\":\"Human Resource\",\"colorCode\":\"00FF00\",\"eventTypes\":[" +
	  			"{\"code\":\"CH\",\"name\":\"Company Holiday\",\"colorCode\":\"00FF00\"}," +
	  			"{\"code\":\"CE\",\"name\":\"Company Event\",\"colorCode\":\"00FF00\"}]}," +
	  		"{\"code\":\"P\",\"name\":\"Payroll\",\"colorCode\":\"0000F1\",\"eventTypes\":[" +
	  			"{\"code\":\"PS\",\"name\":\"Pay Schedule\",\"colorCode\":\"0000F1\"}]}," +
	  		"{\"code\":\"R\",\"name\":\"Retirement\",\"colorCode\":\"FFAB00\",\"eventTypes\":[" +
	  			"{\"code\":\"RPE\",\"name\":\"Retirement Plan Enrollment\",\"colorCode\":\"FFAB00\"}]}," +
	  		"{\"code\":\"T\",\"name\":\"Time Management\",\"colorCode\":\"FFAAA2\",\"eventTypes\":[" +
	  			"{\"code\":\"WS\",\"name\":\"Work Schedule\",\"colorCode\":\"FFAAA2\"}," +
	  			"{\"code\":\"WK\",\"name\":\"Worked\",\"colorCode\":\"FFAAA3\"}," +
	  			"{\"code\":\"PTO\",\"name\":\"Paid Time Off\",\"colorCode\":\"FFAAA4\"}," +
	  			"{\"code\":\"BLK\",\"name\":\"Black Out\",\"colorCode\":\"FFAAA5\"}]}]}," +
	  	"\"calendar\":{\"calendarID\":\"900000002\",\"calendarName\":\"My Business Calendar\"," +
	  		"\"timePeriod\":{\"startDateTime\":\"2011-11-17\",\"endDateTime\":\"2011-12-17\"}}}}";

  private final static String JOHN = "John";

  private JSONParser parser;

  /**
   * Test setup.
   */
  @BeforeMethod
  public void setUp() {
    parser = new JSONParser();
  }

  /**
   * Test the toString method.
   */
  @Test
  public void testToString() {
    final JSON result = parser.parse(INPUT1);
    assertNotNull(result);
    String string = result.toString(JSON.Format.COMPACT);
    assertNotNull(string);
    string = result.toString(JSON.Format.EXPANDED);
    assertNotNull(string);
    string = result.toString(JSON.Format.VERBOSE);
    assertNotNull(string);
  }

  /**
   * Test escaping in string literals.
   */
  @Test
  public void testEscaping() {
    final JSON result = parser.parse(INPUT3);
    assertNotNull(result);
    String string = result.toString(JSON.Format.COMPACT);
    assertEquals(string, INPUT3);
    string = result.toString(CUSTOM_FORMAT);
    assertNotNull(string);
    string = result.toString(JSON.Format.EXPANDED);
    assertNotNull(string);
    string = result.toString(JSON.Format.VERBOSE);
    assertNotNull(string);
  }

  /**
   * Test traversal using a path.
   */
  @Test
  public void testPath() {
    final JSON result = parser.parse(INPUT1);
    assertNotNull(result);
    JSONPath path = new JSONPath("baz.name.first");
    final String string = result.getString(path);
    assertEquals(JOHN, string);
    path = new JSONPath("baz.name");
    final JSON name = result.getJSON(path);
    assertTrue(name instanceof JSON);
  }

  /**
   * Test traversal of arrays using a path.
   */
  @Test
  public void testPathArrays() {
    final JSON result = parser.parse(INPUT2);
    assertNotNull(result);
    JSONPath path = new JSONPath("cities.name");
    assertEquals(result.getString(path, 0), ATLANTA);
    assertEquals(result.getString(path, 1), BIRMINGHAM);
    assertEquals(result.getString(path, 2), CHARLOTTE);
    path = new JSONPath("cities.people.name.first");
    assertEquals(result.getString(path, 1, 2), COSMO);
  }

  /**
   * Test the flatten method.
   */
  @Test
  public void testFlatten() {
    JSON json = parser.parse(INPUT1);
    assertNotNull(json);
    Map<String, String> result = json.flatten();
    assertEquals(result.size(), json.size());
    json = parser.parse(INPUT2);
    assertNotNull(json);
    result = json.flatten();
    assertEquals(result.size(), json.size());
  }

  /**
   * Test the merge method.
   */
  @Test
  public void testMerge() {
    final JSON json2 = parser.parse(INPUT2);
    assertNotNull(json2);
    final int size2 = json2.size();
    final JSON json3 = parser.parse(INPUT3);
    assertNotNull(json3);
    final int size3 = json3.size();
    json2.merge(json3);
    final JSON json4 = parser.parse(INPUT4);
    assertNotNull(json4);
    final int size4 = json4.size();
    json2.merge(json4);
    assertEquals(json2.size(), size2 + size3 + size4);
  }

  /**
   * Test the type conversions.
   */
  @Test
  public void testConversions() {
    final JSON json = parser.parse(INPUT4);
    assertNotNull(json);
    final long xvalue = json.getLong("x");
    assertEquals(xvalue, 100);
    final double tupie = json.getDouble("tupie");
    assertEquals(tupie, TUPIE);
    final boolean flag = json.getBoolean("flag");
    assertTrue(flag);
    final String zvalue = json.getString("z");
    assertEquals(zvalue, "123");
  }

  /**
   * Test parsing example 1.
   */
  @Test
  public void testExample1() {
    final JSON result = parser.parse(INPUT5);
    assertTrue(parser.isValid());
    System.out.println(result.toString(JSON.Format.VERBOSE));
    final JSONPath calendarsPath = new JSONPath("calendars");
    final JSONArray calendars = result.getArray(calendarsPath);
    // JSONArray calendars = result.getArray("calendars");
    assertNotNull(calendars);
    final List<String> calendarIDs = CollectionFactory.arrayList();
    for ( final Object object : calendars ) {
      if ( object instanceof JSON ) {
	final JSON calendar = (JSON) object;
	final String calendarId = calendar.getString("calendarID");
	calendarIDs.add(calendarId);
      }
    }
    System.out.println(calendarIDs.toString());
  }

  /**
   * Test parsing example 2.
   */
  @Test
  public void testExample2() {
    final JSON result = parser.parse(INPUT6);
    assertTrue(parser.isValid());
    System.out.println(result.toString(JSON.Format.VERBOSE));
    final JSONPath eventIDs = new JSONPath(
	"calendarDetail.calendarConfiguration.categories.eventTypes.code");
    final String code = result.getString(eventIDs, 4, 3);
    assertEquals(code, "BLK");
    assertEquals(result.get(eventIDs, 0), "BOE");
  }
}
