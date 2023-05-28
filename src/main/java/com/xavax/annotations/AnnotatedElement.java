//
// Copyright 2023 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//
package com.xavax.annotations;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import com.xavax.util.AbstractJoinableObject;
import com.xavax.util.CollectionFactory;
import com.xavax.util.Joiner;

import static com.xavax.util.Constants.*;

/**
 * AnnotatedElement is the base class for all annotated elements.
 */
public abstract class AnnotatedElement extends AbstractJoinableObject {
  protected final static String ANNOTATIONS = "annotations";
  protected final static String ANNOTATION_MAP = "annotationMap";
  protected final static String CLAZZ = "clazz";
  protected final static String FIELD = "field";
  protected final static String FIELDS = "fields";
  protected final static String FIELD_MAP = "fieldMap";
  protected final static String METHOD = "method";
  protected final static String METHODS = "methods";
  protected final static String METHOD_MAP = "methodMap";
  protected final static String NAME = "name";
  protected final static String PARENT = "parent";

  protected final AnnotatedElementType type;
  protected final String name;
  protected final AnnotatedElement parent;
  protected final List<Annotation> annotations;
  protected final Map<String, Annotation> annotationMap;

  /**
   * Construct an annotated element.
   *
   * @param name  the element name.
   */
  public AnnotatedElement(final AnnotatedElement parent,
                          final AnnotatedElementType type,
                          final String name) {
    this.type = type;
    this.name = name;
    this.parent = parent;
    this.annotations = CollectionFactory.arrayList();
    this.annotationMap = CollectionFactory.hashMap();
  }

  /**
   * Add an annotation.
   *
   * @param annotation  the annotation to add.
   */
  public void add(final Annotation annotation) {
    final Class<? extends Annotation> type = annotation.annotationType();
    final String name = type.getSimpleName();
    annotations.add(annotation);
    annotationMap.put(name, annotation);
  }

  /**
   * Returns the list of annotations for the class.
   *
   * @return the list of annotations for the class.
   */
  public List<Annotation> getAnnotations() {
    return annotations;
  }

  /**
   * Returns the annotation map.
   *
   * @return the annotation map.
   */
  public Map<String, Annotation> getAnnotationMap() {
    return annotationMap;
  }

  /**
   * Returns the class name.
   *
   * @return the class name.
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the element type.
   *
   * @return the element type.
   */
  public AnnotatedElementType getType() {
    return type;
  }

  /**
   * Returns the parent of this element.
   *
   * @return the parent of this element.
   */
  public AnnotatedElement getParent() {
    return parent;
  }

  /**
   * Scan for annotations.
   */
  public abstract void scan(final AnnotationContext<?> context);

  /**
   * Add all annotations for this element.
   *
   * @param annotations  the annotations for this element.
   */
  public void scan(final AnnotationContext<?> context,
                   final Annotation[] annotations) {
    for ( final Annotation annotation : annotations ) {
      add(annotation);
    }
  }

  /**
   * Traverse this annotated element. Call the consumer function.
   *
   * @param context   the context for the traversal.
   * @param consumer  the function to be called with this element.
   */
  public abstract void traverse(final AnnotationContext<?> context,
                       final BiConsumer<AnnotationContext<?>, AnnotatedElement> consumer);

  /**
   * Output this object to the specified joiner.
   *
   * @param joiner  the joiner to use for output.
   * @return this joiner.
   */
  @Override
  public Joiner doJoin(final Joiner joiner) {
    final String parentName = parent == null ? NULL_INDICATOR : parent.name;
    joiner.appendField(NAME, name)
    	  .appendField(PARENT, parentName)
    	  .appendField(ANNOTATIONS, annotations)
    	  .appendField(ANNOTATION_MAP, annotationMap);
    return joiner;
  }
}
