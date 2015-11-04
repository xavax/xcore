package com.xavax.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static com.xavax.util.Constants.EMPTY_STRING;

/**
 * Dates is a utility class for manipulating dates and timestamps.
 */
public final class Dates {
  private final static String ISO_TIMESTAMP = "yyyy-MM-dd'T'HH:mm:ss.SSS";
  private final static String ISO_TIMESTAMP_ND = "yyyyMMdd'T'HHmmssSSS";

  private final static TimeZone GMT = TimeZone.getTimeZone("GMT");

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
   * @param timeZone   the time zone.
   * @return a date in ISO-8601 format.
   */
  public static String timestamp(final Date date, final boolean delimited, final TimeZone timeZone) {
    final SimpleDateFormat formatter =
	new SimpleDateFormat(delimited ? ISO_TIMESTAMP : ISO_TIMESTAMP_ND, Locale.getDefault());
    formatter.setTimeZone(timeZone);
    return date == null ? EMPTY_STRING : formatter.format(date);
  }

  /**
   * Return a date in ISO-8601 format for GMT.
   *
   * @param date       the date to be formatted.
   * @param delimited  true if the delimiters should be included.
   * @return a date in ISO-8601 format.
   */
  public static String timestamp(final Date date, final boolean delimited) {
    return timestamp(date, delimited, GMT);
  }

  /**
   * Return a date in ISO-8601 format with delimiters for the default locale.
   *
   * @param date       the date to be formatted.
   * @return a date in ISO-8601 format.
   */
  public static String timestamp(final Date date) {
    return timestamp(date, true, GMT);
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
   * @param time       the time to be formatted.
   * @param delimited  true if the delimiters should be included.
   * @param timeZone   the time zone.
   * @return a time in ISO-8601 format.
   */
  public static String timestamp(final long time, final boolean delimited, final TimeZone timeZone) {
    return timestamp(new Date(time), delimited, timeZone);
  }

  /**
   * Return a time in ISO-8601 format for GMT.
   *
   * @param  time      the time to be formatted.
   * @param delimited  true if the delimiters should be included.
   * @return a time in ISO-8601 format.
   */
  public static String timestamp(final long time, final boolean delimited) {
    return timestamp(time, delimited, GMT);
  }

  /**
   * Return a time in ISO-8601 format with delimiters for GMT.
   *
   * @param  time      the time to be formatted.
   * @return a time in ISO-8601 format.
   */
  public static String timestamp(final long time) {
    return timestamp(time, true, GMT);
  }
}
