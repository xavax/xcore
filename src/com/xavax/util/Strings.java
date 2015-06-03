//
// Copyright 2013 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//

package com.xavax.util;

/**
 * Useful String utilities.
 */
public class Strings {
  private final static int DEFAULT_BUFFER_SIZE = 256;
  private final static String EMPTY = "";

  /**
   * Returns true if two strings are equal or both null.
   * 
   * @param string1 the first string to compare.
   * @param string2 the second string to compare.
   * @return true if two strings are equal or both null.
   */
  public static boolean equals(final String string1, final String string2) {
    return string1 == null ? string2 == null : string1.equals(string2);
  }

  /**
   * Returns the hash code for a string, or 1 if the string is null or empty.
   * 
   * @param input the string to be hashed.
   * @return the hash code for a string
   */
  public static int hashCode(final String input) {
    return input == null || EMPTY.equals(input) ? 1 : input.hashCode();
  }

  /**
   * Join an array of strings into one string with optional separator.
   * 
   * @param separator separator string (can be null).
   * @param skipNulls true if null or empty items should be skipped.
   * @param strings an array of strings to join.
   * @return a joined string.
   */
  public static String join(final String separator, final boolean skipNulls,
			    final String... strings) {
    final StringBuilder buffer = new StringBuilder(DEFAULT_BUFFER_SIZE);
    boolean first = true;
    final boolean useSeparator = separator != null && separator.length() > 0;
    for ( final String string : strings ) {
      if ( !skipNulls || string != null && string.length() > 0 ) {
	if ( first ) {
	  first = false;
	}
	else if ( useSeparator ) {
	  buffer.append(separator);
	}
	buffer.append(string);
      }
    }
    return buffer.toString();
  }

  /**
   * Pack an array of strings removing any null or empty elements. If the array
   * is already packed simply return the initial array.
   *
   * @param items an array of strings possibly containing null or empty strings.
   * @return a packed array of strings.
   */
  public static String[] pack(final String... items) {
    String[] packed;
    int count = 0;
    for ( final String item : items ) {
      if ( item != null && item.length() > 0 ) {
	++count;
      }
    }
    if ( count == items.length ) {
      // Array is already packed.
      packed = items;
    }
    else {
      packed = new String[count];
      count = 0;
      for ( final String item : items ) {
	if ( item != null && item.length() > 0 ) {
	  packed[count++] = item;
	}
      }
    }
    return packed;
  }
}
