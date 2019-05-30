package com.xavax.concurrent;

/**
 * Finder is a functional interface for functions that find
 * the page with a required feature.
 * 
 * @param <T>  the page type.
 */
@FunctionalInterface
public interface Finder<T extends AbstractPage> {
  /**
   * Apply this function to pages in a PageableObject.
   *
   * @param context  the page context.
   * @param page     the page to examine.
   * @return true if a match was found.
   */
  boolean apply(PageContext<T> context, T page);
}
