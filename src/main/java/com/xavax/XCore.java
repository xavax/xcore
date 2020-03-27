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
public final class XCore extends XProduct {
  private final static XCore PRODUCT =
      new XCore("XCore", 1, 1, 6, 2004, 2020, null);

  /**
   * Construct an XCore.
   *
   * @param name   the product name.
   * @param major  the major version number.
   * @param minor  the minor version number.
   * @param patch  the patch level.
   * @param copyrightBegin  the copyright begin year.
   * @param copyrightEnd    the copyright end year.
   * @param copyright       the copyright message template.
   */
  private XCore(final String name, final int major, final int minor, final int patch,
                final int copyrightBegin, final int copyrightEnd, final String copyright)
  {
    super(name, major, minor, patch, copyrightBegin, copyrightEnd, copyright);
  }

  /**
   * Return the product information.
   * 
   * @return the product information.
   */
  public static XCore getProduct() {
    return PRODUCT;
  }
}
