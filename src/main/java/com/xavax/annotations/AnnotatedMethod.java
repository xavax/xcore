//
// Copyright 2023 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//
package com.xavax.annotations;

import java.lang.reflect.Method;
import java.util.function.BiConsumer;

import com.xavax.util.Joiner;

/**
 * AnnotatedMethod encapsulates information about an annotated method.
 */
public class AnnotatedMethod extends AnnotatedElement {
  private Method method;

  /**
   * Construct an AnnotatedMethod.
   * 
   * @param parent  the parent element.
   * @param method  the method.
   */
  public AnnotatedMethod(final AnnotatedElement parent, final Method method) {
    super(parent, AnnotatedElementType.METHOD, method.getName());
    this.method = method;
  }

  /**
   * Returns the method.
   *
   * @return the method.
   */
  public Method getMethod() {
    return method;
  }

  /**
   * Scan for annotations.
   */
  public void scan(final AnnotationContext<?> context) {
    scan(context, method.getAnnotations());
  }

  /**
   * Traverse this annotated method. Call the consumer function.
   *
   * @param context   the context for the traversal.
   * @param consumer  the function to be called with this element.
   */
  public void traverse(final AnnotationContext<?> context,
                       final BiConsumer<AnnotationContext<?>, AnnotatedElement> consumer) {
    consumer.accept(context, this);
  }

  /**
   * Output this object to the specified joiner.
   *
   * @param joiner  the joiner to use for output.
   * @return this joiner.
   */
  @Override
  public Joiner doJoin(final Joiner joiner) {
    super.doJoin(joiner);
    joiner.appendField(METHOD, method.getName());
    return joiner;
  }
}
