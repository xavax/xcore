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

  @Test
  public void testNextSetBit() {
    page.set(0, true);
    page.set(13, true);
    page.set(23, true);
    page.set(33, true);
    page.set(43, true);
    page.set(53, true);
    page.set(63, true);
    assertEquals(page.nextSetBit(0), 0);
    assertEquals(page.nextSetBit(1), 13);
    assertEquals(page.nextSetBit(14), 23);
    assertEquals(page.nextSetBit(24), 33);
    assertEquals(page.nextSetBit(34), 43);
    assertEquals(page.nextSetBit(44), 53);
    assertEquals(page.nextSetBit(54), 63);
    assertEquals(page.nextSetBit(64), -1);
  }

  @Test
  public void testNextClearBit() {
    page.set(0,BITS_PER_PAGE - 1);
    page.set(0, false);
    page.set(13, false);
    page.set(23, false);
    page.set(33, false);
    page.set(43, false);
    page.set(53, false);
    page.set(63, false);
    assertEquals(page.nextClearBit(0), 0);
    assertEquals(page.nextClearBit(1), 13);
    assertEquals(page.nextClearBit(14), 23);
    assertEquals(page.nextClearBit(24), 33);
    assertEquals(page.nextClearBit(34), 43);
    assertEquals(page.nextClearBit(44), 53);
    assertEquals(page.nextClearBit(54), 63);
    assertEquals(page.nextClearBit(64), -1);
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
