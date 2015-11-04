//
// Copyright 2015 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//
package com.xavax.util;

/**
 * Joinable is an interface for objects that can participate in joining.
 */
public interface Joinable {
  /**
   * Join this object to the specified joiner.
   *
   * @param joiner  the joiner to use.
   * @return the joiner.
   */
  Joiner join(final Joiner joiner);
}
