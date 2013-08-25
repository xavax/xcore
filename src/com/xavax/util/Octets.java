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
public class Octets {
  /**
   * Convert a long to a string of octets delimited by colons. If
   * escape is true, use the HTML entity "%3A" instead of a colon.
   *
   * @param l  the value to be converted.
   * @param escape  true if the delimiter should be escaped.
   * @return a string containing colon-delimited octets.
   */
  public static String toString(long l, boolean escape) {
    byte[] octets = new byte[8];
    StringBuilder sb = new StringBuilder();
    for ( int i = 7; i >= 0; --i ) {
      octets[i] = (byte) (l & 0xFF);
      l >>= 8;
    }
    for ( int i = 0; i < 8; ++i ) {
      if ( i > 0 ) {
	sb.append(escape ? "%3A" : ":");
      }
      String s = String.format("%02X", octets[i]);
      sb.append(s);
    }
    return sb.toString();
  }

  /**
   * Parse a string containing colon-delimited octets and return
   * the corresponding long value.
   *
   * @param s  a string containing colon-delimited octets.
   * @return  the long value corresponding to the octets.
   */
  public static long toLong(String s) {
    long l = 0;
    String[] octets = s.split(":");
    for ( String octet: octets ) {
      int i = Integer.parseInt(octet, 16);
      l = (l << 8) + i;
    }
    return l;
  }
}
