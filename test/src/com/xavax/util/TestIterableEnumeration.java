//
// Copyright 2011 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//
package com.xavax.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

import com.xavax.util.IterableEnumeration;

import static org.testng.Assert.*;

public class TestIterableEnumeration {

  @BeforeMethod
  public void setUp() {
    List<String> l = Arrays.asList(cities);
    en = Collections.enumeration(l);
  }

  @Test
  public void testIterableEnumeration() {
    IterableEnumeration<String> ie = new IterableEnumeration<String>(en);
    Iterator<String> it = ie.iterator();
    assertNotNull(it);
    assertTrue(it.hasNext());
    assertEquals(it.next(), cities[0]);
    assertEquals(it.next(), cities[1]);
    assertEquals(it.next(), cities[2]);
  }

  private String[] cities = new String[] {
      "Atlanta", "Birmingham", "Jackson"
  };
  private Enumeration<String> en;
}
