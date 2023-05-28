//
// Copyright 2023 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//
package com.xavax.annotations;

import java.lang.reflect.Field;
import java.util.function.BiConsumer;

import com.xavax.util.Joiner;

/**
 * AnnotatedField encapsulates information about an annotated field.
 */
public class AnnotatedField extends AnnotatedElement {
  private Field field;

  /**
   * Construct an AnnotatedField.
   *
   * @param parent  the parent element.
   * @param field   the field.
   */
  public AnnotatedField(final AnnotatedElement parent,
                        final Field field) {
    super(parent, AnnotatedElementType.FIELD, field.getName());
    this.field = field;
  }

  /**
   * Returns the field.
   *
   * @return the field.
   */
  public Field getField() {
    return field;
  }

  /**
   * Scan for annotations.
   */
  public void scan(final AnnotationContext<?> context) {
    scan(context, field.getAnnotations());
  }

  /**
   * Traverse this annotated field. Call the consumer function.
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
    joiner.appendField(FIELD, field.getName());
    return joiner;
  }

}
