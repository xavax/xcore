package com.xavax.concurrent;

/**
 * PageContext is a container for criteria and results for
 * operations on pageable objects.
 *
 * @param <T>  the page type.
 */
public class PageContext<T extends AbstractPage> {
  private boolean found;
  private boolean required;
  private long startingIndex;
  private long currentIndex;
  private T currentPage;
  private Object result;

  /**
   * Construct a PageContext.
   */
  public PageContext() {
    reset();
  }

  /**
   * Reset the context. Clears the match found flag.
   *
   * @return this context.
   */
  public final PageContext<T> reset() {
    found = false;
    required = false;
    startingIndex = 0;
    currentIndex = 0;
    currentPage = null;
    result = null;
    return this;
  }

  /**
   * Returns true if a match was found.
   *
   * @return true if a match was found.
   */
  public boolean matchFound() {
    return found;
  }

  /**
   * Returns true if missing pages should be created.
   *
   * @return true if missing pages should be created.
   */
  public boolean isRequired() {
    return required;
  }

  /**
   * Create missing pages.
   *
   * @return this context.
   */
  public PageContext<T> withRequired() {
    required = true;
    return this;
  }

  /**
   * Returns the starting index.
   *
   * @return the starting index.
   */
  public long getStartingIndex() {
    return startingIndex;
  }

  /**
   * Sets the starting index.
   *
   * @param index  the starting index.
   * @return this context.
   */
  public PageContext<T> withStartingIndex(final long index) {
    this.startingIndex = index;
    return this;
  }

  /**
   * Returns the current index.
   *
   * @return the current index.
   */
  public long getCurrentIndex() {
    return currentIndex;
  }

  /**
   * Sets the current index.
   *
   * @param index  the current index.
   * @return this context.
   */
  public PageContext<T> withCurrentIndex(final long index) {
    this.currentIndex = index;
    return this;
  }

  /**
   * Returns the current page.
   *
   * @return the current page.
   */
  public T getCurrentPage() {
    return currentPage;
  }

  /**
   * Sets the current page.
   *
   * @param page  the current page.
   */
  public void setCurrentPage(final T page) {
    this.currentPage = page;
  }

  /**
   * Returns the result.
   *
   * @return the result.
   */
  public Object getResult() {
    return result;
  }

  /**
   * Sets the result.
   *
   * @param result  the result.
   */
  public void setResult(final Object result) {
    this.result = result;
  }
}
