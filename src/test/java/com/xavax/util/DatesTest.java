package com.xavax.util;

import java.util.Date;
import java.util.TimeZone;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Test cases for the Dates utility class.
 * @author phill4
 *
 */
public class DatesTest {
  // 1959-07-19T12:34:56.000 GMT
  private final static long BIRTHDAY = -329916304000L;

  private final static String EXPECT1 = "1959-07-19T07:34:56.000";
  private final static String EXPECT2 = "1959-07-19T12:34:56.000";
  private final static String EXPECT3 = "19590719T073456000";
  private final static String EXPECT4 = "19590719T123456000";

  private final static TimeZone CST = TimeZone.getTimeZone("America/Chicago");
  /**
   * Test timestamp methods with Dates.
   */
  @Test
  public void testTimestampWithDate() {
    final Date date = new Date(BIRTHDAY);
    assertEquals(Dates.timestamp(date, true, CST), EXPECT1);
    assertEquals(Dates.timestamp(date, true), EXPECT2);
    assertEquals(Dates.timestamp(date), EXPECT2);
    assertEquals(Dates.timestamp(date, false, CST), EXPECT3);
    assertEquals(Dates.timestamp(date, false), EXPECT4);
    assertEquals(Dates.timestamp(true).length(), EXPECT1.length());
    assertEquals(Dates.timestamp().length(), EXPECT2.length());
  }

  /**
   * Test timestamp methods with time as a long.
   */
  @Test
  public void testTimestampWithLong() {
    assertEquals(Dates.timestamp(BIRTHDAY, true, CST), EXPECT1);
    assertEquals(Dates.timestamp(BIRTHDAY, true), EXPECT2);
    assertEquals(Dates.timestamp(BIRTHDAY), EXPECT2);
    assertEquals(Dates.timestamp(BIRTHDAY, false, CST), EXPECT3);
    assertEquals(Dates.timestamp(BIRTHDAY, false), EXPECT4);
  }
}
