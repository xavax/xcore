//
// Copyright 2023 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//
package com.xavax.annotations;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * AnnotationContextTest is the test case for AnnotationContext.
 */
public class AnnotationContextTest {

  @Test
  public void testStack() {
    AnnotationsManager manager = new AnnotationsManager();
    final Object object = new Object();
    AnnotationContext<Object> context = new AnnotationContext<Object>(manager);
    int level = context.push(object);
    assertEquals(1, level);
    level = context.push();
    assertEquals(2, level);
    Object result = context.pop();
    assertNull(result);
    result = context.pop();
    assertEquals(object, result);
  }

  @Test
  public void testIndent() {
    AnnotationsManager manager = new AnnotationsManager();
    final Object object = new Object();
    AnnotationContext<Object> context =
	new AnnotationContext<Object>(manager).withIndent("  ");
    String indent = context.indent();
    assertEquals(0, indent.length());
    context.push(object);
    indent = context.indent();
    assertEquals(2, indent.length());
    context.push(object);
    indent = context.indent();
    assertEquals(4, indent.length());
    context.push(object);
    indent = context.indent();
    assertEquals(6, indent.length());
    context.pop();
    indent = context.indent();
    assertEquals(4, indent.length());
    context.push(object);
    indent = context.indent();
    assertEquals(6, indent.length());
    context.pop();
    indent = context.indent();
    assertEquals(4, indent.length());
    context.pop();
    indent = context.indent();
    assertEquals(2, indent.length());
    context.pop();
    indent = context.indent();
    assertEquals(0, indent.length());
  }
}
