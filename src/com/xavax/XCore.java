//
// Copyright 2004, 2015 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//
package com.xavax;

import com.xavax.info.XProduct;

/**
 * This class defines and provides access to the version information
 * and copyright for the Xavax core library.
 */
public final class XCore {
  private final static XProduct PRODUCT =
      new XProduct("XCore", 1, 0, 0, 2004, 2015, null);

  /**
   * Private constructor provided to keep the compiler from generating
   * a public default constructor.
   */
  private XCore() {}

  /**
   * Return the product information.
   * 
   * @return the product information.
   */
  public static XProduct getProduct() {
    return PRODUCT;
  }
}