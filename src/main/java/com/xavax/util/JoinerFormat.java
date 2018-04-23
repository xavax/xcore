//
// Copyright 2018 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//
package com.xavax.util;

import static com.xavax.util.Constants.*;

import java.util.ArrayList;
import java.util.List;

/**
 * JoinerFormat encapsulates the formatting options for Joiner.
 */
@SuppressWarnings("PMD.TooManyFields")
public class JoinerFormat {
  private final static int DEFAULT_INITIAL_CAPACITY = 64;
  public final static JoinerFormat DEBUG_FORMAT = new JoinerFormat()
      .withFieldNames(true).withQuotedStrings().disableModificationa();
  public final static JoinerFormat JSON_FORMAT = new JoinerFormat()
      .withFieldNames(true).withQuotedStrings().withQuotedFieldNames().disableModificationa();
  private boolean quoteFieldNames;
  private boolean quoteStrings;
  private boolean skipNulls;
  private boolean withFieldNames;
  private boolean writeEnabled     = true;
  private char openQuoteCharacter  = DOUBLE_QUOTE;
  private char closeQuoteCharacter = DOUBLE_QUOTE;
  private char arrayOpenCharacter  = LEFT_BRACKET;
  private char arrayCloseCharacter = RIGHT_BRACKET;
  private char listOpenCharacter   = LEFT_BRACE;
  private char listCloseCharacter  = RIGHT_BRACE;
  private char mapOpenCharacter    = LEFT_BRACE;
  private char mapCloseCharacter   = RIGHT_BRACE;
  private int defaultCapacity      = DEFAULT_INITIAL_CAPACITY;
  private int maxDepth;
  private String defaultSeparator = COMMA_SEPARATOR;
  private String fieldSeparator   = COMMA_SEPARATOR;
  private String itemSeparator    = COMMA_SEPARATOR;
  private String mapKeySeparator  = COLON_SEPARATOR;
  private String nameSeparator    = COLON_SEPARATOR;
  private String nullIndicator    = NULL_INDICATOR;
  private String prefix = EMPTY_STRING;
  private String suffix = EMPTY_STRING;
  private List<StringProcessor> processors;

  /**
   * Construct a JoinerFormat.
   */
  public JoinerFormat() {
    processors = new ArrayList<>();
  }

  /**
   * Construct a JoinerFormat that is a copy of an exemplar.
   *
   * @param exemplar  the format to copy.
   */
  public JoinerFormat(final JoinerFormat exemplar) {
    this();
    this.quoteFieldNames = exemplar.quoteFieldNames;
    this.quoteStrings = exemplar.quoteStrings;
    this.skipNulls = exemplar.skipNulls;
    this.withFieldNames = exemplar.withFieldNames;
    this.openQuoteCharacter = exemplar.openQuoteCharacter;
    this.closeQuoteCharacter = exemplar.closeQuoteCharacter;
    this.arrayOpenCharacter = exemplar.arrayOpenCharacter;
    this.arrayCloseCharacter = exemplar.arrayCloseCharacter;
    this.listOpenCharacter = exemplar.listOpenCharacter;
    this.listCloseCharacter = exemplar.listCloseCharacter;
    this.mapOpenCharacter = exemplar.mapOpenCharacter;
    this.mapCloseCharacter = exemplar.mapCloseCharacter;
    this.maxDepth = exemplar.maxDepth;
    this.defaultSeparator = exemplar.defaultSeparator;
    this.fieldSeparator = exemplar.fieldSeparator;
    this.itemSeparator = exemplar.itemSeparator;
    this.mapKeySeparator = exemplar.mapKeySeparator;
    this.nameSeparator = exemplar.nameSeparator;
    this.nullIndicator = exemplar.nullIndicator;
    this.prefix = exemplar.prefix;
    this.suffix = exemplar.suffix;
    processors.addAll(exemplar.processors);
  }

  /**
   * Create a JoinerFormat.
   *
   * @return a JoinerFormat
   */
  public static JoinerFormat create() {
    return new JoinerFormat();
  }

  /**
   * Create a JoinerFormat that is a copy of an exemplar.
   *
   * @param exemplar  the format to copy.
   * @return a JoinerFormat.
   */
  public static JoinerFormat from(final JoinerFormat exemplar) {
    return new JoinerFormat(exemplar);
  }

  /**
   * Sets the default initial capacity of the underlying StringBuilder.
   *
   * @param capacity  the new default initial capacity.
   * @return this JoinerFormat.
   */
  public JoinerFormat setDefaultCapacity(final int capacity) {
    if ( writeEnabled ) {
      defaultCapacity = capacity;
    }
    return this;
  }

  /**
   * Returns the default initial capacity of the underlying StringBuilder.
   *
   * @return the default initial capacity of the underlying StringBuilder.
   */
  public int getDefaultCapacity() {
    return defaultCapacity;
  }

  /**
   * Sets the skipNulls flag to true.
   *
   * @return this JoinerFormat.
   */
  public JoinerFormat skipNulls() {
    if ( writeEnabled ) {
      skipNulls = true;
    }
    return this;
  }

  /**
   * Sets the skipNulls flag to true.
   *
   * @return this JoinerFormat.
   */
  public JoinerFormat skipNulls(final boolean skipNulls) {
    if ( writeEnabled ) {
      this.skipNulls = skipNulls;
    }
    return this;
  }

  /**
   * Returns true if null values should be skipped.
   *
   * @return true if null values should be skipped.
   */
  public boolean hasSkipNulls() {
    return skipNulls;
  }

  /**
   * Sets the null indicator to the specified string.
   *
   * @param nullIndicator  the new null indicator.
   * @return this JoinerFormat.
   */
  public final JoinerFormat withNullIndicator(final String nullIndicator) {
    if ( writeEnabled ) {
      this.nullIndicator = nullIndicator == null ? EMPTY_STRING : nullIndicator;
    }
    return this;
  }

  /**
   * Returns the null indicator string.
   *
   * @return the null indicator string.
   */
  public String getNullIndicator() {
    return nullIndicator;
  }

  /**
   * Sets the quoteStrings flag to true causing strings in the
   * output to be quoted.
   *
   * @return this JoinerFormat.
   */
  public JoinerFormat withQuotedStrings() {
    if ( writeEnabled ) {
      this.quoteStrings = true;
    }
    return this;
  }

  /**
   * Returns true if strings should be quoted.
   *
   * @return true if strings should be quoted.
   */
  public boolean hasQuotedStrings() {
    return quoteStrings;
  }

  /**
   * Enable quoting of field names within objects.
   *
   * @return this JoinerFormat.
   */
  public JoinerFormat withQuotedFieldNames() {
    quoteFieldNames = true;
    return this;
  }

  /**
   * Returns true if field names within objects should be quoted.
   *
   * @return true if field names within objects should be quoted.
   */
  public boolean hasQuotedFieldNames() {
    return quoteFieldNames;
  }

  /**
   * Adds a string processor to the list of processors.
   * 
   * @param processor  the processor to add.
   * @return this JoinerFormat.
   */
  public JoinerFormat withProcessor(final StringProcessor processor) {
    processors.add(processor);
    return this;
  }

  /**
   * Returns the list of string processors, or null if there are
   * no string processors.
   *
   * @return the list of string processors.
   */
  public List<StringProcessor> getProcessors() {
    return processors;
  }

  /**
   * Sets the separator to the specified string.
   *
   * @param separator  the new separator.
   * @return this JoinerFormat.
   */
  public final JoinerFormat withSeparator(final String separator) {
    if ( writeEnabled ) {
      defaultSeparator = separator == null ? EMPTY_STRING : separator;
    }
    return this;
  }

  /**
   * Returns the default separator.
   * @return the default separator.
   */
  public String getSeparator() {
    return defaultSeparator;
  }

  /**
   * Sets the item separator to the specified string. This
   * is used to separate items in an array or collection.
   *
   * @param separator the new item separator.
   * @return this JoinerFormat.
   */
  public final JoinerFormat withItemSeparator(final String separator) {
    if ( writeEnabled ) {
      this.itemSeparator = separator == null ? EMPTY_STRING : separator;
    }
    return this;
  }

  /**
   * Returns the item separator.
   * @return the item separator.
   */
  public String getItemSeparator() {
    return itemSeparator;
  }

  /**
   * Sets the field name separator to the specified string.
   * This can be used to achieve the appearance:
   *   street = 123 Main Street
   * by setting the field name separator to " = ". The
   * default is ": " which has the appearance:
   *   street: 123 Main Street
   *
   * @param separator the new field name separator.
   * @return this JoinerFormat.
   */
  public final JoinerFormat withFieldNameSeparator(final String separator) {
    if ( writeEnabled ) {
      this.nameSeparator = separator == null ? COLON_SEPARATOR : separator;
    }
    return this;
  }

  /**
   * Returns the field name separator.
   * @return the field name separator.
   */
  public String getFieldNameSeparator() {
    return nameSeparator;
  }

  /**
   * Sets the withFieldNames flag. If this flag is true and
   * field names are displayed, fields will be displayed as:
   *   firstName: John
   *
   * @param withFieldNames  true if field names should be displayed.
   * @return this joiner.
   */
  public JoinerFormat withFieldNames(final boolean withFieldNames) {
    if ( writeEnabled ) {
      this.withFieldNames = withFieldNames;
    }
    return this;
  }

  /**
   * Returns true if field name are enabled.
   *
   * @return true if field name are enabled.
   */
  public boolean hasFieldNames() {
    return withFieldNames;
  }

  /**
   * Set the max depth for joining nested joinable objects.
   * The maximum level is specified by MAX_LEVEL.
   *
   * @param maxDepth  the maximum depth.
   * @return this joiner.
   */
  public JoinerFormat withMaxDepth(final int maxDepth) {
    if ( writeEnabled ) {
      this.maxDepth = maxDepth < 0 ? 0 : maxDepth;
    }
    return this;
  }

  /**
   * Returns the maximum depth.
   *
   * @return the maximum depth.
   */
  public int getMaxDepth() {
    return maxDepth;
  }

  /**
   * Sets the prefix to the specified string. The prefix will
   * be prepended to the final result of joining. This is only
   * used by the join method.
   *
   * @param prefix  the prefix string.
   * @return  this JoinerFormat.
   */
  public final JoinerFormat withPrefix(final String prefix) {
    if ( writeEnabled ) {
      this.prefix = prefix == null ? EMPTY_STRING : prefix;
    }
    return this;
  }

  /**
   * Returns the prefix.
   *
   * @return the prefix
   */
  public String getPrefix() {
    return prefix;
  }

  /**
   * Sets the suffix to the specified string. The suffix will
   * be appended to the final result of joining. This is only
   * used by the join method.
   *
   * @param suffix  the suffix string.
   * @return  this JoinerFormat.
   */
  public final JoinerFormat withSuffix(final String suffix) {
    if ( writeEnabled ) {
      this.suffix = suffix == null ? EMPTY_STRING : suffix;
    }
    return this;
  }

  /**
   * Returns the suffix.
   * @return the suffix
   */
  public String getSuffix() {
    return suffix;
  }

  /**
   * Disable modification of this format.
   */
  public JoinerFormat disableModificationa() {
    writeEnabled = false;
    return this;
  }
}
