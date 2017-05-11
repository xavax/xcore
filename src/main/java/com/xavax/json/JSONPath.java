//
// Copyright 2011 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//
package com.xavax.json;

import java.util.ArrayList;

import com.xavax.util.Joinable;
import com.xavax.util.Joiner;

import static com.xavax.util.Constants.*;

/**
 * JSONPath encapsulates a path used to navigate through a JSON to a specific
 * element.
 * 
 * @author Phil Harbison
 */
public final class JSONPath extends ArrayList<String> implements Joinable {
  public final static long serialVersionUID = 0;

  private final static String SEPARATOR_REGEX = "[.]";

  /**
   * Construct a JSONPath from an array of path elements.
   *
   * @param paths  an array of path elements.
   */
  public JSONPath(final String... paths) {
    append(paths);
  }

  /**
   * Create a JSONPath from an array of path elements.
   *
   * @param paths  an array of path elements.
   * @return a JSONPath.
   */
  public static JSONPath create(final String... paths) {
    return new JSONPath(paths);
  }

  /**
   * Append one or more strings to this path. Each string contains one or more
   * path components separated by '.'.
   * 
   * @param paths one or more strings containing path components.
   * @return this path.
   */
  public JSONPath append(final String... paths) {
    for ( final String path : paths ) {
      if ( path != null && !path.equals(EMPTY_STRING) ) {
	final String[] parts = path.trim().split(SEPARATOR_REGEX);
	for ( final String part : parts ) {
	  if ( !part.isEmpty() ) {
	    add(part);
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
  public JSONPath append(final JSONPath path) {
    this.addAll(path);
    return this;
  }

  /**
   * Returns a string representation of this path.
   *
   * @return a string representation of this path.
   */
  @Override
  public String toString() {
    return join(new Joiner()).toString();
  }

  /**
   * Join this object to the specified joiner.
   *
   * @param joiner  the joiner to use.
   * @return the joiner.
   */
  public Joiner join(final Joiner joiner) {
    if ( joiner != null ) {
      boolean first = true;
      for ( final String string : this ) {
	if ( first ) {
	  first = false;
	}
	else {
	  joiner.appendRaw(PERIOD);
	}
	joiner.appendRaw(string);
      }
    }
    return joiner;
  }
}
