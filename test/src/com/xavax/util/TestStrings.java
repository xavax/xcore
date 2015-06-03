//
// Copyright 2015 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//

package com.xavax.util;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Test cases for the Strings utility class.
 */
public class TestStrings {
  private final static int ALPHARETTA_HASH = 797734992;
  private final static String EMPTY = "";
  private final static String ALPHARETTA = "Alpharetta";
  private final static String BIRMINGHAM = "Birmingham";
  private final static String SEPARATOR = ",";
  private final static String JOINED = "aaa,bbb,ccc,ddd,eee,fff,ggg";
  private final static String JOINED_NOSEP = "aaabbbcccdddeeefffggg";
  private final static String[] NAMES = new String[] {
      "aaa", "bbb", "ccc", "ddd", "eee", "fff", "ggg"
  };

  /**
   * Test the static equals method.
   */
  @Test
  public void testStaticEquals() {
    assertTrue(Strings.equals(null, null));
    assertTrue(Strings.equals(EMPTY, EMPTY));
    assertTrue(Strings.equals(ALPHARETTA, ALPHARETTA));
    assertTrue(Strings.equals(BIRMINGHAM, BIRMINGHAM));
    assertFalse(Strings.equals(null, EMPTY));
    assertFalse(Strings.equals(EMPTY, null));
    assertFalse(Strings.equals(null, ALPHARETTA));
    assertFalse(Strings.equals(ALPHARETTA, null));
    assertFalse(Strings.equals(EMPTY, ALPHARETTA));
    assertFalse(Strings.equals(ALPHARETTA, EMPTY));
    assertFalse(Strings.equals(ALPHARETTA, BIRMINGHAM));
    assertFalse(Strings.equals(BIRMINGHAM, ALPHARETTA));
  }

  /**
   * Test the hashCode method.
   */
  @Test
  public void testStaticHashCode() {
    assertEquals(1, Strings.hashCode(null));
    assertEquals(1, Strings.hashCode(EMPTY));
    assertEquals(ALPHARETTA_HASH, Strings.hashCode(ALPHARETTA));
  }

  /**
   * Test the basic join method.
   */
  @Test
  public void testJoin() {
    String result = Strings.join(SEPARATOR, true, NAMES);
    assertEquals(JOINED, result);
    result = Strings.join(SEPARATOR, true, NAMES[0], NAMES[1], NAMES[2],
	NAMES[3],
	NAMES[4], NAMES[5], NAMES[6]);
    assertEquals(JOINED, result);
  }

  /**
   * Test the join separator feature.
   */
  @Test
  public void testJoinSeparator() {
    assertEquals(JOINED_NOSEP, Strings.join(null, true, NAMES));
    assertEquals(JOINED_NOSEP, Strings.join(EMPTY, true, NAMES));
  }

  /**
   * Test the join skip nulls feature.
   */
  @Test
  public void testJoinSkipNulls() {
    assertEquals(JOINED, Strings.join(SEPARATOR, true, NAMES[0], null, NAMES[1],
				      EMPTY, NAMES[2], null, EMPTY, NAMES[3],
				      EMPTY, null, NAMES[4], NAMES[5], EMPTY,
				      NAMES[6], null));
  }

  /**
   * Test the pack method.
   */
  @Test
  public void testPack() {
    String[] packed = Strings.pack(ALPHARETTA, EMPTY, BIRMINGHAM, null,
	NAMES[0], null, EMPTY,
	NAMES[1], null, NAMES[2], EMPTY, null, NAMES[3], null);
    assertEquals(packed.length, 6);
    assertEquals(packed[0], ALPHARETTA);
    assertEquals(packed[1], BIRMINGHAM);
    assertEquals(packed[5], NAMES[3]);
    final String[] items = new String[] { ALPHARETTA, BIRMINGHAM, NAMES[0] };
    packed = Strings.pack(items);
    assertTrue(items == packed);
  }
}
