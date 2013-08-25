//
// Copyright 2011 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//
package com.xavax.util;

import java.util.Iterator;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.xavax.util.IterableStringTokenizer;

import static org.testng.Assert.*;

public class TestIterableStringTokenizer {

  @BeforeMethod
  public void setUp() {
  }

  @Test
  public void testIterableStringTokenizer() {
    IterableStringTokenizer st = new IterableStringTokenizer("abc def ghi jkl");
    Iterator<String> it = st.iterator();
    boolean b = it instanceof Iterator;
    assertTrue(b);
    assertTrue(it.hasNext());
    assertEquals(it.next(), "abc");
    assertEquals(it.next(), "def");
    assertEquals(it.next(), "ghi");
    assertEquals(it.next(), "jkl");
    assertFalse(it.hasNext());
    st = new IterableStringTokenizer("abc,def;ghi:jkl", ",;:");
    it = st.iterator();
    b = it instanceof Iterator;
    assertTrue(b);
    assertTrue(it.hasNext());
    assertEquals(it.next(), "abc");
    assertEquals(it.next(), "def");
    assertEquals(it.next(), "ghi");
    assertEquals(it.next(), "jkl");
    assertFalse(it.hasNext());
    st = new IterableStringTokenizer("abc,def;ghi:jkl", ",;:", true);
    it = st.iterator();
    b = it instanceof Iterator;
    assertTrue(b);
    assertTrue(it.hasNext());
    assertEquals(it.next(), "abc");
    assertEquals(it.next(), ",");
    assertEquals(it.next(), "def");
    assertEquals(it.next(), ";");
    assertEquals(it.next(), "ghi");
    assertEquals(it.next(), ":");
    assertEquals(it.next(), "jkl");
    assertFalse(it.hasNext());
  }
}
