//
// Copyright 2023 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//
package com.xavax.annotations;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import com.xavax.util.CollectionFactory;
import com.xavax.util.Joiner;

/**
 * AnnotatedClass encapsulates information about an annotated class.
 */
public class AnnotatedClass extends AnnotatedElement {
  private final static Map<String,Method> objectMethods;
  private final Class<?> clazz;
  private final List<AnnotatedField> fields;
  private final List<AnnotatedMethod> methods;
  private final Map<String, AnnotatedField> fieldMap;
  private final Map<String, AnnotatedMethod> methodMap;

  static {
    objectMethods = CollectionFactory.hashMap();
    for ( final Method method : Object.class.getMethods() ) {
      objectMethods.put(method.getName(), method);
    }
  }

  /**
   * Construct an AnnotatedClass.
   *
   * @param clazz  the annotated class.
   */
  public AnnotatedClass(final Class<?> clazz) {
    super(null, AnnotatedElementType.CLASS, clazz.getSimpleName());
    this.clazz = clazz;
    this.fields = CollectionFactory.arrayList();
    this.fieldMap = CollectionFactory.hashMap();
    this.methods = CollectionFactory.arrayList();
    this.methodMap = CollectionFactory.hashMap();
  }

  /**
   * Add an annotated field.
   *
   * @param field  the field to add.
   */
  public void add(final AnnotatedField field) {
    final String name = field.getName();
    fields.add(field);
    fieldMap.put(name, field);
  }

  /**
   * Add an annotated method.
   *
   * @param method  the method to add.
   */
  public void add(final AnnotatedMethod method) {
    final String name = method.getName();
    methods.add(method);
    methodMap.put(name, method);
  }

  /**
   * Returns the annotated class.
   *
   * @return the annotated class.
   */
  public Class<?> getAnnotatedClass()  {
    return clazz;
  }

  /**
   * Returns the list of annotated fields.
   *
   * @return the list of annotated fields.
   */
  public List<AnnotatedField> getFields() {
    return fields;
  }

  /**
   * Returns the map of annotated fields.
   *
   * @return the map of annotated fields.
   */
  public Map<String, AnnotatedField> getFieldMap() {
    return fieldMap;
  }

  /**
   * Returns the list of annotated methods.
   *
   * @return the list of annotated methods.
   */
  public List<AnnotatedMethod> getMethods() {
    return methods;
  }

  /**
   * Returns the map of annotated methods.
   *
   * @return the map of annotated methods.
   */
  public Map<String, AnnotatedMethod> getMethodMap() {
    return methodMap;
  }

  /**
   * Scan for annotations.
   */
  public void scan(final AnnotationContext<?> context) {
    scan(context, clazz.getAnnotations());
    scan(context, clazz);
  }

  public void scan(final AnnotationContext<?> context,
                   final Class<?> clazz) {
    for ( final Field field : clazz.getDeclaredFields() ) {
      AnnotatedField annotatedField = new AnnotatedField(this, field);
      add(annotatedField);
      annotatedField.scan(context);
    }
    for ( final Method method : clazz.getDeclaredMethods() ) {
      if ( objectMethods.get(method.getName()) == null ) {
	final AnnotatedMethod annotatedMethod = new AnnotatedMethod(this, method);
	add(annotatedMethod);
	annotatedMethod.scan(context);
      }
    }
    Class<?> superclass = clazz.getSuperclass();
    if ( superclass != Object.class ) {
      scan(context, superclass);
    }
  }

  /**
   * Traverse this annotated class. Call the consumer function
   * then traverse the fields and methods.
   *
   * @param context   the context for the traversal.
   * @param consumer  the function to be called with this element.
   */
  public void traverse(final AnnotationContext<?> context,
                       final BiConsumer<AnnotationContext<?>, AnnotatedElement> consumer) {
    consumer.accept(context, this);
    context.push();
    for ( final AnnotatedField field : fields ) {
      field.traverse(context, consumer);
    }
    for ( final AnnotatedMethod method : methods ) {
      method.traverse(context, consumer);
    }
    context.pop();
  }

  /**
   * Output this object to the specified joiner.
   *
   * @param joiner  the joiner to use for output.
   * @return this joiner.
   */
  @Override
  public Joiner doJoin(final Joiner joiner) {
    joiner.appendField(CLAZZ, clazz.getName())
    	  .appendField(FIELDS, fields)
    	  .appendField(FIELD_MAP, fieldMap)
    	  .appendField(METHODS, methods)
    	  .appendField(METHOD_MAP, methodMap);
    return joiner;
  }
}
