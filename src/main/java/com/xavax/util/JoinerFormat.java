//
// Copyright 2018 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//
package com.xavax.util;

import static com.xavax.util.Constants.*;

import java.util.ArrayList;
import java.util.List;

import com.xavax.exception.InvalidModification;

/**
 * JoinerFormat encapsulates the formatting options for Joiner.
 */
@SuppressWarnings({ "PMD.TooManyFields", "PMD.TooManyMethods"})
public class JoinerFormat {
  private final static int DEFAULT_INITIAL_CAPACITY = 64;

  private boolean quoteFieldNames;
  private boolean quoteStrings;
  private boolean skipNulls;
  private boolean withFieldNames;
  private boolean withIndent;
  private boolean writeEnabled     = true;
  private char openQuoteCharacter  = DOUBLE_QUOTE;
  private char closeQuoteCharacter = DOUBLE_QUOTE;
  private int defaultCapacity      = DEFAULT_INITIAL_CAPACITY;
  private int maxDepth;
  private String arrayCloseString  = RIGHT_BRACKET_STRING;
  private String arrayOpenString   = LEFT_BRACKET_STRING;
  private String defaultSeparator  = COMMA_SPACE;
  private String fieldSeparator    = COMMA_SPACE;
  private String indentString;
  private String itemSeparator     = COMMA_SPACE;
  private String listCloseString   = RIGHT_BRACE_STRING;
  private String listOpenString    = LEFT_BRACE_STRING;
  private String mapCloseString    = RIGHT_BRACE_STRING;
  private String mapOpenString     = LEFT_BRACE_STRING;
  private String mapKeySeparator   = COLON_SEPARATOR;
  private String nameSeparator     = COLON_SEPARATOR;
  private String nullIndicator     = NULL_INDICATOR;
  private String objectCloseString = RIGHT_BRACE_STRING;
  private String objectOpenString  = LEFT_BRACE_STRING;
  private String prefix            = EMPTY_STRING;
  private String suffix            = EMPTY_STRING;
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
    this.withIndent = exemplar.withIndent;
    this.openQuoteCharacter = exemplar.openQuoteCharacter;
    this.closeQuoteCharacter = exemplar.closeQuoteCharacter;
    this.arrayOpenString = exemplar.arrayOpenString;
    this.arrayCloseString = exemplar.arrayCloseString;
    this.listOpenString = exemplar.listOpenString;
    this.listCloseString = exemplar.listCloseString;
    this.mapOpenString = exemplar.mapOpenString;
    this.mapCloseString = exemplar.mapCloseString;
    this.maxDepth = exemplar.maxDepth;
    this.defaultSeparator = exemplar.defaultSeparator;
    this.fieldSeparator = exemplar.fieldSeparator;
    this.indentString = exemplar.indentString;
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
  public JoinerFormat withDefaultCapacity(final int capacity) {
    if ( checkAccess() ) {
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
    return withSkipNulls(true);
  }

  /**
   * Sets the skipNulls flag to true.
   *
   * @param skipNulls  true if nulls should be skipped.
   * @return this JoinerFormat.
   */
  public JoinerFormat withSkipNulls(final boolean skipNulls) {
    if ( checkAccess() ) {
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
    if ( checkAccess() ) {
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
   * Sets the open quote character.
   *
   * @param openQuoteCharacter   the new open quote character.
   * @return this format.
   */
  public JoinerFormat withOpenQuoteCharacter(final char openQuoteCharacter) {
    this.openQuoteCharacter = openQuoteCharacter;
    return this;
  }

  /**
   * Returns the open quote character.
   *
   * @return the open quote character.
   */
  public char getOpenQuoteCharacter() {
    return openQuoteCharacter;
  }

  /**
   * Sets the close quote character.
   *
   * @param closeQuoteCharacter   the new close quote character.
   * @return this format.
   */
  public JoinerFormat withCloseQuoteCharacter(final char closeQuoteCharacter) {
    this.closeQuoteCharacter = closeQuoteCharacter;
    return this;
  }

  /**
   * Returns the close quote character.
   *
   * @return the close quote character.
   */
  public char getCloseQuoteCharacter() {
    return closeQuoteCharacter;
  }

  /**
   * Sets the quoteStrings flag to true causing strings in the
   * output to be quoted.
   *
   * @return this JoinerFormat.
   */
  public JoinerFormat withQuotedStrings() {
    if ( checkAccess() ) {
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
   * Sets the array open string.
   *
   * @param openString   the new array open string.
   * @return this format.
   */
  public JoinerFormat withArrayOpenString(final String openString) {
    this.arrayOpenString = openString;
    return this;
  }

  /**
   * Returns the array open string.
   *
   * @return the array open string.
   */
  public String getArrayOpenString() {
    return arrayOpenString;
  }

  /**
   * Sets the array close string.
   *
   * @param closeString   the new array close string.
   * @return this format.
   */
  public JoinerFormat withArrayCloseString(final String closeString) {
    this.arrayCloseString = closeString;
    return this;
  }

  /**
   * Returns the array close string.
   *
   * @return the array close string.
   */
  public String getArrayCloseString() {
    return arrayCloseString;
  }

  /**
   * Sets indent string and enable indentation if the string
   * is not null.
   *
   * @param indentString  the indent string.
   * @return this format.
   */
  public JoinerFormat withIndent(final String indentString) {
    this.withIndent = indentString != null;
    this.indentString = indentString;
    return this;
  }

  /**
   * Returns true if indentation is enabled.
   *
   * @return true if indentation is enabled.
   */
  public boolean withIndent() {
    return withIndent;
  }

  /**
   * Returns the indent string.
   *
   * @return the indent string.
   */
  public String getIndentString() {
    return indentString;
  }

  /**
   * Sets the list open string.
   *
   * @param openString  the new list open string.
   * @return this format.
   */
  public JoinerFormat withListOpenString(final String openString) {
    this.listOpenString = openString;
    return this;
  }

  /**
   * Returns the list open string.
   *
   * @return the list open string.
   */
  public String getListOpenString() {
    return listOpenString;
  }

  /**
   * Sets the list close string.
   *
   * @param closeString   the new list close string.
   * @return this format.
   */
  public JoinerFormat withListCloseString(final String closeString) {
    this.listCloseString = closeString;
    return this;
  }

  /**
   * Returns the list close string.
   *
   * @return the list close string.
   */
  public String getListCloseString() {
    return listCloseString;
  }

  /**
   * Sets the map open string.
   *
   * @param openString   the new map open string.
   * @return this format.
   */
  public JoinerFormat withMapOpenString(final String openString) {
    this.mapOpenString = openString;
    return this;
  }

  /**
   * Returns the map open string.
   *
   * @return the map open string.
   */
  public String getMapOpenString() {
    return mapOpenString;
  }

  /**
   * Sets the map close string.
   *
   * @param closeString   the new map close string.
   * @return this format.
   */
  public JoinerFormat withMapCloseString(final String closeString) {
    this.mapCloseString = closeString;
    return this;
  }

  /**
   * Returns the map close string.
   *
   * @return the map close string.
   */
  public String getMapCloseString() {
    return mapCloseString;
  }

  /**
   * Sets the object open string.
   *
   * @param openString   the new object open string.
   * @return this format.
   */
  public JoinerFormat withObjectOpenString(final String openString) {
    this.objectOpenString = openString;
    return this;
  }

  /**
   * Returns the object open string.
   *
   * @return the object open string.
   */
  public String getObjectOpenString() {
    return objectOpenString;
  }

  /**
   * Sets the object close string.
   *
   * @param closeString   the new object close string.
   * @return this format.
   */
  public JoinerFormat withObjectCloseString(final String closeString) {
    this.objectCloseString = closeString;
    return this;
  }

  /**
   * Returns the object close string.
   *
   * @return the object close string.
   */
  public String getObjectCloseString() {
    return objectCloseString;
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
    if ( checkAccess() ) {
      defaultSeparator = separator == null ? EMPTY_STRING : separator;
    }
    return this;
  }

  /**
   * Returns the default separator.
   *
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
    if ( checkAccess() ) {
      this.itemSeparator = separator == null ? EMPTY_STRING : separator;
    }
    return this;
  }

  /**
   * Returns the item separator.
   *
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
    if ( checkAccess() ) {
      this.nameSeparator = separator == null ? COLON_SEPARATOR : separator;
    }
    return this;
  }

  /**
   * Returns the field name separator.
   *
   * @return the field name separator.
   */
  public String getFieldNameSeparator() {
    return nameSeparator;
  }

  /**
   * Sets the map key separator to the specified string.
   *
   * @param separator the new map key separator.
   * @return this JoinerFormat.
   */
  public final JoinerFormat withMapKeySeparator(final String separator) {
    if ( checkAccess() ) {
      this.mapKeySeparator = separator == null ? COLON_SEPARATOR : separator;
    }
    return this;
  }

  /**
   * Returns the map key separator.
   *
   * @return the map key separator.
   */
  public String getMapKeySeparator() {
    return mapKeySeparator;
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
    if ( checkAccess() ) {
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
    if ( checkAccess() ) {
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
    if ( checkAccess() ) {
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
    if ( checkAccess() ) {
      this.suffix = suffix == null ? EMPTY_STRING : suffix;
    }
    return this;
  }

  /**
   * Returns the suffix.
   *
   * @return the suffix
   */
  public String getSuffix() {
    return suffix;
  }

  /**
   * Disable modification of this format. Subsequent attempts to
   * modify this format will throw an InvalidModification exception.
   * There is no mechanism to enable modifications once this method
   * is called; however, a modifiable copy can be made using the
   * copy constructor or the from method.
   *
   * @return this JoinerFormat.
   */
  public JoinerFormat disableModifications() {
    writeEnabled = false;
    return this;
  }

  /**
   * Returns true if this format is write-enabled; otherwise,
   * throws an InvalidModification exception.
   *
   * @return true if this format is write-enabled.
   * @throw InvalidModification if this format is read-only.
   */
  boolean checkAccess() {
    if ( !writeEnabled ) {
      throw new InvalidModification(this.getClass().getName());
    }
    return writeEnabled;
  }
}
