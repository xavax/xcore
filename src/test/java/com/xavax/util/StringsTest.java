//
// Copyright 2015 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//

package com.xavax.util;

import java.util.Comparator;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Test cases for the Strings utility class.
 */
public class StringsTest {
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
    assertEquals((String) null, null);
    assertEquals(EMPTY, EMPTY);
    assertEquals(ALPHARETTA, ALPHARETTA);
    assertEquals(BIRMINGHAM, BIRMINGHAM);
    assertNotEquals(null, EMPTY);
    assertNotEquals(EMPTY, null);
    assertNotEquals(null, ALPHARETTA);
    assertNotEquals(ALPHARETTA, null);
    assertNotEquals(EMPTY, ALPHARETTA);
    assertNotEquals(ALPHARETTA, EMPTY);
    assertNotEquals(ALPHARETTA, BIRMINGHAM);
    assertNotEquals(BIRMINGHAM, ALPHARETTA);
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
    assertSame(items, packed);
  }

  /**
   * Test the comparator method.
   */
  @Test
  public void testComparator() {
    final Comparator<String> comparator = Strings.comparator();
    assertTrue(comparator.compare(ALPHARETTA, BIRMINGHAM) < 0);
    assertTrue(comparator.compare(BIRMINGHAM, ALPHARETTA) > 0);
    assertTrue(comparator.compare(null, BIRMINGHAM) < 0);
    assertTrue(comparator.compare(null, null) == 0);
  }
}
