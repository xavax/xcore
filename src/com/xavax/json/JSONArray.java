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

public class JSONArray extends ArrayList<Object> {
  public JSONArray() {
  }

  public JSONArray(Collection<Object> c) {
    super(c);
  }

  public JSONArray(Object[] items) {
    for (Object o : items) {
      add(o);
    }
  }

  public JSONArray(JSONArray ja) {
    this.addAll(ja);
  }

  public boolean add(Collection<Object> c) {
    JSONArray ja = new JSONArray(c);
    return super.add((Object) ja);
  }

  public boolean add(Map<String, Object> map) {
    JSON json = new JSON(map);
    return super.add((Object) json);
  }

  public boolean add(JSON json) {
    return super.add((Object) json);
  }

  public boolean addAll(JSONArray ja) {
    boolean result = false;
    for (Object o : ja) {
      if ( o instanceof JSON ) {
	o = new JSON((JSON) o);
      }
      else if ( o instanceof JSONArray ) {
	o = new JSONArray((JSONArray) o);
      }
      add(o);
      result = true;
    }
    return result;
  }

  public List<String> flatten() {
    List<String> result = CollectionFactory.arrayList();
    for (Object o : this) {
      String s = o == null ? "" : o.toString();
      result.add(s);
    }
    return result;
  }

  public Map<String, String> asMap() {
    List<String> l = flatten();
    Map<String, String> map = CollectionFactory.hashMap();
    int i = 0;
    for (String s : l) {
      map.put(String.valueOf(i++), s);
    }
    return map;
  }

  public String toString() {
    return toString(Format.COMPACT);
  }

  public String toString(Format format) {
    StringBuilder sb = new StringBuilder();
    toString(sb, format, 0);
    return sb.toString();
  }

  public void toString(StringBuilder sb, Format format, int level) {
    String indentation = format.indentation(level++);
    String innerIndentation = format.indentation(level);
    sb.append(format.preOpenBrace).append("[");
    if (size() > 0) {
      sb.append(format.postOpenBrace);
      boolean first = true;
      for (Object o : this) {
	if (first) {
	  first = false;
	} else {
	  sb.append(format.preComma).append(",").append(format.postComma);
	}
	sb.append(innerIndentation);
	JSON.appendValue(sb, format, level, o);
      }
      sb.append(format.preCloseBrace).append(indentation);
    }
    sb.append("]").append(format.postCloseBrace);
  }

  public final static long serialVersionUID = 0;
}
