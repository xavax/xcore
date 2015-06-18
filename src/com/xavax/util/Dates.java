package com.xavax.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.xavax.util.Constants.EMPTY_STRING;

/**
 * Dates is a utility class for manipulating dates and timestamps.
 */
public final class Dates {
  private final static String ISO_TIMESTAMP = "yyyy-MM-ddTHH:mm:ss.SSS";
  private final static String ISO_TIMESTAMP_ND = "yyyyMMddTHHmmss";

  /**
   * Private constructor provided to keep the compiler from generating
   * a public default constructor.
   */
  private Dates() {}

  /**
   * Return a date in ISO-8601 format.
   *
   * @param date       the date to be formatted.
   * @param delimited  true if the delimiters should be included.
   * @return a date in ISO-8601 format.
   */
  public static String timestamp(final Date date, final boolean delimited) {
    final SimpleDateFormat formatter =
	new SimpleDateFormat(delimited ? ISO_TIMESTAMP : ISO_TIMESTAMP_ND, Locale.getDefault());
    return date == null ? EMPTY_STRING : formatter.format(date);
  }

  /**
   * Return a date in ISO-8601 format with delimiters.
   *
   * @param date       the date to be formatted.
   * @return a date in ISO-8601 format.
   */
  public static String timestamp(final Date date) {
    return timestamp(date, true);
  }


  /**
   * Return the current time in ISO-8601 format.
   *
   * @param delimited  true if the delimiters should be included.
   * @return a date in ISO-8601 format.
   */
  public static String timestamp(final boolean delimited) {
    return timestamp(new Date(System.currentTimeMillis()), true);
  }

  /**
   * Return the current time in ISO-8601 format with delimiters.
   *
   * @return a date in ISO-8601 format.
   */
  public static String timestamp() {
    return timestamp(true);
  }

  /**
   * Return a time in ISO-8601 format.
   *
   * @param  time      the time to be formatted.
   * @param delimited  true if the delimiters should be included.
   * @return a time in ISO-8601 format.
   */
  public static String timestamp(final long time, final boolean delimited) {
    return timestamp(new Date(time), delimited);
  }

  /**
   * Return a time in ISO-8601 format with delimiters.
   *
   * @param  time      the time to be formatted.
   * @return a time in ISO-8601 format.
   */
  public static String timestamp(final long time) {
    return timestamp(time, true);
  }
}
