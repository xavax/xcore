//
// Copyright 2017 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//
package com.xavax.util;

/**
 * AbstractJoinableObject is a base class for objects implementing Joinable.
 */
abstract public class AbstractJoinableObject implements Joinable {

  final static int DEFAULT_BUFFER_SIZE = 256;

  /**
   * Returns a string representation of this object.
   * @return a string representation of this object.
   */
  @Override
  public String toString() {
    return join(null).toString();
  }

  /**
   * Output this object to the specified joiner.
   *
   * @param joiner  the joiner to use for output.
   * @return this joiner.
   */
  @Override
  public Joiner join(final Joiner joiner) {
    return doJoin((joiner == null ? new Joiner(DEFAULT_BUFFER_SIZE) : joiner)
	.beginObject(null)).endObject();
  }

  /**
   * Output this object to the specified joiner.
   *
   * @param joiner  the joiner to use for output.
   * @return this joiner.
   */
  abstract protected Joiner doJoin(final Joiner joiner);
}