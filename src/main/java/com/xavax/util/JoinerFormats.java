package com.xavax.util;

/**
 * JoinerFormats defines commonly used formats.
 */
public class JoinerFormats {
  public final static JoinerFormat DEBUG_FORMAT =
      new JoinerFormat().withFieldNames(true)
      			.withQuotedStrings()
      			.disableModifications();
  public final static JoinerFormat JSON_FORMAT =
      new JoinerFormat().withFieldNames(true)
      			.withQuotedStrings()
      			.withQuotedFieldNames()
      			.withNullIndicator("null")
      			.disableModifications();
}
