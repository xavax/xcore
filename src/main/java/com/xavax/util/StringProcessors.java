//
// Copyright 2018 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//
package com.xavax.util;

import java.util.Locale;

import org.apache.commons.text.StringEscapeUtils;

/**
 * StringProcessors is a utility class that defines some common
 * string processors.
 */
@SuppressWarnings("PMD.ClassNamingConventions")
public class StringProcessors {
  public final static StringProcessor ESCAPE_HTML_STRING = new EscapeHtmlStringProcessor();
  public final static StringProcessor ESCAPE_JAVA_STRING = new EscapeJavaStringProcessor();
  public final static StringProcessor ESCAPE_JSON_STRING = new EscapeJsonStringProcessor();
  public final static StringProcessor LOWER_CASE_STRING  = new LowerCaseStringProcessor();
  public final static StringProcessor UPPER_CASE_STRING  = new UpperCaseStringProcessor();

  /**
   * Private constructor to prevent creating a default constructor.
   */
  private StringProcessors() {
    // Do nothing here.
  }

  /**
   * EscapeHtmlStringProcessor is a string processor that escapes all
   * special characters defined by HTML.
   */
  public static class EscapeHtmlStringProcessor implements StringProcessor {
    /**
     * Process an input string producing an output string.
     *
     * @param format  the joiner format. 
     * @param input   the input string.
     * @param clientData  any additional client data (or null).
     * @return a processed string.
     */
    @Override
    public String process(final JoinerFormat format, final String input, final Object clientData) {
      return StringEscapeUtils.escapeHtml4(input);
    }
  }

  /**
   * EscapeJavaStringProcessor is a string processor that escapes all
   * special characters defined by Java.
   */
  public static class EscapeJavaStringProcessor implements StringProcessor {
    /**
     * Process an input string producing an output string.
     *
     * @param format  the joiner format. 
     * @param input   the input string.
     * @param clientData  any additional client data (or null).
     * @return a processed string.
     */
    @Override
    public String process(final JoinerFormat format, final String input, final Object clientData) {
      return StringEscapeUtils.escapeJava(input);
    }
  }

  /**
   * EscapeJsonStringProcessor is a string processor that escapes all
   * special characters defined by JSON.
   */
  public static class EscapeJsonStringProcessor implements StringProcessor {
    /**
     * Process an input string producing an output string.
     *
     * @param format  the joiner format. 
     * @param input   the input string.
     * @param clientData  any additional client data (or null).
     * @return a processed string.
     */
    @Override
    public String process(final JoinerFormat format, final String input, final Object clientData) {
      return StringEscapeUtils.escapeJson(input);
    }
  }

  /**
   * LowerCaseStringProcessor is a string processor that converts all
   * characters to lower case.
   */
  public static class LowerCaseStringProcessor implements StringProcessor {
    /**
     * Process an input string producing an output string.
     *
     * @param format  the joiner format. 
     * @param input   the input string.
     * @param clientData  any additional client data (or null).
     * @return a processed string.
     */
    @Override
    public String process(final JoinerFormat format, final String input, final Object clientData) {
      return input.toLowerCase(Locale.getDefault());
    }
  }

  /**
   * UpperCaseStringProcessor is a string processor that converts all
   * characters to upper case.
   */
  public static class UpperCaseStringProcessor implements StringProcessor {
    /**
     * Process an input string producing an output string.
     *
     * @param format  the joiner format. 
     * @param input   the input string.
     * @param clientData  any additional client data (or null).
     * @return a processed string.
     */
    @Override
    public String process(final JoinerFormat format, final String input, final Object clientData) {
      return input.toUpperCase(Locale.getDefault());
    }
  }

}
