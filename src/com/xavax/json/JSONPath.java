//
// Copyright 2011 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//
package com.xavax.json;

import java.util.ArrayList;

/**
 * JSONPath encapsulates a path used to navigate through a JSON to a specific
 * element.
 * 
 * @author Phil Harbison
 */
public class JSONPath extends ArrayList<String> {
  public JSONPath() {
  }

  public JSONPath(String... paths) {
    append(paths);
  }

  public static JSONPath create(String... paths) {
    JSONPath jp = new JSONPath(paths);
    return jp;
  }

  /**
   * Append one or more strings to this path. Each string contains one or more
   * path components separated by '.'.
   * 
   * @param paths one or more strings containing path components.
   * @return this path.
   */
  public JSONPath append(String... paths) {
    for (String path : paths) {
      if ( path != null && !path.equals("") ) {
	String[] parts = path.trim().split("[.]");
	for (String s : parts) {
	  if ( s != null && !s.equals("") ) {
	    add(s);
	  }
	}
      }
    }
    return this;
  }

  /**
   * Append all components of a JSONPath to this JSONPath.
   * 
   * @param path a path to be appended to this path.
   * @return this path.
   */
  public JSONPath append(JSONPath path) {
    this.addAll(path);
    return this;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    boolean first = true;
    for (String s : this) {
      if ( first ) {
	first = false;
      }
      else {
	sb.append('.');
      }
      sb.append(s);
    }
    return sb.toString();
  }

  public final static long serialVersionUID = 0;
}
