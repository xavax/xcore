//
// Copyright 2015, 2019 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//

package com.xavax.concurrent;

import com.xavax.util.AbstractJoinableObject;

/**
 * AbstractPage is the base class for pages in a pageable object.
 */
public abstract class AbstractPage extends AbstractJoinableObject {
  /**
   * The parent object.
   */
  protected final AbstractPageableObject<?> parent;

  /**
   * Construct an AbstractPage with the specified parent.
   *
   * @param parent  the parent pageable object.
   */
  protected AbstractPage(final AbstractPageableObject<?> parent) {
    this.parent = parent;
  }

}
