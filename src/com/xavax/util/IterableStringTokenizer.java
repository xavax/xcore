//
// Copyright 2011 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//
package com.xavax.util;

import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * IterableStringTokenizer wraps a StringTokenizer and makes it Iterable
 * so it can be used in for-each statements.
 */
public class IterableStringTokenizer implements Iterable<String> {
  public IterableStringTokenizer(String str) {
    tokenizer = new StringTokenizer(str);
  }

  public IterableStringTokenizer(String str, String delim) {
    tokenizer = new StringTokenizer(str, delim);
  }

  public IterableStringTokenizer(String str, String delim,
				 boolean returnDelims) {
    tokenizer = new StringTokenizer(str, delim, returnDelims);
  }

  public int countTokens() {
    return tokenizer.countTokens();
  }

  public boolean hasMoreTokens() {
    return tokenizer.hasMoreTokens();
  }

  public String nextToken() {
    return tokenizer.nextToken();
  }

  public String nextToken(String delim) {
    return tokenizer.nextToken(delim);
  }

  /**
   * Returns an Iterator for the underlying Enumeration, thereby making it
   * Iterable.
   * 
   * @return an Iterator for this enumeration.
   */
  public Iterator<String> iterator() {
    return new Iterator<String>() {
      public boolean hasNext() {
	return tokenizer.hasMoreElements();
      }

      public String next() {
	return tokenizer.nextToken();
      }

      public void remove() {
	throw new UnsupportedOperationException();
      }
    };
  }

  private StringTokenizer tokenizer;
}
