//
// Copyright 2015 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//
package com.xavax.util;

import static com.xavax.util.Constants.*;

import java.util.Collection;
import java.util.Map;

/**
 * Joiner supports the efficient implementation of toString methods
 * for complex objects.
 */
@SuppressWarnings("PMD.ExcessivePublicCount")
public class Joiner {
  private final static int DEFAULT_INITIAL_CAPACITY = 64;
  private final static int MAX_LEVEL = Long.SIZE - 1;

  private boolean quoteStrings;
  private boolean reusable;
  private boolean skipNulls;
  private boolean withFieldNames = true;
  private int depth;
  private int maxDepth;
  private String defaultSeparator = COMMA_SEPARATOR;
  private String itemSeparator = COMMA_SEPARATOR;
  private String nameSeparator = COLON_SEPARATOR;
  private String nullIndicator = NULL_INDICATOR;
  private String prefix = EMPTY_STRING;
  private String suffix = EMPTY_STRING;
  private final Tracker tracker = new Tracker();

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
    depth = 1;
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
  public final Joiner withNullIndicator(final String nullIndicator) {
    this.nullIndicator = nullIndicator == null ? EMPTY_STRING : nullIndicator;
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
  public final Joiner withSeparator(final String separator) {
    defaultSeparator = separator == null ? EMPTY_STRING : separator;
    tracker.setSeparator(defaultSeparator);
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
   * @return this Joiner.
   */
  public final Joiner withItemSeparator(final String separator) {
    this.itemSeparator = separator == null ? EMPTY_STRING : separator;
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
   * @return this Joiner.
   */
  public final Joiner withFieldNameSeparator(final String separator) {
    this.nameSeparator = separator == null ? COLON_SEPARATOR : separator;
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
   * @param withFieldNames  the new value of the withFieldNames property
   * @return this joiner.
   */
  public Joiner withFieldNames(final boolean withFieldNames) {
    this.withFieldNames = withFieldNames;
    return this;
  }

  /**
   * Returns true if field name are enabled.
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
    this.prefix = prefix == null ? EMPTY_STRING : prefix;
    return this;
  }

  /**
   * Returns the prefix.
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
   * @return  this Joiner.
   */
  public final Joiner withSuffix(final String suffix) {
    this.suffix = suffix == null ? EMPTY_STRING : suffix;
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
   * Join a variable length array of objects.
   *
   * @param objects  the array of objects to be joined.
   * @return this Joiner.
   */
  public String join(final Object... objects) {
    builder.append(prefix);
    tracker.clearFlag();;
    for ( final Object object : objects ) {
      if ( check(null, object) ) {
	tracker.addSeparator();
	append(object);
	tracker.setFlag();
      }
    }
    builder.append(suffix);
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
      if ( quoteStrings ) {
	builder.append('"')
	       .append(string)
	       .append('"');
      }
      else {
	builder.append(string);
      }
      tracker.setFlag();
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
      object.join(this);
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
	appendItem(null, object);
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
	appendItem(null, object);
      }
      endCollection();
    }
    return this;
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
      beginCollection(name);
      for ( final Map.Entry<?,?> entry : map.entrySet() ) {
	final Object key = entry.getKey();
	final Object value = entry.getValue();
	final String entryName = key == null ? nullIndicator : key.toString();
	if ( check(entryName, value) ) {
	  appendItem(entryName, value);
	}
      }
      endCollection();
    }
    return this;
  }

  /**
   * Append an item from an array or collection.
   *
   * @param name    the name of the item.
   * @param object  the item to append.
   */
  public void appendItem(final String name, final Object object) {
    if ( check(name, object) ) {
      tracker.addSeparator();
      beginObject(name);
      append(object);
      endObject();
      tracker.setFlag();
    }
  }

  /**
   * Begin joining an array.
   *
   * @param name  the name of this field.
   * @return this Joiner.
   */
  public Joiner beginArray(final String name) {
    beginField(name);
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
   * @param name  the name of this field.
   * @return this Joiner.
   */
  public Joiner beginCollection(final String name) {
    beginField(name);
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
   * @param name  the name of this field.
   * @return this Joiner.
   */
  public Joiner beginObject(final String name) {
    beginField(name);
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
    return this.nullIndicator; 
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
    if ( withFieldNames && name != null ) {
      builder.append(name)
             .append(nameSeparator);
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
    tracker.push(itemSeparator);
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
      if ( !skipNulls ) {
	tracker.addSeparator();
	beginField(name);
	builder.append(nullIndicator);
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

  /**
   * Tracker keeps track of the levels of nested items and
   * whether we currently need a separator at each level.
   */
  class Tracker {

    private int level;
    private long flags;
    private String[] stack = new String[MAX_LEVEL + 1];

    /**
     * Construct a Tracker with the specified separator.
     *
     * @param separator  the string to use as a separator.
     */
    public Tracker() {
      stack[0] = defaultSeparator;
    }

    /**
     * Increment the level and push a separator onto the stack.
     *
     * @param separator  the new separator.
     */
    public void push(final String separator) {
      if ( level < MAX_LEVEL ) {
	stack[++level] = separator == null ? defaultSeparator : separator;
	clearFlag();
      }
    }

    /**
     * Decrement the level.
     */
    public void pop() {
      if ( level > 0 ) {
	--level;
	setFlag();
      }
    }

    /**
     * Add a separator to the output if needed.
     */
    public void addSeparator() {
      if ( isSet() ) {
	builder.append(stack[level]);
	clearFlag();
      }
    }

    /**
     * Sets the separator for the current level.
     *
     * @param separator  the new separator.
     */
    public void setSeparator(final String separator) {
       stack[level] = separator;
    }

    /**
     * Set the flag for this level to indicate an item was
     * added and the output now needs a separator.
     */
    public void setFlag() {
      flags |= 1 << level;
    }

    /**
     * Clear the flag for this level to indicate a separator
     * is not needed.
     */
    public void clearFlag() {
      flags &= ~(1 << level);
    }

    /**
     * Returns the flag for this level.
     * @return the flag for this level.
     */
    public boolean isSet() {
      return (flags & (1 << level)) != 0;
    }

    /**
     * Returns the current level. This is only for testing.
     * @return the current level.
     */
    int getLevel() {
      return level;
    }

    /**
     * Sets the level. This is only for testing.
     *
     * @param level  the new level.
     */
    void setLevel(final int level) {
      this.level = level;
    }
  }
}
