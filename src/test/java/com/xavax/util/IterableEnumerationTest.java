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

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

import static org.testng.Assert.*;

/**
 * Test cases for the IterableEnumeration class.
 */
public class IterableEnumerationTest {
  private final static String[] CITIES = new String[] {
      "Atlanta", "Birmingham", "Jackson"
  };

  private Enumeration<String> cityList;

  /**
   * Perform set up before tests.
   */
  @BeforeMethod
  public void setUp() {
    cityList = Collections.enumeration(Arrays.asList(CITIES));
  }

  /**
   * Test the iterator returned by the IterableEnumeration.iterator method.
   */
  @Test
  public void testIterableEnumeration() {
    final IterableEnumeration<String> enumeration = new IterableEnumeration<String>(cityList);
    final Iterator<String> iterator = enumeration.iterator();
    assertNotNull(iterator);
    assertTrue(iterator.hasNext());
    assertEquals(iterator.next(), CITIES[0]);
    assertEquals(iterator.next(), CITIES[1]);
    assertEquals(iterator.next(), CITIES[2]);
  }

  /**
   * Test the static create method.
   */
  @Test
  public void testCreate() {
    final IterableEnumeration<String> enumeration = IterableEnumeration.create(cityList);
    assertNotNull(enumeration);
    final Iterator<String> iterator = enumeration.iterator();
    assertNotNull(iterator);
    assertTrue(iterator.hasNext());
    assertEquals(iterator.next(), CITIES[0]);
  }

  /**
   * Test the remove method throwing an exception.
   */
  @Test(expectedExceptions = UnsupportedOperationException.class)
  public void testRemove() {
    final IterableEnumeration<String> enumeration = new IterableEnumeration<String>(cityList);
    final Iterator<String> iterator = enumeration.iterator();
    iterator.remove();
  }
}
