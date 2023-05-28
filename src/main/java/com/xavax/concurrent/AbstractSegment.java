//
// Copyright 2015, 2019 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//

package com.xavax.concurrent;

import com.xavax.util.AbstractJoinableObject;
import com.xavax.util.Joiner;

/**
 * AbstractSegment is the base class for segments in a pageable object.
 * A segment consists of an array of pages that are created on demand.
 *
 * @param <T>  the type of pages in the segment.
 */
public abstract class AbstractSegment<T extends AbstractPage> extends AbstractJoinableObject {
  final static int SEGMENT_BUFFER_SIZE = 8192;

  protected int pageCount;
  protected final T[] pages;
  protected final AbstractPageableObject<T> parent;

  /**
   * Construct a segment.
   *
   * @param parent  the parent of this segment.
   */
  public AbstractSegment(final AbstractPageableObject<T> parent) {
    this.parent = parent;
    pages = createPageArray(parent.getPagesPerSegment());
  }

  /**
   * Create a new page to be added to the page map.
   *
   * @return a new page.
   */
  protected abstract T createPage();

  /**
   * Create an array of pages.
   *
   * @param size  the size of the array.
   * @return an array of pages.
   */
  protected abstract T[] createPageArray(final int size);

  /**
   * Returns the number of pages in this segment.
   * 
   * @return the number of pages in this segment.
   */
  int pageCount() {
    return pageCount;
  }

  /**
   * Returns the page containing the specified item, or null
   * if the page does not exist and require is false. If require
   * is true, missing pages are created.
   *
   * @param index    the global index of the item.
   * @param require  if true, missing pages will be created.
   * @return
   */
  T getPageContaining(final int index, final boolean require) {
    return getPage(parent.indexToPageIndex(index), require);
  }

  /**
   * Returns the page at the specified index in the page map. If the
   * page does not exist and require is true, create the page.
   *
   * @param require   true if a missing page should be created.
   * @param mapIndex  the index of the desired page.
   * @return the page at the specified index.
   */
  T getPage(final int mapIndex, final boolean require) {
    T page = null;
    synchronized ( pages ) {
      page = pages[mapIndex];
    }
    if ( page == null && require ) {
	page = createPage(mapIndex);
    }
    return page;
  }

  /**
   * Create a new page at the specified index in the page map.
   * The sentinel is acquired twice so it is not held during
   * page creation which consumes an unknown amount of time.
   *
   * @param mapIndex  the index for the new page.
   * @return a new page.
   */
  T createPage(final int mapIndex) {
    T page = null;
    synchronized ( pages ) {
      page = pages[mapIndex];
    }
    if ( page == null ) {
      page = createPage();
      synchronized ( pages ) {
	final T existing = pages[mapIndex];
	if ( existing == null ) {
	  pages[mapIndex] = page;
	  ++pageCount;
	  parent.pageCreated();
	}
	else {
	  page = existing;
	}
      }
    }
    return page;
  }

  T findPage(final PageContext<T> context, final Finder<T> finder) {
    T page = null;
    int pageIndex = parent.indexToPageIndex(context.getStartingIndex());
    for ( ; pageIndex <= pages.length; ++pageIndex ) {
      page = getPage(pageIndex, context.isRequired());
      if ( page != null && finder.apply(context, page) ) {
	break;
      }
    }
    return page;
  }

  /**
   * Returns a string representation of this segment.
   *
   * @return a string representation of this segment.
   */
  @Override
  public String toString() {
    return doJoin(Joiner.create(SEGMENT_BUFFER_SIZE)).toString();
  }

  /**
   * Join this object to the specified joiner.
   *
   * @param joiner  the joiner to use.
   * @return the joiner.
   */
  @Override
  public Joiner doJoin(final Joiner joiner) {
    joiner.append("pageCount", pageCount)
          .append("pages", (Object[]) pages);
    return joiner;
  }
}
