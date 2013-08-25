//
// Copyright 2010 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//

package com.xavax.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;

/**
 * FilenameFormatter creates filenames from a template which can refer
 * to the current time of day which is passed to the formatter as the
 * first parameter
 *
 * @author alvitar@xavax.com
 */
public class FilenameFormatter {
  /**
   * Create a filename from a template. The first positional parameter passed
   * to the formatter is the current date and time. The template can use this
   * to create a filename that includes a timestamp. Additional parameters
   * are passed to the formatter and can be referenced by the template.
   *
   * @param template     the template to use as the format string.
   * @param extraParams  extra parameters that can be used in formatting.
   * @return
   */
  static public String filename(String template, String... extraParams) {
    StringBuilder sb = new StringBuilder();
    Date date = new Date();
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    Formatter formatter = new Formatter(sb);
    formatter.format(template, calendar, extraParams);
    formatter.close();
    return sb.toString();
  }

}
