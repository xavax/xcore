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
public final class FilenameFormatter {
  private final static int DEFAULT_BUFFER_SIZE = 32;

  /**
   * Construct a FilenameFormatter. This exists only to prevent the
   * compiler from generating a default constructor.
   */
  private FilenameFormatter() {}

  /**
   * Create a filename from a template. The first positional parameter passed
   * to the formatter is the current date and time. The template can use this
   * to create a filename that includes a timestamp. Additional parameters
   * are passed to the formatter and can be referenced by the template.
   *
   * @param template     the template to use as the format string.
   * @param extraParams  extra parameters that can be used in formatting.
   * @return a filename string.
   */
  public static String filename(final String template, final String... extraParams) {
    return filename(template, new Date(), extraParams);
  }

  /**
   * Create a filename from a template. The first positional parameter passed
   * to the formatter is the specified date and time. The template can use this
   * to create a filename that includes a timestamp. Additional parameters
   * are passed to the formatter and can be referenced by the template.
   *
   * @param template     the template to use as the format string.
   * @param date         the date to use for the timestamp.
   * @param extraParams  extra parameters that can be used in formatting.
   * @return a filename string.
   */
  public static String filename(final String template, final Date date, final String... extraParams) {
    final Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    final StringBuilder buffer = new StringBuilder(DEFAULT_BUFFER_SIZE);
    final Formatter formatter = new Formatter(buffer);
    final int capacity = extraParams == null ? 1 : extraParams.length + 1;
    formatter.format(template, Varargs.create(capacity).append(calendar)
				      .flatten((Object[]) extraParams).toArray());
    formatter.close();
    return buffer.toString();
  }
}
