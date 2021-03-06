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
  private boolean writeEnabled = true;
  private char openQuoteCharacter   = DOUBLE_QUOTE;
  private char closeQuoteCharacter  = DOUBLE_QUOTE;
  private char arrayOpenCharacter   = LEFT_BRACKET;
  private char arrayCloseCharacter  = RIGHT_BRACKET;
  private char listOpenCharacter    = LEFT_BRACE;
  private char listCloseCharacter   = RIGHT_BRACE;
  private char mapOpenCharacter     = LEFT_BRACE;
  private char mapCloseCharacter    = RIGHT_BRACE;
  private char objectOpenCharacter  = LEFT_BRACE;
  private char objectCloseCharacter = RIGHT_BRACE;
  private int defaultCapacity       = DEFAULT_INITIAL_CAPACITY;
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
   * Sets the array open character.
   *
   * @param openCharacter   the new array open character.
   * @return this format.
   */
  public JoinerFormat withArrayOpenCharacter(final char openCharacter) {
    this.arrayOpenCharacter = openCharacter;
    return this;
  }

  /**
   * Returns the array open character.
   *
   * @return the array open character.
   */
  public char getArrayOpenCharacter() {
    return arrayOpenCharacter;
  }

  /**
   * Sets the array close character.
   *
   * @param closeCharacter   the new array close character.
   * @return this format.
   */
  public JoinerFormat withArrayCloseCharacter(final char closeCharacter) {
    this.arrayCloseCharacter = closeCharacter;
    return this;
  }

  /**
   * Returns the array close character.
   *
   * @return the array close character.
   */
  public char getArrayCloseCharacter() {
    return arrayCloseCharacter;
  }

  /**
   * Sets the list open character.
   *
   * @param openCharacter  the new list open character.
   * @return this format.
   */
  public JoinerFormat withListOpenCharacter(final char openCharacter) {
    this.listOpenCharacter = openCharacter;
    return this;
  }

  /**
   * Returns the list open character.
   *
   * @return the list open character.
   */
  public char getListOpenCharacter() {
    return listOpenCharacter;
  }

  /**
   * Sets the list close character.
   *
   * @param closeCharacter   the new list close character.
   * @return this format.
   */
  public JoinerFormat withListCloseCharacter(final char closeCharacter) {
    this.listCloseCharacter = closeCharacter;
    return this;
  }

  /**
   * Returns the list close character.
   *
   * @return the list close character.
   */
  public char getListCloseCharacter() {
    return listCloseCharacter;
  }

  /**
   * Sets the map open character.
   *
   * @param openCharacter   the new map open character.
   * @return this format.
   */
  public JoinerFormat withMapOpenCharacter(final char openCharacter) {
    this.mapOpenCharacter = openCharacter;
    return this;
  }

  /**
   * Returns the map open character.
   *
   * @return the map open character.
   */
  public char getMapOpenCharacter() {
    return mapOpenCharacter;
  }

  /**
   * Sets the map close character.
   *
   * @param closeCharacter   the new map close character.
   * @return this format.
   */
  public JoinerFormat withMapCloseCharacter(final char closeCharacter) {
    this.mapCloseCharacter = closeCharacter;
    return this;
  }

  /**
   * Returns the map close character.
   *
   * @return the map close character.
   */
  public char getMapCloseCharacter() {
    return mapCloseCharacter;
  }

  /**
   * Sets the object open character.
   *
   * @param openCharacter   the new object open character.
   * @return this format.
   */
  public JoinerFormat withObjectOpenCharacter(final char openCharacter) {
    this.objectOpenCharacter = openCharacter;
    return this;
  }

  /**
   * Returns the object open character.
   *
   * @return the object open character.
   */
  public char getObjectOpenCharacter() {
    return objectOpenCharacter;
  }

  /**
   * Sets the object close character.
   *
   * @param closeCharacter   the new object close character.
   * @return this format.
   */
  public JoinerFormat withObjectCloseCharacter(final char closeCharacter) {
    this.objectCloseCharacter = closeCharacter;
    return this;
  }

  /**
   * Returns the object close character.
   *
   * @return the object close character.
   */
  public char getObjectCloseCharacter() {
    return objectCloseCharacter;
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
