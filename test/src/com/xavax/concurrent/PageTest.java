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
  private final static String EXPECTED = "[0000.0000";

  private Page page;

  @BeforeMethod
  public void beforeMethod() {
    page = new Page();
  }

  @Test
  public void testSetBits() {
    page.setBit(31, true);
    boolean value = page.getBit(31);
    assertTrue(value);
    page.setBit(31, false);
    value = page.getBit(31);
    assertFalse(value);
    page.setBit(32, true);
    value = page.getBit(32);
    assertTrue(value);
    page.setBit(32, false);
    value = page.getBit(32);
    assertFalse(value);
    for ( int i = 0; i < BITS_PER_PAGE; ++i ) {
      page.setBit(i, true);
      checkBits(page, i, true);
      page.setBit(i, false);
      checkBits(page, i, false);
    }
  }

  private void checkBits(Page page, int index, boolean state) {
    for ( int i = 0; i < BITS_PER_PAGE; ++i ) {
      boolean value = page.getBit(i);
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
    String s = page.toString().substring(0, EXPECTED.length());
    assertEquals(s, EXPECTED);
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
