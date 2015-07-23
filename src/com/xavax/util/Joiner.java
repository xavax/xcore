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
   * Append a string.
   *
   * @param string  the string to append.
   * @return this Joiner.
   */
  public Joiner append(final String string) {
    beginField();
    if ( string == null ) {
      if ( !skipNulls ) {
	builder.append(nullIndicator);
      }
    }
    else {
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
    if ( object == null ) {
      if ( !skipNulls ) {
	builder.append(nullIndicator);
      }
    }
    else {
      beginObject();
      if ( object instanceof Joinable ) {
	((Joinable) object).join(this);
      }
      else {
	builder.append(object.toString());      
      }
      endObject();
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
   * Begin joining a field. Append the separator if this is
   * the first field.
   *
   * @return this Joiner.
   */
  private Joiner beginField() {
    if ( first ) {
      first = false;
    }
    else {
      builder.append(separator);
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
