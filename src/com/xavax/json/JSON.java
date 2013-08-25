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
  /**
   * Format describes how a JSON should be formatted when converted to a string.
   */
  public static class Format {
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

    public Format(boolean indent, String quoteIdentifier, String quoteString,
		  String preColon, String preComma, String preOpenBrace,
		  String preCloseBrace, String postColon, String postComma,
		  String postOpenBrace, String postCloseBrace) {
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

    public String indentation(int level) {
      return indent ? tabs.substring(0, level) : "";
    }

    private final static String tabs = "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t";
  }

  public JSON() {
  }

  public JSON(Map<String, Object> map) {
    for (Map.Entry<String, Object> entry : map.entrySet()) {
      super.put(entry.getKey(), entry.getValue());
    }
  }
  
  public JSON(JSON json) {
    for (Map.Entry<String, Object> entry : json.entrySet()) {
      Object o = entry.getValue();
      if ( o instanceof JSON ) {
	o = new JSON((JSON) o);
      }
      else if ( o instanceof JSONArray ) {
	o = new JSONArray((JSONArray) o);
      }
      super.put(entry.getKey(), o);
    }
  }

  public JSON create() {
    return new JSON();
  }

  public JSON put(String key, Object value) {
    super.put(key, value);
    return this;
  }

  public JSON put(String key, Collection<Object> value) {
    super.put(key, new JSONArray(value));
    return this;
  }

  public JSON put(String key, Object[] value) {
    super.put(key, new JSONArray(value));
    return this;
  }

  public JSON put(String key, Map<String, Object> value) {
    JSON json = new JSON(value);
    super.put(key, json);
    return this;
  }

  public JSON put(String key, JSON json) {
    super.put(key, json);
    return this;
  }

  public JSON put(String key, JSONArray array) {
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
  public Object get(JSONPath path, int... params) {
    int count = 0;
    int level = 1;
    int levels = path.size();
    Object result = null;
    JSON map = this;
    for (String s : path) {
      result = map.get(s);
      if ( result instanceof JSON ) {
	map = (JSON) result;
      }
      else if ( result instanceof JSONArray ) {
	if ( level < levels ) {
	  int index = params.length > count ? params[count++] : 0;
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
  public Object get(Object key) {
    return key instanceof JSONPath ? get((JSONPath) key, 0) : super.get(key);
  }

  public JSONArray getArray(String key) {
    Object o = get(key);
    return o != null && o instanceof JSONArray ? (JSONArray) o : null;
  }

  public JSONArray getArray(JSONPath path, int... params) {
    Object o = get(path, params);
    return o != null && o instanceof JSONArray ? (JSONArray) o : null;
  }

  private Boolean getBoolean(Object o) {
    Boolean result = null;
    if ( o instanceof Boolean ) {
      result = (Boolean) o;
    }
    else if ( o instanceof String ) {
      result = Boolean.valueOf((String) o);
    }
    return result;
  }

  public Boolean getBoolean(String key) {
    return getBoolean(get(key));
  }

  public Boolean getBoolean(JSONPath path, int... params) {
    return getBoolean(get(path, params));
  }

  public boolean getBoolean(String key, boolean defaultValue) {
    Boolean b = getBoolean(key);
    return b == null ? defaultValue : b.booleanValue();
  }

  private Double getDouble(Object o) {
    Double result = null;
    if ( o instanceof Double ) {
      result = (Double) o;
    }
    else if ( o instanceof String ) {
      result = Double.valueOf((String) o);
    }
    return result;
  }

  public Double getDouble(String key) {
    return getDouble(get(key));
  }

  public Double getDouble(JSONPath path, int... params) {
    return getDouble(get(path, params));
  }

  public double getDouble(String key, double defaultValue) {
    Double d = getDouble(key);
    return d == null ? defaultValue : d.doubleValue();
  }

  private Long getLong(Object o) {
    Long result = null;
    if ( o instanceof Long ) {
      result = (Long) o;
    }
    else if ( o instanceof String ) {
      result = Long.valueOf((String) o);
    }
    return result;
  }

  public Long getLong(String key) {
    return getLong(get(key));
  }

  public Long getLong(JSONPath path, int... params) {
    return getLong(get(path, params));
  }

  public long getLong(String key, long defaultValue) {
    Long l = getLong(key);
    return l == null ? defaultValue : l.longValue();
  }

  private String getString(Object o) {
    String result = null;
    if ( o instanceof String ) {
      result = (String) o;
    }
    else if ( o != null ) {
      result = o.toString();
    }
    return result;
  }

  public String getString(String key) {
    return getString(get(key));
  }

  public String getString(JSONPath path, int... params) {
    return getString(get(path, params));
  }

  public String getString(String key, String defaultValue) {
    String s = getString(key);
    return s == null ? defaultValue : s;
  }

  public JSON getJSON(String key) {
    Object o = get(key);
    return o != null && o instanceof JSON ? (JSON) o : null;
  }

  public JSON getJSON(JSONPath path, int... params) {
    Object o = get(path, params);
    return o != null && o instanceof JSON ? (JSON) o : null;
  }

  public Map<String, String> flatten() {
    Map<String, String> result = CollectionFactory.hashMap();
    for (Map.Entry<String, Object> entry : entrySet()) {
      Object value = entry.getValue();
      result.put(entry.getKey(), value == null ? null : value.toString());
    }
    return result;
  }

  public JSON merge(JSON json) {
    for (Map.Entry<String, Object> entry : json.entrySet()) {
      String key = entry.getKey();
      Object o = entry.getValue();
      if ( o instanceof JSON ) {
	Object target = get(key);
	if ( target instanceof JSON ) {
	  ((JSON) target).merge((JSON) o);
	}
	else {
	  o = new JSON((JSON) o);
	  super.put(key, o);
	}
      }
      else if ( o instanceof JSONArray ) {
	Object target = get(key);
	if ( target instanceof JSONArray ) {
	  ((JSONArray) target).addAll((JSONArray) o);
	}
	else {
	  o = new JSONArray((JSONArray) o);
	  super.put(key, o);
	}
      }
      else {
	super.put(key, o);
      }
    }
    return this;
  }

  public String toString() {
    return toString(Format.COMPACT);
  }

  public String toString(Format format) {
    StringBuilder sb = new StringBuilder();
    // sb.append("{").append(format.postOpenBrace);
    toString(sb, format, 0);
    // sb.append(format.preCloseBrace).append("}");
    return sb.toString();
  }

  public void toString(StringBuilder sb, Format format, int level) {
    String indentation = format.indentation(level++);
    String innerIndentation = format.indentation(level);
    sb.append(format.preOpenBrace).append("{");
    if ( entrySet().size() > 0 ) {
      sb.append(format.postOpenBrace);
      boolean first = true;
      for (Map.Entry<String, Object> entry : entrySet()) {
	if ( first ) {
	  first = false;
	}
	else {
	  sb.append(format.preComma).append(",").append(format.postComma);
	}
	sb.append(innerIndentation);
	sb.append(format.quoteIdentifier).append(entry.getKey())
	    .append(format.quoteIdentifier);
	sb.append(format.preColon).append(":").append(format.postColon);
	Object value = entry.getValue();
	appendValue(sb, format, level, value);
      }
      sb.append(format.preCloseBrace).append(indentation);
    }
    sb.append("}").append(format.postCloseBrace);
  }

  public static void appendValue(StringBuilder sb, Format format, int level,
				 Object value) {
    if ( value instanceof JSON ) {
      ((JSON) value).toString(sb, format, level);
    }
    else if ( value instanceof JSONArray ) {
      ((JSONArray) value).toString(sb, format, level);
    }
    else if ( value instanceof String ) {
      String s = StringEscapeUtils.escapeEcmaScript((String) value);
      sb.append(format.quoteString).append(s).append(format.quoteString);
    }
    else {
      sb.append(value);
    }
  }

  public byte[] getBytes() {
    String s = toString();
    return s.getBytes();
  }

  public final static long serialVersionUID = 0;
}
