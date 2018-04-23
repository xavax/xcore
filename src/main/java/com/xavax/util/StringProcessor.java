//
// Copyright 2018 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//
package com.xavax.util;

/**
 * StringProcessor is an interface for string processors that
 * can be used in conjunction with joiners.
 */
public interface StringProcessor {
  /**
   * Process an input string producing an output string.
   *
   * @param format  the joiner format. 
   * @param input   the input string.
   * @param clientData  any additional client data (or null).
   * @return a processed string.
   */
  public String process(final JoinerFormat format, final String input, final Object clientData);
}
