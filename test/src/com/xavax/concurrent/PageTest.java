//
// Copyright 2015 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//

package com.xavax.concurrent;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static com.xavax.concurrent.ConcurrentBitSet.*;

public class PageTest {
  private final static String EXPECTED1 = "[01000000.00010000.00000100.00000001";
  private final static String EXPECTED2 = "[11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111.00000000";
  private final static String EXPECTED3 = "[00011111.11111111.11111111.11111111.11111111.11110000.00000000.00000000.00000000";
  private final static String EXPECTED4 = "[11100000.00000000.00000000.00000000.00000000.00001111.11111111.11111111.00000000";
  private final static String EXPECTED5 = "[00000000.00000000.00000000.00000000.00000000.00000000.00000000.11111111.00000000";
  private final static String EXPECTED6 = "[11111111.00000000.00000000.00000000.00000000.00000000.00000000.11111111.00000000";

  private Page page;

  @BeforeMethod
  public void beforeMethod() {
    page = new Page();
  }

  @Test
  public void testSetBit() {
    page.set(31, true);
    boolean value = page.get(31);
    assertTrue(value);
    page.set(31, false);
    value = page.get(31);
    assertFalse(value);
    page.set(32, true);
    value = page.get(32);
    assertTrue(value);
    page.set(32, false);
    value = page.get(32);
    assertFalse(value);
    for ( int i = 0; i < BITS_PER_PAGE; ++i ) {
      page.set(i, true);
      checkBits(page, i, true);
      page.set(i, false);
      checkBits(page, i, false);
    }
  }

  @Test
  public void testSetBits() {
    page.set(0,63);
    String s = page.toString().substring(0, EXPECTED2.length());
    assertEquals(s, EXPECTED2);
    page = new Page();
    page.set(3,43);
    s = page.toString().substring(0, EXPECTED3.length());
    assertEquals(s, EXPECTED3);
  }

  @Test
  public void testClearBits() {
    page.set(0,63);
    String s = page.toString().substring(0, EXPECTED2.length());
    assertEquals(s, EXPECTED2);
    page.clear(3,43);
    s = page.toString().substring(0, EXPECTED4.length());
    assertEquals(s, EXPECTED4);
    page.set(0,63);
    page.clear(0,55);
    s = page.toString().substring(0, EXPECTED5.length());
    assertEquals(s, EXPECTED5);
    page.set(0,63);
    page.clear(8,55);
    s = page.toString().substring(0, EXPECTED6.length());
    assertEquals(s, EXPECTED6);
  }

  private void checkBits(Page page, int index, boolean state) {
    for ( int i = 0; i < BITS_PER_PAGE; ++i ) {
      boolean value = page.get(i);
      if ( i == index ) {
	assertEquals(value, state);
      }
      else {
	assertFalse(value);
      }
    }
  }

  @Test
  public void testToString() {
    page.set(1, true);
    page.set(11, true);
    page.set(21, true);
    page.set(31, true);
    String s = page.toString().substring(0, EXPECTED1.length());
    assertEquals(s, EXPECTED1);
  }

  // @Test(expectedExceptions = AssertionError.class)
  // public void testGetLowerBounds() {
  // entry.getBit(-1);
  // }
  //
  // @Test(expectedExceptions = AssertionError.class)
  // public void testGetUpperBounds() {
  // entry.getBit(BITS_PER_ENTRY + 1);
  // }
  //
  // @Test(expectedExceptions = AssertionError.class)
  // public void testSetLowerBounds() {
  // entry.setBit(-1, true);
  // }
  //
  // @Test(expectedExceptions = AssertionError.class)
  // public void testSetUpperBounds() {
  // entry.setBit(BITS_PER_ENTRY + 1, true);
  // }
}
