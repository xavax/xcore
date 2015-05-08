//
// Copyright 2015 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//

package com.xavax.exception;

/**
 * RangeException is thrown when an unsupported integer value is detected.
 */
public class RangeException extends RuntimeException {
	private final static long serialVersionUID = 1L;
	private final static String format = "value %d is not within the range %d and %d";

	private final long min;
	private final long max;
	private final long attempted;

	/**
	 * Construct a range exception.
	 *
	 * @param min  the minimum supported value.
	 * @param max  the maximum supported value.
	 * @param attempted  the attempted value that is out of range.
	 */
	public RangeException(long min, long max, long attempted) {
		super(String.format(format, attempted, min, max));
		this.min = min;
		this.max = max;
		this.attempted = attempted;
	}

	/**
	 * Returns the minimum supported value.
	 *
	 * @return  the minimum supported value.
	 */
	public long getMinimum() {
		return min;
	}

	/**
	 * Returns the maximum supported value.
	 *
	 * @return  the maximum supported value.
	 */
	public long getMaximum() {
		return max;
	}

	/**
	 * Returns the attempted value that exceeded the range.
	 *
	 * @return  the attempted value.
	 */
	public long getAttempted() {
		return attempted;
	}
}
