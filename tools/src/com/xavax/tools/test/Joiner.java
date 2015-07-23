//
// Copyright 2013 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//
package com.xavax.tools.test;

import java.util.Collection;

/**
 * Joiner is a utility for joining arrays and lists of objects as strings.
 */
final public class Joiner {
  private final static String SEPARATOR = ", ";

  private Joiner() {
  }

  /**
   * Join an array of parameters as strings.
   *
   * @param parameters  parameters to be joined.
   * @return a string representation of the array.
   */
  public static String join(final Object... parameters) {
    final StringBuilder buffer = new StringBuilder();
    join(buffer, parameters);
    return buffer.toString();
  }

  /**
   * Join a collection of parameters as strings.
   *
   * @param parameters  parameters to be joined.
   * @return a string representation of the array.
   */
  public static String join(final Collection<?> parameters) {
    final StringBuilder buffer = new StringBuilder();
    join(buffer, parameters);
    return buffer.toString();
  }

  /**
   * Append an array of parameters to the output.
   *
   * @param buffer      the output buffer.
   * @param parameters  the array of parameters to add.
   */
  private static void join(final StringBuilder buffer, final Object[] parameters) {
    buffer.append('[');
    boolean first = true;
    for ( final Object parameter : parameters ) {
      if ( first ) {
	first = false;
      }
      else {
	buffer.append(SEPARATOR);
      }
      join(buffer, parameter);
    }
    buffer.append(']');
  }

  /**
   * Append a collection to the output;
   *
   * @param buffer  the output buffer.
   * @param collection  the collection to append.
   */
  private static void join(final StringBuilder buffer,
			   final Collection<?> collection) {
    buffer.append('{');
    boolean first = true;
    for ( final Object parameter : collection ) {
      if ( first ) {
	first = false;
      }
      else {
	buffer.append(SEPARATOR);
      }
      join(buffer, parameter);
    }
    buffer.append('}');
  }

  /**
   * Append a parameter to the output.
   *
   * @param buffer  the output buffer.
   * @param parameter  the parameter to append.
   */
  private static void join(final StringBuilder buffer, final Object parameter) {
    if ( parameter == null ) {
      buffer.append("null");
    }
    else if ( parameter instanceof Object[] ) {
      join(buffer, (Object[]) parameter);
    }
    else if ( parameter instanceof Collection<?> ) {
      join(buffer, (Collection<?>) parameter);
    }
    else {
      buffer.append(parameter.toString());
    }
  }
}
