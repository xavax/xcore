//
// Copyright 2011 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//
package com.xavax.json;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;

import com.xavax.util.CollectionFactory;

/**
 * JSON represents a JSON object as nested hashmaps.
 */
public class JSON extends HashMap<String, Object> {
  private static final char RIGHT_BRACE = '}';
  private static final char LEFT_BRACE = '{';
  private static final char COLON = ':';
  private static final char COMMA = ',';
  public final static long serialVersionUID = 0;

  /**
   * Construct an empty JSON.
   */
  public JSON() {
    // Nothing to see here.
  }

  /**
   * Construct a JSON from the data in a map.
   *
   * @param map  a map to populate the new JSON
   */
  public JSON(final Map<String, Object> map) {
    for ( final Map.Entry<String, Object> entry : map.entrySet()) {
      super.put(entry.getKey(), entry.getValue());
    }
  }

  /**
   * Construct a JSON that is a copy of another JSON.
   *
   * @param json  the JSON to copy.
   */
  public JSON(final JSON json) {
    for ( final Map.Entry<String, Object> entry : json.entrySet() ) {
      Object object = entry.getValue();
      if ( object instanceof JSON ) {
	object = new JSON((JSON) object);
      }
      else if ( object instanceof JSONArray ) {
	object = new JSONArray((JSONArray) object);
      }
      super.put(entry.getKey(), object);
    }
  }

  /**
   * Create a new JSON.
   * 
   * @return a new JSON.
   */
  public JSON create() {
    return new JSON();
  }

  /**
   * Put a key:value pair into the JSON.
   *
   * @param key    the key for the value.
   * @param value  the value to be inserted.
   * @return this JSON.
   */
  public JSON put(final String key, final Object value) {
    super.put(key, value);
    return this;
  }

  /**
   * Put a key:value pair into the JSON.
   *
   * @param key    the key for the value.
   * @param value  the value to be inserted.
   * @return this JSON.
   */
  public JSON put(final String key, final Collection<Object> value) {
    super.put(key, new JSONArray(value));
    return this;
  }

  /**
   * Put a key:value pair into the JSON.
   *
   * @param key    the key for the value.
   * @param value  the value to be inserted.
   * @return this JSON.
   */
  public JSON put(final String key, final Object... value) {
    super.put(key, new JSONArray(value));
    return this;
  }

  /**
   * Put a key:value pair into the JSON.
   *
   * @param key    the key for the value.
   * @param value  the value to be inserted.
   * @return this JSON.
   */
  public JSON put(final String key, final Map<String, Object> value) {
    final JSON json = new JSON(value);
    super.put(key, json);
    return this;
  }

  /**
   * Put a key:value pair into the JSON.
   *
   * @param key   the key for the value.
   * @param json  the value to be inserted.
   * @return this JSON.
   */
  public JSON put(final String key, final JSON json) {
    super.put(key, json);
    return this;
  }

  /**
   * Put a key:value pair into the JSON.
   *
   * @param key    the key for the value.
   * @param array  the value to be inserted.
   * @return this JSON.
   */
  public JSON put(final String key, final JSONArray array) {
      super.put(key, array);
      return this;
  }
  
  /**
   * Returns the value specified by a path. This can be a scalar value, an
   * array, or a JSON. If the path traverses through an array, the index must be
   * provided by the variable list params. If the list of params is insufficient
   * (i.e. we traverse through 4 arrays but only 3 indexes are provided) the
   * index defaults to zero.
   * 
   * @param path
   *          specifies a path into a JSON such as person.address.city.
   * @param params
   *          an array of integers to be used to index into arrays.
   * @return the value specified by path, or null if the path is not valid.
   */
  public Object get(final JSONPath path, final int... params) {
    int count = 0;
    int level = 1;
    final int levels = path.size();
    Object result = null;
    JSON map = this;
    for ( final String s : path ) {
      result = map.get(s);
      if ( result instanceof JSON ) {
	map = (JSON) result;
      }
      else if ( result instanceof JSONArray ) {
	if ( level < levels ) {
	  final int index = params.length > count ? params[count++] : 0;
	  result = ((JSONArray) result).get(index);
	  if ( result instanceof JSON ) {
	    map = (JSON) result;
	  }
	  else {
	    break;
	  }
	}
	else if ( params.length > count ) {
	  result = ((JSONArray) result).get(params[count]);
	  break;
	}
      }
      else {
	break;
      }
      ++level;
    }
    return result;
  }

  /**
   * Returns the object specified by key. If key is a JSONPath, call get with
   * an index of zero to force it to call the method above. This handles the
   * special case where the user calls get with a JSONPaqth but passes no
   * index parameters. The compiler cannot distinguish that from a call to
   * get(Object).
   */
  public Object get(final Object key) {
    return key instanceof JSONPath ? get((JSONPath) key, 0) : super.get(key);
  }

  /**
   * Returns the array field with the specified name.
   *
   * @param key  the name of the array field to get.
   * @return the array field with the specified name.
   */
  public JSONArray getArray(final String key) {
    final Object object = get(key);
    return object != null && object instanceof JSONArray ? (JSONArray) object : null;
  }

  /**
   * Returns the array field at the specified path.
   *
   * @param path    the path of the array field.
   * @param params  the parameters to use while traversing the path.
   * @return the array field at the specified path.
   */
  public JSONArray getArray(final JSONPath path, final int... params) {
    final Object object = get(path, params);
    return object != null && object instanceof JSONArray ? (JSONArray) object : null;
  }

  /**
   * Convert a field to a Boolean if necessary.
   *
   * @param object  the value to convert.
   * @return a value converted to a Boolean.
   */
  private Boolean getBoolean(final Object object) {
    Boolean result = null;
    if ( object instanceof Boolean ) {
      result = (Boolean) object;
    }
    else if ( object instanceof String ) {
      result = Boolean.valueOf((String) object);
    }
    return result;
  }

  /**
   * Return the boolean field with the specified key.
   *
   * @param key  the name of the field.
   * @return a boolean field.
   */
  public Boolean getBoolean(final String key) {
    return getBoolean(get(key));
  }

  /**
   * Get the boolean field with the specified name. If the field is missing
   * or null, return the specified default value.
   *
   * @param key  the name of the field.
   * @param defaultValue  the value to return if the field is null.
   * @return the boolean field with the specified name.
   */
  public boolean getBoolean(final String key, final boolean defaultValue) {
    final Boolean flag = getBoolean(key);
    return flag == null ? defaultValue : flag.booleanValue();
  }

  /**
   * Get the boolean field at the specified path.
   *
   * @param path    the path of the specified field.
   * @param params  the parameters to use when traversing the path.
   * @return the boolean field at the specified path.
   */
  public Boolean getBoolean(final JSONPath path, final int... params) {
    return getBoolean(get(path, params));
  }

  /**
   * Convert a value to a Double if necessary.
   *
   * @param value  the value to be converted.
   * @return a value converted to a Double.
   */
  private Double getDouble(final Object value) {
    Double result = null;
    if ( value instanceof Double ) {
      result = (Double) value;
    }
    else if ( value instanceof String ) {
      result = Double.valueOf((String) value);
    }
    return result;
  }

  /**
   * Get the double field with the specified name.
   *
   * @param key  the name of the field.
   * @return the double field with the specified name.
   */
  public Double getDouble(final String key) {
    return getDouble(get(key));
  }

  /**
   * Get the double field with the specified name. If the field is missing
   * or null, return the specified default value.
   *
   * @param key  the name of the field.
   * @param defaultValue  the value to return if the field is null.
   * @return the double field with the specified name.
   */
  public double getDouble(final String key, final double defaultValue) {
    final Double value = getDouble(key);
    return value == null ? defaultValue : value.doubleValue();
  }

  /**
   * Get the double field at the specified path.
   *
   * @param path    the path of the specified field.
   * @param params  the parameters to use when traversing the path.
   * @return the double field at the specified path.
   */
  public Double getDouble(final JSONPath path, final int... params) {
    return getDouble(get(path, params));
  }

  /**
   * Convert a value to a Long if necessary.
   *
   * @param object  the object to convert.
   * @return a value converted to a long.
   */
  private Long getLong(final Object object) {
    Long result = null;
    if ( object instanceof Long ) {
      result = (Long) object;
    }
    else if ( object instanceof String ) {
      result = Long.valueOf((String) object);
    }
    return result;
  }

  /**
   * Get the long field with the specified name.
   *
   * @param key  the name of the field.
   * @return the long field with the specified name.
   */
  public Long getLong(final String key) {
    return getLong(get(key));
  }

  /**
   * Get the long field with the specified name. If the field is missing
   * or null, return the specified default value.
   *
   * @param key  the name of the field.
   * @param defaultValue  the value to return if the field is null.
   * @return the long field with the specified name.
   */
  public long getLong(final String key, final long defaultValue) {
    final Long value = getLong(key);
    return value == null ? defaultValue : value.longValue();
  }

  /**
   * Get the long field at the specified path.
   *
   * @param path    the path of the specified field.
   * @param params  the parameters to use when traversing the path.
   * @return the long field at the specified path.
   */
  public Long getLong(final JSONPath path, final int... params) {
    return getLong(get(path, params));
  }

  /**
   * Convert an object to a string if necessary.
   *
   * @param object  the object to convert.
   * @return the object converted to a string.
   */
  private String getString(final Object object) {
    String result = null;
    if ( object instanceof String ) {
      result = (String) object;
    }
    else if ( object != null ) {
      result = object.toString();
    }
    return result;
  }

  /**
   * Get the string field with the specified name.
   *
   * @param key  the name of the field.
   * @return the string field with the specified name.
   */
  public String getString(final String key) {
    return getString(get(key));
  }

  /**
   * Get the string field with the specified name. If the field is missing
   * or null, return the specified default value.
   *
   * @param key  the name of the field.
   * @param defaultValue  the value to return if the field is null.
   * @return the string field with the specified name.
   */
  public String getString(final String key, final String defaultValue) {
    final String string = getString(key);
    return string == null ? defaultValue : string;
  }

  /**
   * Get the string field at the specified path.
   *
   * @param path    the path of the specified field.
   * @param params  the parameters to use when traversing the path.
   * @return the string field at the specified path.
   */
  public String getString(final JSONPath path, final int... params) {
    return getString(get(path, params));
  }

  /**
   * Get the embedded JSON object with the specified name.
   *
   * @param key  the name of the object.
   * @return the embedded JSON object with the specified name.
   */
  public JSON getJSON(final String key) {
    final Object object = get(key);
    return object != null && object instanceof JSON ? (JSON) object : null;
  }

  /**
   * Get an embedded JSON object at the specified path.
   *
   * @param path    the path of the object.
   * @param params  the parameters to use while traversing the path.
   * @return the embedded JSON object at the specified path.
   */
  public JSON getJSON(final JSONPath path, final int... params) {
    final Object object = get(path, params);
    return object != null && object instanceof JSON ? (JSON) object : null;
  }

  /**
   * Return a hash map that is a flattened version of this JSON.
   * Any arrays or objects will be represented as a string in
   * JSON format.
   *
   * @return a hash map representing a flattened JSON.
   */
  public Map<String, String> flatten() {
    final Map<String, String> result = CollectionFactory.hashMap();
    for ( final Map.Entry<String, Object> entry : entrySet()) {
      final Object value = entry.getValue();
      result.put(entry.getKey(), value == null ? null : value.toString());
    }
    return result;
  }

  /**
   * Merge a JSON into this JSON.
   *
   * @param json  the JSON to merge into this JSON.
   * @return this JSON.
   */
  public JSON merge(final JSON json) {
    for ( final Map.Entry<String, Object> entry : json.entrySet()) {
      final String key = entry.getKey();
      Object object = entry.getValue();
      if ( object instanceof JSON ) {
	final Object target = get(key);
	if ( target instanceof JSON ) {
	  ((JSON) target).merge((JSON) object);
	}
	else {
	  object = new JSON((JSON) object);
	  super.put(key, object);
	}
      }
      else if ( object instanceof JSONArray ) {
	final Object target = get(key);
	if ( target instanceof JSONArray ) {
	  ((JSONArray) target).addAll((JSONArray) object);
	}
	else {
	  object = new JSONArray((JSONArray) object);
	  super.put(key, object);
	}
      }
      else {
	super.put(key, object);
      }
    }
    return this;
  }

  /**
   * Returns a compact string representation of this JSON.
   *
   * @return a compact string representation of this JSON.
   */
  @Override
  public String toString() {
    return toString(Format.COMPACT);
  }

  /**
   * Return a string representation of this JSON formatted according
   * to the specified format.
   *
   * @param format
   * @return a string representation of this JSON.
   */
  public String toString(final Format format) {
    final StringBuilder builder = new StringBuilder();
    // sb.append("{").append(format.postOpenBrace);
    toString(builder, format, 0);
    // sb.append(format.preCloseBrace).append("}");
    return builder.toString();
  }

  /**
   * Create a string representation of this JSON formatted according
   * to the specified format using an existing string builder.
   *
   * @param builder      the string builder.
   * @param format       the format.
   * @param indentLevel  the initial indentation level.
   */
  public void toString(final StringBuilder builder, final Format format,
                       final int indentLevel) {
    int level = indentLevel;
    final String indentation = format.indentation(level++);
    final String innerIndentation = format.indentation(level);
    builder.append(format.preOpenBrace).append(LEFT_BRACE);
    if ( entrySet().size() > 0 ) {
      builder.append(format.postOpenBrace);
      boolean first = true;
      for ( final Map.Entry<String, Object> entry : entrySet()) {
	if ( first ) {
	  first = false;
	}
	else {
	  builder.append(format.preComma).append(COMMA).append(format.postComma);
	}
	builder.append(innerIndentation);
	builder.append(format.quoteIdentifier).append(entry.getKey())
	    .append(format.quoteIdentifier);
	builder.append(format.preColon).append(COLON).append(format.postColon);
	final Object value = entry.getValue();
	appendValue(builder, format, level, value);
      }
      builder.append(format.preCloseBrace).append(indentation);
    }
    builder.append(RIGHT_BRACE).append(format.postCloseBrace);
  }

  /**
   * Append a JSON field value to the builder.
   *
   * @param builder  the string builder used for output.
   * @param format   the format for formatting the JSON.
   * @param level    the indentation level.
   * @param value    the value to append.
   */
  public static void appendValue(final StringBuilder builder, final Format format,
                                 final int level, final Object value) {
    if ( value instanceof JSON ) {
      ((JSON) value).toString(builder, format, level);
    }
    else if ( value instanceof JSONArray ) {
      ((JSONArray) value).toString(builder, format, level);
    }
    else if ( value instanceof String ) {
      final String string = StringEscapeUtils.escapeEcmaScript((String) value);
      builder.append(format.quoteString).append(string).append(format.quoteString);
    }
    else {
      builder.append(value);
    }
  }

  /**
   * Return this JSON as an array of bytes.
   *
   * @return this JSON as an array of bytes.
   */
  public byte[] getBytes() {
    return toString().getBytes();
  }

  /**
   * Format describes how a JSON should be formatted when converted to a string.
   */
  public static class Format {
    private final static String TABS = "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t";

    // Single line, no whitespace.
    public final static Format COMPACT =
	new Format(false, "", "'", "", "", "", "", "", "", "", "");
    // Single line, some whitespace.
    public final static Format EXPANDED =
	new Format(false, "", "'", "", "", "", " ", " ", " ", " ", "");
    // Multiple lines with whitespace.
    public final static Format VERBOSE =
	new Format(true, "", "'", "", "", "", "\n", " ", "\n", "\n", "");

    public final boolean indent;
    public final String quoteIdentifier;
    public final String quoteString;
    public final String preColon;
    public final String preComma;
    public final String preOpenBrace;
    public final String preCloseBrace;
    public final String postColon;
    public final String postComma;
    public final String postOpenBrace;
    public final String postCloseBrace;

    /**
     * Construct a Format.
     *
     * @param indent		true if output should be indented.
     * @param quoteIdentifier	string used to quote identifiers.
     * @param quoteString	string used to quote strings.
     * @param preColon		string that precedes a colon.
     * @param preComma		string that precedes a comma.
     * @param preOpenBrace	string that precedes an opening brace.
     * @param preCloseBrace	string that precedes a closing brace.
     * @param postColon		string that follows a colon.
     * @param postComma		string that follows a comma.
     * @param postOpenBrace	string that follows an opening brace.
     * @param postCloseBrace	string that follows a closing brace.
     */
    @SuppressWarnings("PMD.ExcessiveParameterList")
    public Format(final boolean indent, final String quoteIdentifier, final String quoteString,
                  final String preColon, final String preComma, final String preOpenBrace,
                  final String preCloseBrace, final String postColon, final String postComma,
                  final String postOpenBrace, final String postCloseBrace) {
      this.indent = indent;
      this.quoteIdentifier = quoteIdentifier;
      this.quoteString = quoteString;
      this.preColon = preColon;
      this.preComma = preComma;
      this.preOpenBrace = preOpenBrace;
      this.preCloseBrace = preCloseBrace;
      this.postColon = postColon;
      this.postComma = postComma;
      this.postOpenBrace = postOpenBrace;
      this.postCloseBrace = postCloseBrace;
    }

    /**
     * Returns a string containing the specified number of tabs to use
     * for indentation.
     *
     * @param level  the indentation level.
     * @return a string of tabs for indentation.
     */
    public String indentation(final int level) {
      return indent ? TABS.substring(0, level) : "";
    }
  }
}
