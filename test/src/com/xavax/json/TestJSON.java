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

import com.xavax.json.JSON;
import com.xavax.json.JSONArray;
import com.xavax.json.JSONParser;
import com.xavax.json.JSONPath;
import com.xavax.util.CollectionFactory;

import static org.testng.Assert.*;

public class TestJSON {
  private JSONParser parser;

  private final JSON.Format myFormat = new JSON.Format(false, "", "\"", "", "",
      "", "", "", "", "", "");

  private final String input1 =
      "{foo:{x:\"1\",y:\"2\"},bar:{x:\"3\",y:\"4\"}," +
	  "baz:{x:\"5\",y:\"6\",name:{first:\"John\",last:\"Doe\"}}," +
	  "cities:[\"Atlanta\",\"Birmingham\",\"Charlotte\"]}";
  private final String input2 =
      "{cities:[" +
	  "{name: 'Atlanta', x: 3.0000, y: 4.0000, pop: 4500000}," +
	  "{name: 'Birmingham', x: 1.0000, y: 2.0000, pop: 1250000, people: [" +
	  "{name: {first: 'Jack', last: 'Brown'}}," +
	  "{name: {first: 'Judy', last: 'Jetson'}}," +
	  "{name: {first: 'Cosmo', last: 'Spacely'}}]}," +
	  "{name: 'Charlotte', x: 5.0000, y: 6.0000, pop: 2000000}]}";
  final String input3 =
      "{restaurants:[" +
	  "{name:'Clint\\'s Burgers'}," +
	  "{name:'Kurt\\'s Pizza'}," +
	  "{name:'Phil\\'s BBQ'}]}";
  final String input4 =
      "{x: '100', pi: '3.14159', flag: 'true', z: 123}";

  @BeforeMethod
  public void setUp() {
    parser = new JSONParser();
  }

  @Test
  public void testToString() {
    JSON result = parser.parse(input1);
    assertNotNull(result);
    String s = result.toString(JSON.Format.COMPACT);
    assertNotNull(s);
    s = result.toString(JSON.Format.EXPANDED);
    assertNotNull(s);
    s = result.toString(JSON.Format.VERBOSE);
    assertNotNull(s);
  }

  @Test
  public void testEscaping() {
    JSON result = parser.parse(input3);
    assertNotNull(result);
    String s = result.toString(JSON.Format.COMPACT);
    assertEquals(s, input3);
    s = result.toString(myFormat);
    assertNotNull(s);
    s = result.toString(JSON.Format.EXPANDED);
    assertNotNull(s);
    s = result.toString(JSON.Format.VERBOSE);
    assertNotNull(s);
  }

  @Test
  public void testPath() {
    JSON result = parser.parse(input1);
    assertNotNull(result);
    JSONPath path = new JSONPath("baz.name.first");
    String s = result.getString(path);
    assert (s.equals("John"));
    path = new JSONPath("baz.name");
    JSON name = result.getJSON(path);
    assert (name instanceof JSON);
  }

  @Test
  public void testPathArrays() {
    JSON result = parser.parse(input2);
    assertNotNull(result);
    JSONPath path = new JSONPath("cities.name");
    String s = result.getString(path);
    assert (s.equals("Atlanta"));
    s = result.getString(path, 1);
    assert (s.equals("Birmingham"));
    s = result.getString(path, 2);
    assert (s.equals("Charlotte"));
    path = new JSONPath("cities.people.name.first");
    s = result.getString(path, 1, 2);
    assert (s.equals("Cosmo"));
  }

  @Test
  public void testFlatten() {
    JSON json = parser.parse(input1);
    assertNotNull(json);
    Map<String, String> result = json.flatten();
    assertEquals(result.size(), json.size());
    json = parser.parse(input2);
    assertNotNull(json);
    result = json.flatten();
    assertEquals(result.size(), json.size());
  }

  @Test
  public void testMerge() {
    JSON json2 = parser.parse(input2);
    assertNotNull(json2);
    int size2 = json2.size();
    JSON json3 = parser.parse(input3);
    assertNotNull(json3);
    int size3 = json3.size();
    json2.merge(json3);
    JSON json4 = parser.parse(input4);
    assertNotNull(json4);
    int size4 = json4.size();
    json2.merge(json4);
    assertEquals(json2.size(), size2 + size3 + size4);
  }

  @Test
  public void testConversions() {
    JSON json = parser.parse(input4);
    assertNotNull(json);
    long x = json.getLong("x");
    assertEquals(x, 100);
    double pi = json.getDouble("pi");
    assertEquals(pi, 3.14159);
    boolean flag = json.getBoolean("flag");
    assertEquals(flag, true);
    String z = json.getString("z");
    assertEquals(z, "123");
  }

  @Test
  public void testExample1() {
    final String input = "{\"calendars\":[{\"calendarID\":\"900000002\",\"calendarName\":\"My Business Calendar\",\"calendarUri\":{\"href\":\"/v1_0/O/A/calendarDetail/900000002\"}},{\"calendarID\":\"900000000\",\"calendarName\":\"My Business Calendar\",\"calendarUri\":{\"href\":\"/v1_0/O/A/calendarDetail/900000000\"}}]}";
    JSON result = parser.parse(input);
    assertTrue(parser.isValid());
    String s = result.toString(JSON.Format.VERBOSE);
    System.out.println(s);
    JSONPath calendarsPath = new JSONPath("calendars");
    JSONArray calendars = result.getArray(calendarsPath);
    // JSONArray calendars = result.getArray("calendars");
    assertNotNull(calendars);
    List<String> calendarIDs = CollectionFactory.arrayList();
    for (Object o : calendars) {
      if ( o instanceof JSON ) {
	JSON calendar = (JSON) o;
	String id = calendar.getString("calendarID");
	calendarIDs.add(id);
      }
    }
    System.out.println(calendarIDs.toString());
  }

  @Test
  public void testExample2() {
    final String input = "{\"calendarDetail\":{\"calendarConfiguration\":{\"timeNotationCode\":\"12h\",\"categories\":[{\"code\":\"B\",\"name\":\"Benefit\",\"colorCode\":\"FF0000\",\"eventTypes\":[{\"code\":\"BOE\",\"name\":\"Benefits Open Enrollment\",\"colorCode\":\"FF0000\"}]},{\"code\":\"H\",\"name\":\"Human Resource\",\"colorCode\":\"00FF00\",\"eventTypes\":[{\"code\":\"CH\",\"name\":\"Company Holiday\",\"colorCode\":\"00FF00\"},{\"code\":\"CE\",\"name\":\"Company Event\",\"colorCode\":\"00FF00\"}]},{\"code\":\"P\",\"name\":\"Payroll\",\"colorCode\":\"0000F1\",\"eventTypes\":[{\"code\":\"PS\",\"name\":\"Pay Schedule\",\"colorCode\":\"0000F1\"}]},{\"code\":\"R\",\"name\":\"Retirement\",\"colorCode\":\"FFAB00\",\"eventTypes\":[{\"code\":\"RPE\",\"name\":\"Retirement Plan Enrollment\",\"colorCode\":\"FFAB00\"}]},{\"code\":\"T\",\"name\":\"Time Management\",\"colorCode\":\"FFAAA2\",\"eventTypes\":[{\"code\":\"WS\",\"name\":\"Work Schedule\",\"colorCode\":\"FFAAA2\"},{\"code\":\"WK\",\"name\":\"Worked\",\"colorCode\":\"FFAAA3\"},{\"code\":\"PTO\",\"name\":\"Paid Time Off\",\"colorCode\":\"FFAAA4\"},{\"code\":\"BLK\",\"name\":\"Black Out\",\"colorCode\":\"FFAAA5\"}]}]},\"calendar\":{\"calendarID\":\"900000002\",\"calendarName\":\"My Business Calendar\",\"timePeriod\":{\"startDateTime\":\"2011-11-17\",\"endDateTime\":\"2011-12-17\"}}}}";
    JSON result = parser.parse(input);
    assertTrue(parser.isValid());
    String s = result.toString(JSON.Format.VERBOSE);
    System.out.println(s);
    JSONPath eventIDs = new JSONPath(
	"calendarDetail.calendarConfiguration.categories.eventTypes.code");
    String code = result.getString(eventIDs, 4, 3);
    assertEquals(code, "BLK");
    Object o = result.get(eventIDs);
    assertEquals(o, "BOE");
  }
}
