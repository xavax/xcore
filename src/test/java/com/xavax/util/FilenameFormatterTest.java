//
// Copyright 2013 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//

package com.xavax.util;

import java.util.Calendar;
import java.util.Date;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Test case for FilenameFormatter.
 *
 * @author alvitar@xavax.com
 */
public class FilenameFormatterTest {
  private final static String EXPECTED = "test19590719123456.log";
  private final static String PREFIX = "test";
  private final static String SUFFIX = ".log";
  private final static String SEPARATOR = ", ";
  private final static String DEFAULT_TEMPLATE = "test%1$tY%1$tm%1$td%1$tH%1$tM%1$tS" + SUFFIX;
  private final static String PARAM_TEMPLATE = "%2$s, %3$s, %4$s";
  private final static String PARAM1 = "aaa1";
  private final static String PARAM2 = "bbb2";
  private final static String PARAM3 = "ccc3";
  private final static String PARAM_EXPECTED = PARAM1 + SEPARATOR + PARAM2 + SEPARATOR + PARAM3;
  private final static String[] PARAMS = new String[] { PARAM1, PARAM2, PARAM3 };

  private Date birthday;

  /**
   * Setup before each test.
   */
  @BeforeMethod
  public void setUp() {
    final Calendar calendar = Calendar.getInstance();
    calendar.set(1959, 6, 19, 12, 34, 56);
    birthday = calendar.getTime();
  }

  /**
   * Test the filename formatter with the current date.
   */
  @Test
  public void testFormatter() {
    final String output = FilenameFormatter.filename(DEFAULT_TEMPLATE, PREFIX);
    assertEquals(22, output.length());
    assertTrue(output.startsWith(PREFIX));
    assertTrue(output.endsWith(SUFFIX));
  }

  /**
   * Test the filename formatter with the current date.
   */
  @Test
  public void testFormatterWithDate() {
    final String output = FilenameFormatter.filename(DEFAULT_TEMPLATE, birthday, PREFIX);
    assertEquals(output, EXPECTED);
  }

  /**
   * Test the extra parameters feature.
   */
  @Test
  public void testExtraParams() {
    String output = FilenameFormatter.filename(PARAM_TEMPLATE, PARAM1, PARAM2, PARAM3);
    assertEquals(output, PARAM_EXPECTED);
    output = FilenameFormatter.filename(PARAM_TEMPLATE, PARAMS);
    assertEquals(output, PARAM_EXPECTED);
    output = FilenameFormatter.filename(DEFAULT_TEMPLATE, birthday, (String[]) null);
    assertEquals(output, EXPECTED);
  }
}
