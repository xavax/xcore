//
// Copyright 2023 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//
package com.xavax.annotations;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xavax.util.AbstractJoinableObject;
import com.xavax.util.CollectionFactory;
import com.xavax.util.Joiner;

/**
 * AnnotationsManager provides method to manage a repository of
 * metadata about annotations.
 */
public class AnnotationsManager extends AbstractJoinableObject {
  private final static String ADD = "add";
  private final static String CLASSNAME = AnnotationsManager.class.getSimpleName();
  private final static Logger logger = LoggerFactory.getLogger(AnnotationsManager.class);

  protected final List<AnnotatedClass> classes;
  protected final Map<String, AnnotatedClass> classMap;

  /**
   * Construct an AnnotationsManager.
   */
  public AnnotationsManager() {
    classes = CollectionFactory.arrayList();
    classMap = CollectionFactory.hashMap();
  }

  /**
   * Add a class to the repository.
   *
   * @param clazz  the class to add.
   */
  public void add(final Class<?> clazz) {
    final String name = clazz.getSimpleName();
    logger.debug("{}.{}: ading class {}", CLASSNAME, ADD, name);
    final AnnotatedClass annotatedClass = new AnnotatedClass(clazz);
    classes.add(annotatedClass);
    classMap.put(name, annotatedClass);
  }

  /**
   * Scan all registered classes for annotations.
   */
  public void scan(final AnnotationContext<?> context) {
    for ( final AnnotatedClass annotatedClass : classes ) {
      annotatedClass.scan(context);
    }
  }

  /**
   * Traverse all registered classes.
   *
   * @param context   the context for the traversal.
   * @param consumer  the function to be called with this element.
   */
  public void traverse(final AnnotationContext<?> context,
                       final BiConsumer<AnnotationContext<?>, AnnotatedElement> consumer) {
    for ( final AnnotatedClass annotatedClass : classes ) {
      annotatedClass.traverse(context, consumer);
    }
  }

  /**
   * Output this object to the specified joiner.
   *
   * @param joiner  the joiner to use for output.
   * @return this joiner.
   */
  @Override
  public Joiner doJoin(final Joiner joiner) {
    return joiner;
  }

}
