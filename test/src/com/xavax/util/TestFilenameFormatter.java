//
// Copyright 2013 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//

package com.xavax.util;

import org.testng.annotations.Test;

import com.xavax.util.FilenameFormatter;

import static org.testng.Assert.assertEquals;

/**
 * Test case for FilenameFormatter.
 *
 * @author alvitar@xavax.com
 */
public class TestFilenameFormatter {

  @Test
  public void TestFormatter() {
    String s = FilenameFormatter.filename(defaultTemplate, "test");
    assertEquals(20, s.length());
  }

  private final static String defaultTemplate = "test%1$tY%1$tm%1$td%1$tH%1$tM.log";
}
