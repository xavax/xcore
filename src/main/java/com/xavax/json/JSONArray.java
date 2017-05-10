//
// Copyright 2011 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//
package com.xavax.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.xavax.json.JSON.Format;
import com.xavax.util.CollectionFactory;

import static com.xavax.util.Constants.*;

/**
 * JSONArray represents an array field in a JSON.
 */
public final class JSONArray extends ArrayList<Object> {
  public final static long serialVersionUID = 0;

  /**
   * Construct a JSONArray from a collection.
   *
   * @param collection  contains the initial array elements.
   */
  public JSONArray(final Collection<Object> collection) {
    super(collection);
  }

  /**
   * Construct a JSONArray from an array of items.
   *
   * @param items  the initial array elements.
   */
  public JSONArray(final Object... items) {
    for ( final Object item : items ) {
      add(item);
    }
  }

  /**
   * Construct a JSONArray as a copy of another JSONArray.
   *
   * @param array  the array to copy.
   */
  public JSONArray(final JSONArray array) {
    this.addAll(array);
  }

  /**
   * Create a new JSONArray from a collection and add the new
   * array to this array.
   *
   * @param collection  the collection of items to add.
   * @return true if this array was modified.
   */
  public boolean add(final Collection<Object> collection) {
    final JSONArray array = new JSONArray(collection);
    return super.add((Object) array);
  }

  /**
   * Create a new JSON from a map and add it to this array.
   *
   * @param map  the map of items to add.
   * @return true if this array was modified.
   */
  public boolean add(final Map<String, ? extends Object> map) {
    final JSON json = new JSON(map);
    return super.add((Object) json);
  }

  /**
   * Add a JSON to this array.
   *
   * @param json  the JSON to add.
   * @return true if this array was modified.
   */
  public boolean add(final JSON json) {
    return super.add((Object) json);
  }

  /**
   * Add all elements of a JSONArray to this array.
   *
   * @param array  the array of elements to add.
   * @return true if this array was modified.
   */
  public boolean addAll(final JSONArray array) {
    boolean result = false;
    for ( Object object : array ) {
      if ( object instanceof JSON ) {
	object = new JSON((JSON) object);
      }
      else if ( object instanceof JSONArray ) {
	object = new JSONArray((JSONArray) object);
      }
      add(object);
      result = true;
    }
    return result;
  }

  /**
   * Return a list that is a flattened version of this array.
   * Any arrays or objects will be represented as a string in
   * JSON format.
   *
   * @return a list representing a flattened array.
   */
  public List<String> flatten() {
    final List<String> result = CollectionFactory.arrayList();
    for ( final Object object : this ) {
      result.add(object == null ? "" : object.toString());
    }
    return result;
  }

  /**
   * Convert this JSONArray to a map with the array index as the key.
   *
   * @return this JSONArray as a map.
   */
  public Map<String, String> asMap() {
    final List<String> list = flatten();
    final Map<String, String> map = CollectionFactory.hashMap();
    int count = 0;
    for ( final String string : list ) {
      map.put(String.valueOf(count++), string);
    }
    return map;
  }

  /**
   * Returns a compact string representation of this array.
   *
   * @return a compact string representation of this array.
   */
  @Override
  public String toString() {
    return toString(Format.COMPACT);
  }

  /**
   * Return a string representation of this array formatted according
   * to the specified format.
   *
   * @param format  controls the formatting.
   * @return a string representation of this JSONArray.
   */
  public String toString(final Format format) {
    final StringBuilder builder = new StringBuilder();
    toString(builder, format, 0);
    return builder.toString();
  }

  /**
   * Create a string representation of this array formatted according
   * to the specified format using an existing string builder.
   *
   * @param builder      the string builder.
   * @param format       the format.
   * @param indentLevel  the initial indentation level.
   */
  public void toString(final StringBuilder builder, final Format format, final int indentLevel) {
    int level = indentLevel;
    final String indentation = format.indentation(level++);
    final String innerIndentation = format.indentation(level);
    builder.append(format.preOpenBrace).append(LEFT_BRACKET);
    if ( size() > 0 ) {
      builder.append(format.postOpenBrace);
      boolean first = true;
      for ( final Object object : this ) {
	if ( first ) {
	  first = false;
	}
	else {
	  builder.append(format.preComma).append(COMMA).append(format.postComma);
	}
	builder.append(innerIndentation);
	JSON.appendValue(builder, format, level, object);
      }
      builder.append(format.preCloseBrace).append(indentation);
    }
    builder.append(RIGHT_BRACKET).append(format.postCloseBrace);
  }
}
