//
// Copyright 2015 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//
package com.xavax.util;

import static com.xavax.util.Constants.*;

import java.util.Collection;

/**
 * Joiner supports the efficient implementation of toString methods
 * for complex objects.
 */
public class Joiner {
  private final static int DEFAULT_INITIAL_CAPACITY = 64;

  private boolean first = true;
  private boolean quoteStrings;
  private boolean reusable;
  private boolean skipNulls;
  private String nullIndicator = NULL_INDICATOR;
  private String separator = COMMA_SEPARATOR;
  
  // Joiner is a transient object so this warning is not relevant.
  @SuppressWarnings("PMD.AvoidStringBufferField")
  private final StringBuilder builder;

  /**
   * Construct a joiner with the default initial capacity.
   */
  public Joiner() {
    this(DEFAULT_INITIAL_CAPACITY);
  }

  /**
   * Construct a joiner with the specified initial capacity.
   *
   * @param initialCapacity the initial capacity.
   */
  public Joiner(final int initialCapacity) {
    builder = new StringBuilder(initialCapacity);
  }

  /**
   * Create a joiner with the default initial capacity.
   *
   * @return a new Joiner.
   */
  public static Joiner create() {
    return new Joiner();
  }

  /**
   * Create a joiner with the specified initial capacity.
   *
   * @param initialCapacity the initial capacity.
   * @return a new Joiner.
   */
  public static Joiner create(final int initialCapacity) {
    return new Joiner(initialCapacity);
  }

  /**
   * Make this joiner reusable. After calling toString, the internal
   * string builder will be reset to empty.
   *
   * @return this Joiner.
   */
  public Joiner reusable() {
    this.reusable = true;
    return this;
  }

  /**
   * Sets the skipNulls flag to true.
   *
   * @return this Joiner.
   */
  public Joiner skipNulls() {
    skipNulls = true;
    return this;
  }

  /**
   * Sets the null indicator to the specified string.
   *
   * @param nullIndicator  the new null indicator.
   * @return this Joiner.
   */
  public Joiner withNullIndicator(final String nullIndicator) {
    this.nullIndicator = nullIndicator;
    return this;
  }

  /**
   * Sets the quoteStrings flag to true causing strings in the
   * output to be quoted.
   *
   * @return this Joiner.
   */
  public Joiner withQuotedStrings() {
    this.quoteStrings = true;
    return this;
  }

  /**
   * Sets the separator to the specified string.
   *
   * @param separator  the new separator.
   * @return this Joiner.
   */
  public Joiner withSeparator(final String separator) {
    this.separator = separator;
    return this;
  }

  /**
   * Append a boolean value to the output.
   *
   * @param value  the value to append to the output.
   * @return this joiner.
   */
  public Joiner append(final boolean value) {
    return append(null, value);
  }

  /**
   * Append a boolean value to the output.
   *
   * @param name   the field name.
   * @param value  the value to append to the output.
   * @return this joiner.
   */
  public Joiner append(final String name, final boolean value) {
    beginField(name);
    builder.append(value);
    return this;
  }

  /**
   * Append a boolean value to the output.
   *
   * @param value  the value to append to the output.
   * @return this joiner.
   */
  public Joiner append(final Boolean value) {
    return append(null, value);
  }

  /**
   * Append a boolean value to the output.
   *
   * @param name   the field name.
   * @param value  the value to append to the output.
   * @return this joiner.
   */
  public Joiner append(final String name, final Boolean value) {
    return appendField(name, value);
  }

  /**
   * Append a character value to the output.
   *
   * @param value  the value to append to the output.
   * @return this joiner.
   */
  public Joiner append(final char value) {
    return append(null, value);
  }

  /**
   * Append a character value to the output.
   *
   * @param name   the field name.
   * @param value  the value to append to the output.
   * @return this joiner.
   */
  public Joiner append(final String name, final char value) {
    beginField(name);
    builder.append(value);
    return this;
  }

  /**
   * Append a character value to the output.
   *
   * @param value  the value to append to the output.
   * @return this joiner.
   */
  public Joiner append(final Character value) {
    return append(null, value);
  }

  /**
   * Append a character value to the output.
   *
   * @param name   the field name.
   * @param value  the value to append to the output.
   * @return this joiner.
   */
  public Joiner append(final String name, final Character value) {
    return appendField(name, value);
  }

  /**
   * Append a byte value to the output.
   *
   * @param value  the value to append to the output.
   * @return this joiner.
   */
  public Joiner append(final byte value) {
    return append(null, value);
  }

  /**
   * Append a byte value to the output.
   *
   * @param name   the field name.
   * @param value  the value to append to the output.
   * @return this joiner.
   */
  public Joiner append(final String name, final byte value) {
    beginField(name);
    builder.append(value);
    return this;
  }

  /**
   * Append a short integer value to the output.
   *
   * @param value  the value to append to the output.
   * @return this joiner.
   */
  public Joiner append(final short value) {
    return append(null, value);
  }

  /**
   * Append a short integer value to the output.
   *
   * @param name   the field name.
   * @param value  the value to append to the output.
   * @return this joiner.
   */
  public Joiner append(final String name, final short value) {
    beginField(name);
    builder.append(value);
    return this;
  }

  /**
   * Append an integer value to the output.
   *
   * @param value  the value to append to the output.
   * @return this joiner.
   */
  public Joiner append(final int value) {
    return append(null, value);
  }

  /**
   * Append an integer value to the output.
   *
   * @param name   the field name.
   * @param value  the value to append to the output.
   * @return this joiner.
   */
  public Joiner append(final String name, final int value) {
    beginField(name);
    builder.append(value);
    return this;
  }

  /**
   * Append a long value to the output.
   *
   * @param value  the value to append to the output.
   * @return this joiner.
   */
  public Joiner append(final long value) {
    return append(null, value);
  }

  /**
   * Append a long value to the output.
   *
   * @param name   the field name.
   * @param value  the value to append to the output.
   * @return this joiner.
   */
  public Joiner append(final String name, final long value) {
    beginField(name);
    builder.append(value);
    return this;
  }

  /**
   * Append a Number to the output.
   *
   * @param value  the value to append to the output.
   * @return this joiner.
   */
  public Joiner append(final Number value) {
    return appendField(null, value);
  }

  /**
   * Append a Number to the output.
   *
   * @param name   the field name.
   * @param value  the value to append to the output.
   * @return this joiner.
   */
  public Joiner append(final String name, final Number value) {
    return appendField(name, value);
  }

  /**
   * Append a field. Append the separator if this is
   * the first field.
   *
   * @param name   the field name.
   * @param field  the field being appended.
   * @return this Joiner.
   */
  public Joiner appendField(final String name, final Object field) {
    if ( field == null ) {
      if ( !skipNulls ) {
	beginField(name);
	builder.append(nullIndicator);
      }
    }
    else {
      beginField(name);
      builder.append(field);
    }
    return this;
  }

  /**
   * Append a string.
   *
   * @param string  the string to append.
   * @return this Joiner.
   */
  public Joiner append(final String string) {
    return append(null, string);
  }

  /**
   * Append a string.
   *
   * @param name    the field name.
   * @param string  the string to append.
   * @return this Joiner.
   */
  public Joiner append(final String name, final String string) {
    if ( string == null ) {
      if ( !skipNulls ) {
	beginField(name);
	builder.append(nullIndicator);
      }
    }
    else {
      beginField(name);
      if ( quoteStrings ) {
	builder.append('"')
	       .append(string)
	       .append('"');
      }
      else {
	builder.append(string);
      }
    }
    return this;
  }

  /**
   * Append an object.
   *
   * @param object  the object to be joined.
   * @return this Joiner.
   */
  public Joiner append(final Object object) {
    return append(null, object);
  }

  /**
   * Append an object.
   *
   * @param name    the field name.
   * @param object  the object to be joined.
   * @return this Joiner.
   */
  public Joiner append(final String name, final Object object) {
    if ( object == null ) {
      if ( !skipNulls ) {
	beginField(name);
	builder.append(nullIndicator);
      }
    }
    else {
      if ( object instanceof Joinable ) {
	((Joinable) object).join(this);
      }
      else {
	beginObject();
	builder.append(object.toString());   
	endObject();
      }
    }
    return this;
  }

  /**
   * Append an array of objects.
   *
   * @param objects  the array of objects to be joined.
   * @return this Joiner.
   */
  public Joiner append(final Object... objects) {
    if ( objects == null ) {
      if ( !skipNulls ) {
	builder.append(nullIndicator);
      }
    }
    else {
      beginArray();
      for ( final Object object : objects ) {
	append(object);
      }
      endArray();
    }
    return this;
  }

  /**
   * Append a collection.
   *
   * @param collection  the collection to be joined.
   * @return this Joiner.
   */
  public Joiner append(final Collection<?> collection) {
    if ( collection == null ) {
      if ( !skipNulls ) {
	builder.append(nullIndicator);
      }
    }
    else {
      beginCollection();
      for ( final Object object : collection ) {
	append(object);
      }
      endCollection();
    }
    return this;
  }

  /**
   * Begin joining an array.
   *
   * @return this Joiner.
   */
  public Joiner beginArray() {
    return beginEntity(LEFT_BRACKET);
  }

  /**
   * Finish joining an array.
   *
   * @return this Joiner.
   */
  public Joiner endArray() {
    return endEntity(RIGHT_BRACKET);
  }

  /**
   * Begin joining an collection.
   *
   * @return this Joiner.
   */
  public Joiner beginCollection() {
    return beginEntity(LEFT_BRACE);
  }

  /**
   * Finish joining an collection.
   *
   * @return this Joiner.
   */
  public Joiner endCollection() {
    return endEntity(RIGHT_BRACE);
  }

  /**
   * Begin joining an object.
   *
   * @return this Joiner.
   */
  public Joiner beginObject() {
    return beginEntity(LEFT_PAREN);
  }

  /**
   * Finish joining an object.
   *
   * @return this Joiner.
   */
  public Joiner endObject() {
    return endEntity(RIGHT_PAREN);
  }

  /**
   * Append a string to the output.
   *
   * @param string  the string to append.
   * @return this Joiner.
   */
  public Joiner appendRaw(final String string) {
    if ( string != null ) {
      builder.append(string);
    }
    return this;
  }

  /**
   * Returns the contents of this Joiner as a string.
   *
   * @return the contents of this Joiner as a string.
   */
  public String toString() {
    final String result = builder.toString();
    if ( reusable ) {
      builder.setLength(0);
      builder.trimToSize();
    }
    return result;
  }

  /**
   * Returns the string builder used for joining.
   *
   * @return the string builder used for joining.
   */
  public StringBuilder getBuilder() {
    return this.builder;
  }

  /**
   * Returns the null indicator string.
   *
   * @return the null indicator string.
   */
  public String getNullIndicator() {
    return this.nullIndicator; 
  }

  /**
   * Begin joining a field. Append the separator if this is
   * the first field.
   *
   * @param name  the field name (omit if null).
   * @return this Joiner.
   */
  private Joiner beginField(final String name) {
    if ( first ) {
      first = false;
    }
    else {
      builder.append(separator);
    }
    if ( name != null ) {
      builder.append(name)
             .append(COLON_SEPARATOR);
    }
    return this;
  }

  /**
   * Begin joining an object, array, or collection.
   *
   * @param beginChar  the beginning character.
   * @return this Joiner.
   */
  private Joiner beginEntity(final char beginChar) {
    if ( !first ) {
      builder.append(separator);
      first = true;
    }
    builder.append(beginChar);
    return this;
  }

  /**
   * End joining an object, array, or collection.
   * @param endChar
   * @return
   */
  private Joiner endEntity(final char endChar) {
    first = false;
    builder.append(endChar);
    return this;
  }
}
