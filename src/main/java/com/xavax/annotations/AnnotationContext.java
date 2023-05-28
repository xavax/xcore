//
// Copyright 2023 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//
package com.xavax.annotations;

import java.util.Stack;

import com.xavax.util.AbstractJoinableObject;
import com.xavax.util.CollectionFactory;
import com.xavax.util.Indentation;
import com.xavax.util.Joiner;

import static com.xavax.util.Constants.EMPTY_STRING;

/**
 * AnnotationContext provides a context for the scan and traverse
 * methods. It tracks the depth of the traversal.
 *
 * @param <T>  the type of objects on the stack.
 */
public class AnnotationContext<T> extends AbstractJoinableObject {
  private final static String HAS_INDENT = "hasIndent";
  private final static String LEVEL = "level";

  private final AnnotationsManager manager;

  private boolean hasIndent;
  private int level;
  private StringBuilder builder;
  private Stack<T> stack;

  private Indentation indentation;

  /**
   * Construct an AnnotationContext
   */
  public AnnotationContext(final AnnotationsManager manager) {
    this.manager = manager;
    level = 0;
    stack = CollectionFactory.stack();
  }

  /**
   * Create a builder for general use.
   * @return
   */
  public AnnotationContext<T> withBuilder() {
    builder = new StringBuilder();
    return this;
  }

  /**
   * Manage indentation. Each level of indentation adds the
   * indentation string.
   *
   * @param indentString  the string to use for indentation.
   * @return this context.
   */
  public AnnotationContext<T> withIndent(final String indentString) {
    hasIndent = true;
    indentation = Indentation.getInstance(indentString);
    return this;
  }

  /**
   * Returns the builder.
   *
   * @return the builder.
   */
  public StringBuilder builder() {
    if ( builder == null ) {
      builder = new StringBuilder();
    }
    return builder;
  }

  /**
   * Returns an indentation string for the current level.
   *
   * @return an indentation string for the current level.
   */
  public String indent() {
    return hasIndent ? indentation.indent(level) : EMPTY_STRING;
  }

  /**
   * Returns the current level.
   *
   * @return the current level.
   */
  public int getLevel() {
    return level;
  }

  /**
   * Returns the AnnotationsManager.
   *
   * @return the AnnotationsManager.
   */
  public AnnotationsManager getManager() {
    return manager;
  }

  /**
   * Pop an object from the stack and decrement the level.
   *
   * @return the top of the stack
   */
  public T pop() {
    --level;
    return stack.pop();
  }

  /**
   * Push an object onto the stack and increment the level.
   *
   * @return the new level.
   */
  public int push(final T object) {
    stack.push(object);
    return ++level;
  }

  /**
   * Push a null onto the stack and increment the level.
   *
   * @return the new level.
   */
  public int push() {
    return push(null);
  }
  
  /**
   * Output this object to the specified joiner.
   *
   * @param joiner  the joiner to use for output.
   * @return this joiner.
   */
  @Override
  public Joiner doJoin(final Joiner joiner) {
    joiner.appendField(HAS_INDENT, hasIndent)
    	  .appendField(LEVEL, level);
    return joiner;
  }
}
