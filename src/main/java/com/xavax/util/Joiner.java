//
// Copyright 2015 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//
package com.xavax.util;

import static com.xavax.util.Constants.*;

import java.util.Collection;
import java.util.Map;

import static com.xavax.util.JoinerFormats.DEBUG_FORMAT;

/**
 * Joiner supports the efficient implementation of toString methods
 * for complex objects.
 */
@SuppressWarnings({
  "PMD.AvoidUsingShortType",
  "PMD.CyclomaticComplexity",
  "PMD.GodClass",
  "PMD.TooManyMethods"
})
public class Joiner {

  private boolean reusable;
  private int depth;
  private int maxDepth;
  private JoinerFormat format;

  private final Tracker tracker;

  // Joiner is a transient object so this warning is not relevant.
  // If this joiner is reusable, the buffer length is set to zero
  // and trimmed to size after calling toString.
  @SuppressWarnings("PMD.AvoidStringBufferField")
  private final StringBuilder builder;

  /**
   * Construct a joiner with the default initial capacity.
   */
  public Joiner() {
    this(DEBUG_FORMAT);
  }

  /**
   * Construct a joiner with the specified initial capacity.
   *
   * @param initialCapacity  the initial capacity of the Joiner.
   */
  public Joiner(final int initialCapacity) {
    this(initialCapacity, DEBUG_FORMAT);
  }

  /**
   * Construct a joiner with the specified format.
   *
   * @param format  the joiner format.
   */
  public Joiner(final JoinerFormat format) {
    this(format.getDefaultCapacity(), format);
  }

  /**
   * Construct a joiner with the specified initial capacity and format.
   *
   * @param initialCapacity the initial capacity.
   * @param format  the joiner format.
   */
  public Joiner(final int initialCapacity, final JoinerFormat format) {
    this.format = format;
    tracker = new Tracker(this);
    builder = new StringBuilder(initialCapacity);
    depth = 1;
  }

  /**
   * Create a joiner with the default initial capacity and format.
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
   * Create a joiner with the specified format.
   *
   * @param format  the joiner format.
   * @return a new Joiner.
   */
  public static Joiner create(final JoinerFormat format) {
    return new Joiner(format);
  }

  /**
   * Create a joiner with the specified initial capacity and format.
   *
   * @param initialCapacity the initial capacity.
   * @param format  the joiner format.
   * @return a new Joiner.
   */
  public static Joiner create(final int initialCapacity, final JoinerFormat format) {
    return new Joiner(initialCapacity, format);
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
   * Sets the format for this joiner.
   *
   * @param format  the new format.
   * @return this Joiner.
   */
  public Joiner withFormat(final JoinerFormat format) {
    this.format = format;
    return this;
  }

  /**
   * Returns the format for this joiner.
   *
   * @return the format for this joiner.
   */
  public JoinerFormat getFormat() {
    return format;
  }
  /**
   * Sets the skipNulls flag to true.
   *
   * @return this Joiner.
   */
  public Joiner skipNulls() {
    format.withSkipNulls(true);
    return this;
  }

  /**
   * Sets the null indicator to the specified string.
   *
   * @param nullIndicator  the new null indicator.
   * @return this Joiner.
   */
  public final Joiner withNullIndicator(final String nullIndicator) {
    this.format.withNullIndicator(nullIndicator);
    return this;
  }

  /**
   * Sets the separator to the specified string.
   *
   * @param separator  the new separator.
   * @return this Joiner.
   */
  public final Joiner withSeparator(final String separator) {
    format.withSeparator(separator);
    tracker.setSeparator(separator);
    return this;
  }

  /**
   * Set the max depth for joining nested joinable objects.
   * The maximum level is specified by MAX_LEVEL.
   *
   * @param maxDepth  the maximum depth.
   * @return this joiner.
   */
  public Joiner withMaxDepth(final int maxDepth) {
    this.maxDepth = maxDepth < 0 ? 0 : maxDepth;
    return this;
  }

  /**
   * Returns the maximum depth.
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
   * @return  this Joiner.
   */
  public final Joiner withPrefix(final String prefix) {
    format.withPrefix(prefix);
    return this;
  }

  /**
   * Sets the suffix to the specified string. The suffix will
   * be appended to the final result of joining. This is only
   * used by the join method.
   *
   * @param suffix  the suffix string.
   * @return  this Joiner.
   */
  public final Joiner withSuffix(final String suffix) {
    format.withSuffix(suffix);
    return this;
  }

  /**
   * Join a variable length array of objects.
   *
   * @param objects  the array of objects to be joined.
   * @return this Joiner.
   */
  public String join(final Object... objects) {
    builder.append(format.getPrefix());
    tracker.clearFlag();
    for ( final Object object : objects ) {
      if ( check(null, object) ) {
	tracker.addSeparator();
	append(object);
	tracker.setFlag();
      }
    }
    builder.append(format.getSuffix());
    return toString();
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
    tracker.setFlag();
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
    tracker.setFlag();
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
    tracker.setFlag();
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
    tracker.setFlag();
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
    tracker.setFlag();
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
    tracker.setFlag();
    return this;
  }

  /**
   * Append a Number to the output.
   *
   * @param value  the value to append to the output.
   * @return this joiner.
   */
  public Joiner append(final Number value) {
    return append(null, value);
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
    if ( check(name, field) ) {
      beginField(name);
      builder.append(field);
      tracker.setFlag();
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
    if ( check(name, string) ) {
      beginField(name);
      appendString(string);
      tracker.setFlag();
    }
    return this;
  }

  /**
   * Append a string, optionally enclosed in quotes, and processed
   * by the list of string processors.
   *
   * @param input  the input string.
   * @return this Joiner.
   */
  public Joiner appendString(final String input) {
    if ( format.hasQuotedStrings() ) {
	builder.append(format.getOpenQuoteCharacter())
	       .append(process(input))
	       .append(format.getCloseQuoteCharacter());
    }
    else {
	builder.append(process(input));
    }
    return this;
  }

  /**
   * Append an object. If the object is Joinable,
   * do a nested join.
   *
   * @param object  the object to be joined.
   * @return this Joiner.
   */
  public Joiner append(final Object object) {
    if ( check(null, object) ) {
      if ( object instanceof Joinable ) {
	nest((Joinable) object);
      }
      else {
	builder.append(object.toString());
	tracker.clearFlag();
      }
    }
    return this;
  }

  /**
   * Attempt to join a nested object.
   *
   * @param object  the nested object to be joined.
   * @return this joiner.
   */
  public Joiner nest(final Joinable object) {
    if ( maxDepth == 0 || depth <= maxDepth ) {
      ++depth;
      tracker.push(null);
      beginObject(null);
      object.join(this);
      endObject();
      tracker.pop();
      --depth;
    }
    else {
      builder.append(ELLIPSIS);
    }
    return this;
  }

  /**
   * Append an object.
   *
   * @param name    the field name.
   * @param object  the object to be joined.
   * @return this Joiner.
   */
  public Joiner append(final String name, final Object object) {
    if ( check(name, object) ) {
      beginObject(name);
      append(object);
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
  public Joiner append(final Object...objects) {
    return append(null, objects);
  }

  /**
   * Append an array of objects.
   *
   * @param name  the field name.
   * @param objects  the array of objects to be joined.
   * @return this Joiner.
   */
  public Joiner append(final String name, final Object...objects) {
    if ( check(name, objects) ) {
      beginArray(name);
      for ( final Object object : objects ) {
	appendItem(object);
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
    return append(null, collection);
  }

  /**
   * Append a collection.
   *
   * @param name  the name of this field.
   * @param collection  the collection to be joined.
   * @return this Joiner.
   */
  public Joiner append(final String name, final Collection<?> collection) {
    if ( check(name, collection) ) {
      beginCollection(name);
      for ( final Object object : collection ) {
	appendItem(object);
      }
      endCollection();
    }
    return this;
  }

  /**
   * Append an item from an array or collection.
   *
   * @param object  the item to append.
   */
  public void appendItem(final Object object) {
    if ( check(null, object) ) {
      tracker.addSeparator();
      if ( object instanceof String ) {
	appendString((String) object);
      }
      else if ( object instanceof Joinable ) {
	nest((Joinable) object);
      }
      else {
	builder.append(object.toString());
      }
      tracker.setFlag();
    }
  }

  /**
   * Append a map.
   *
   * @param map  the map to be joined.
   * @return this Joiner.
   */
  public Joiner append(final Map<?,?> map) {
    return append(null, map);
  }

  /**
   * Append a map.
   *
   * @param name  the name of this field.
   * @param map  the map to be joined.
   * @return this Joiner.
   */
  public Joiner append(final String name, final Map<?,?> map) {
    if ( check(name, map) ) {
      beginMap(name);
      for ( final Map.Entry<?,?> entry : map.entrySet() ) {
	tracker.addSeparator();
	appendKey(entry.getKey());
	builder.append(format.getMapKeySeparator());
	appendValue(entry.getValue());
	tracker.setFlag();
      }
      endMap();
    }
    return this;
  }

  /**
   * Append the map entry key.
   *
   * @param key  the key.
   */
  void appendKey(final Object key) {
    if ( key == null ) {
      builder.append(format.getNullIndicator());
    }
    else if ( key instanceof String ) {
      appendString((String) key);
    }
    else {
      builder.append(key.toString());
    }
  }

  /**
   * Append a map entry value.
   *
   * @param object  the value to append.
   */
  public void appendValue(final Object object) {
    if ( object == null ) {
      builder.append(format.getNullIndicator());
    }
    else if ( object instanceof String ) {
      appendString((String) object);
    }
    else if ( object instanceof Joinable ) {
      nest((Joinable) object);
    }
    else {
      builder.append(object.toString());
    }
  }

  /**
   * Process a string using each string processor in the processors list.
   *
   * @param input  the input string.
   * @return a processed string.
   */
  String process(final String input) {
    String output = input;
    for ( final StringProcessor processor : format.getProcessors() ) {
      output = processor.process(format, output, null);
    }
    return output;
  }

  /**
   * Begin joining an array.
   *
   * @param name  the name of this field.
   * @return this Joiner.
   */
  public Joiner beginArray(final String name) {
    beginField(name);
    return beginEntity(format.getArrayOpenCharacter());
  }

  /**
   * Finish joining an array.
   *
   * @return this Joiner.
   */
  public Joiner endArray() {
    return endEntity(format.getArrayCloseCharacter());
  }

  /**
   * Begin joining an collection.
   *
   * @param name  the name of this field.
   * @return this Joiner.
   */
  public Joiner beginCollection(final String name) {
    beginField(name);
    return beginEntity(format.getListOpenCharacter());
  }

  /**
   * Finish joining an collection.
   *
   * @return this Joiner.
   */
  public Joiner endCollection() {
    return endEntity(format.getListCloseCharacter());
  }

  /**
   * Begin joining a map.
   *
   * @param name  the name of this field.
   * @return this Joiner.
   */
  public Joiner beginMap(final String name) {
    beginField(name);
    return beginEntity(format.getMapOpenCharacter());
  }

  /**
   * Finish joining a map.
   *
   * @return this Joiner.
   */
  public Joiner endMap() {
    return endEntity(format.getMapCloseCharacter());
  }

  /**
   * Begin joining an object.
   *
   * @param name  the name of this field.
   * @return this Joiner.
   */
  public Joiner beginObject(final String name) {
    beginField(name);
    return beginEntity(format.getObjectOpenCharacter());
  }

  /**
   * Finish joining an object.
   *
   * @return this Joiner.
   */
  public Joiner endObject() {
    return endEntity(format.getObjectCloseCharacter());
  }

  /**
   * Append a character to the output.
   *
   * @param character  the character to append.
   * @return this Joiner.
   */
  public Joiner appendRaw(final char character) {
    builder.append(character);
    return this;
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
    return format.getNullIndicator(); 
  }

  /**
   * Begin joining a field. Append the list separator if
   * this is the first field.
   *
   * @param name  the field name (omit if null).
   * @return this Joiner.
   */
  private Joiner beginField(final String name) {
    tracker.addSeparator();
    if ( format.hasFieldNames() && name != null ) {
      final boolean quotedFieldNames = format.hasQuotedFieldNames();
      if ( quotedFieldNames ) {
	builder.append(format.getOpenQuoteCharacter());
      }
      builder.append(name);
      if ( quotedFieldNames ) {
	builder.append(format.getCloseQuoteCharacter());
      }
      builder.append(format.getFieldNameSeparator());
      tracker.clearFlag();
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
    tracker.addSeparator();
    builder.append(beginChar);
    tracker.push(format.getItemSeparator());
    return this;
  }

  /**
   * End joining an object, array, or collection.
   * @param endChar
   * @return
   */
  private Joiner endEntity(final char endChar) {
    builder.append(endChar);
    tracker.pop();
    return this;
  }

  /**
   * Append the null indicator if we are not skipping nulls.
   * Add a leading separator if needed.
   */
  private boolean check(final String name, final Object object) {
    boolean result = true;
    if ( object == null ) {
      if ( !format.hasSkipNulls() ) {
	tracker.addSeparator();
	beginField(name);
	builder.append(format.getNullIndicator());
	tracker.setFlag();
      }
      result = false;
    }
    return result;
  }

  /**
   * Returns the Tracker. This is only for testing.
   * @return the Tracker.
   */
  Tracker getTracker() {
    return tracker;
  }
}
