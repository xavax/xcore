//
// Copyright 2011 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//
package com.xavax.util;

import java.util.Iterator;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Test cases for the IterableStringTokenizer class.
 */
public class TestIterableStringTokenizer {
  private final static int COUNT = 4;
  private final static String DELIMITERS = ",;:";
  private final static String INPUT2 = "abc,def;ghi:jkl";
  private final static String INPUT1 = "abc def ghi jkl";
  private final static String SPACE = " ";
  private final static String TOKEN4 = "jkl";
  private final static String TOKEN3 = "ghi";
  private final static String TOKEN2 = "def";
  private final static String TOKEN1 = "abc";

  /**
   * Test the iterator returned by IterableStringTokenizer's iterator method.
   */
  @Test
  public void testIteratorMethods() {
    IterableStringTokenizer tokenizer = new IterableStringTokenizer(INPUT1);
    Iterator<String> 
    iterator = tokenizer.iterator();
    assertTrue(iterator instanceof Iterator);
    assertTrue(iterator.hasNext());
    assertEquals(iterator.next(), TOKEN1);
    assertEquals(iterator.next(), TOKEN2);
    assertEquals(iterator.next(), TOKEN3);
    assertEquals(iterator.next(), TOKEN4);
    assertFalse(iterator.hasNext());
    tokenizer = new IterableStringTokenizer(INPUT2, DELIMITERS);
    iterator = tokenizer.iterator();
    assertTrue(iterator instanceof Iterator);
    assertTrue(iterator.hasNext());
    assertEquals(iterator.next(), TOKEN1);
    assertEquals(iterator.next(), TOKEN2);
    assertEquals(iterator.next(), TOKEN3);
    assertEquals(iterator.next(), TOKEN4);
    assertFalse(iterator.hasNext());
    tokenizer = new IterableStringTokenizer(INPUT2, DELIMITERS, true);
    iterator = tokenizer.iterator();
    assertTrue(iterator instanceof Iterator);
    assertTrue(iterator.hasNext());
    assertEquals(iterator.next(), TOKEN1);
    assertEquals(iterator.next(), ",");
    assertEquals(iterator.next(), TOKEN2);
    assertEquals(iterator.next(), ";");
    assertEquals(iterator.next(), TOKEN3);
    assertEquals(iterator.next(), ":");
    assertEquals(iterator.next(), TOKEN4);
    assertFalse(iterator.hasNext());
  }

  /**
   * Test IterableStringTokenizer's direct methods.
   */
  @Test
  public void testMethods() {
    final IterableStringTokenizer tokenizer = new IterableStringTokenizer(INPUT1);
    assertEquals(tokenizer.countTokens(), COUNT);
    assertTrue(tokenizer.hasMoreTokens());
    assertEquals(tokenizer.nextToken(), TOKEN1);
    assertEquals(tokenizer.nextToken(SPACE), TOKEN2);
  }

  /**
   * Test the remove method throwing an exception.
   */
  @Test(expectedExceptions = UnsupportedOperationException.class)
  public void testRemove() {
    final IterableStringTokenizer tokenizer = new IterableStringTokenizer(INPUT1);
    final Iterator<String> iterator = tokenizer.iterator();
    iterator.remove();
  }
}
