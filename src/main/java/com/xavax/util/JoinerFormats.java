package com.xavax.util;

import static com.xavax.util.Constants.*;

/**
 * JoinerFormats defines commonly used formats.
 */
public class JoinerFormats {
  public final static String TAB_INDENT = "\t";
  public final static JoinerFormat DEBUG_FORMAT =
      new JoinerFormat().withFieldNames(true)
      			.withQuotedStrings()
      			.disableModifications();
  public final static JoinerFormat VERBOSE_DEBUG_FORMAT =
      new JoinerFormat(DEBUG_FORMAT).withFieldNames(true)
      				    .withQuotedStrings()
      				    .withIndent(TAB_INDENT)
      				    .withItemSeparator(COMMA_SEPARATOR)
      				    .withListOpenString(LEFT_BRACKET_NEWLINE)
      				    .withMapOpenString(LEFT_BRACE_NEWLINE)
      				    .withObjectOpenString(LEFT_BRACE_STRING)
      				    .disableModifications();
  public final static JoinerFormat JSON_FORMAT =
      new JoinerFormat().withFieldNames(true)
      			.withQuotedStrings()
      			.withQuotedFieldNames()
      			.withNullIndicator("null")
      			.disableModifications();
  public final static JoinerFormat VERBOSE_JSON_FORMAT =
      new JoinerFormat(JSON_FORMAT).withFieldNames(true)
      				   .withQuotedStrings()
      				   .withQuotedFieldNames()
      				   .withIndent(TAB_INDENT)
      				   .withListOpenString(LEFT_BRACKET_NEWLINE)
      				   .withMapOpenString(LEFT_BRACE_NEWLINE)
      				   .withObjectOpenString(LEFT_BRACE_NEWLINE)
      				   .withNullIndicator("null")
      				   .disableModifications();
}
