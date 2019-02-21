//
// Copyright 2013 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//

package com.xavax.util;

/**
 * Octets converts between long integers and strings containing 8
 * octets (byte values in hexadecimal) separated by colon delimiters.
 */
@SuppressWarnings("PMD.ClassNamingConventions")
public final class Octets {

  /**
   * Private constructor provided to keep the compiler from generating
   * a public default constructor.
   */
  private Octets() {}

  /**
   * Convert a long to a string of octets delimited by colons. If
   * escape is true, use the HTML entity "%3A" instead of a colon.
   *
   * @param value   the value to be converted.
   * @param escape  true if the delimiter should be escaped.
   * @return a string containing colon-delimited octets.
   */
  public static String toString(final long value, final boolean escape) {
    long lvalue = value;
    byte[] octets = new byte[8];
    final StringBuilder buffer = new StringBuilder();
    for ( int i = 7; i >= 0; --i ) {
      octets[i] = (byte) (lvalue & 0xFF);
      lvalue >>= 8;
    }
    for ( int i = 0; i < 8; ++i ) {
      if ( i > 0 ) {
	buffer.append(escape ? "%3A" : ":");
      }
      buffer.append(String.format("%02X", octets[i]));
    }
    return buffer.toString();
  }

  /**
   * Parse a string containing colon-delimited octets and return
   * the corresponding long value.
   *
   * @param value  a string containing colon-delimited octets.
   * @return  the long value corresponding to the octets.
   */
  public static long toLong(final String value) {
    long result = 0;
    final String[] octets = value.split(":");
    for ( final String octet: octets ) {
      result = (result << 8) + Integer.parseInt(octet, 16);
    }
    return result;
  }
}
