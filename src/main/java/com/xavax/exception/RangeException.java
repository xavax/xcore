//
// Copyright 2015 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//

package com.xavax.exception;

import static com.xavax.message.XMessage.OUT_OF_RANGE;

/**
 * RangeException is thrown when an unsupported integer value is detected.
 */
public class RangeException extends XRuntimeException {
  private final static long serialVersionUID = 1L;

  private final long min;
  private final long max;
  private final long attempted;
  private final String name;

  /**
   * Construct a range exception.
   *
   * @param min    the minimum supported value.
   * @param max    the maximum supported value.
   * @param value  the attempted value that is out of range.
   * @param name   the name of the value that is out of range.
   */
  public RangeException(final long min, final long max, final long value, final String name) {
    super(OUT_OF_RANGE, value, name, min, max);
    this.min = min;
    this.max = max;
    this.attempted = value;
    this.name = name;
  }

  /**
   * Returns the minimum supported value.
   *
   * @return the minimum supported value.
   */
  public long getMinimum() {
    return min;
  }

  /**
   * Returns the maximum supported value.
   *
   * @return the maximum supported value.
   */
  public long getMaximum() {
    return max;
  }

  /**
   * Returns the attempted value that exceeded the range.
   *
   * @return the attempted value.
   */
  public long getAttempted() {
    return attempted;
  }

  /**
   * Returns the name of the value that is out of range.
   *
   * @return the name of the value that is out of range.
   */
  public String getName() {
    return name;
  }
}
