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
  private final StringTokenizer tokenizer;

  /**
   * Construct an IterableStringTokenizer from the specified string.
   * @param string  the string to be tokenized.
   */
  public IterableStringTokenizer(final String string) {
    tokenizer = new StringTokenizer(string);
  }

  /**
   * Construct an IterableStringTokenizer from the specified string
   * using the specified delimiters.
   *
   * @param string  the string to be tokenized.
   * @param delim   the delimiters.
   */
  public IterableStringTokenizer(final String string, final String delim) {
    tokenizer = new StringTokenizer(string, delim);
  }

  /**
   * Construct an IterableStringTokenizer from the specified string
   * using the specified delimiters and return delimiters flag.
   *
   * @param string  the string to be tokenized.
   * @param delim   the delimiters.
   * @param returnDelims  true if delimiters should be returned as tokens.
   */
  public IterableStringTokenizer(final String string, final String delim,
				 final boolean returnDelims) {
    tokenizer = new StringTokenizer(string, delim, returnDelims);
  }

  /**
   * Returns the number of tokens.
   *
   * @return the number of tokens.
   */
  public int countTokens() {
    return tokenizer.countTokens();
  }

  /**
   * Returns true if this iterator has more tokens.
   *
   * @return true if this iterator has more tokens.
   */
  public boolean hasMoreTokens() {
    return tokenizer.hasMoreTokens();
  }

  /**
   * Returns the next token.
   *
   * @return the next token.
   */
  public String nextToken() {
    return tokenizer.nextToken();
  }

  /**
   * Returns the next token after changing delimiters.
   *
   * @param delim  the new delimiters.
   * @return the next token.
   */
  public String nextToken(final String delim) {
    return tokenizer.nextToken(delim);
  }

  /**
   * Returns an Iterator for the underlying Enumeration, thereby
   * making it Iterable.
   * 
   * @return an Iterator for this enumeration.
   */
  public Iterator<String> iterator() {
    return new Iterator<String>() {
      /**
       * Returns true if this iterator has more tokens.
       *
       * @return true if this iterator has more tokens.
       */
      @Override
      @SuppressWarnings({"PMD.AccessorMethodGeneration", "PMD.CommentRequired"})
      public boolean hasNext() {
	return tokenizer.hasMoreElements();
      }

      /**
       * Returns the next token.
       *
       * @return the next token.
       */
      @SuppressWarnings("PMD.AccessorMethodGeneration")
      public String next() {
	return tokenizer.nextToken();
      }

      /**
       * This method of the Iterator interface is not supported.
       */
      public void remove() {
	throw new UnsupportedOperationException();
      }
    };
  }
}
