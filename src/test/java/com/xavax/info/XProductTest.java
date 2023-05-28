//
// Copyright 2004, 2013 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//
package com.xavax.info;

import org.junit.Before;
import org.junit.Test;

import com.xavax.XCore;

import static org.junit.Assert.*;

/**
 * Test case for the XProduct class.
 *
 * @author alvitar@xavax.com
 */
public class XProductTest {

  private final static int MAJOR = 1;
  private final static int MINOR = 2;
  private final static int PATCH = 3;
  private final static String NAME = "XBacon";
  private final static String COMPANY = " Yoyodyne Propulsion";
  private final static String TEMPLATE = "Copyright %s" + COMPANY;
  private final static String COPYRIGHT1 = "Copyright 2001" + COMPANY;
  private final static String COPYRIGHT2 = "Copyright 2001, 2027" + COMPANY;
  private final static String VERSION = NAME + " " + MAJOR + "." + MINOR + "." + PATCH;
  private final static String XAVAX = "Xavax, Inc.";
  private final static String XCORE = "XCore";

  private XProduct product;

  /**
   * Test setup.
   */
  @Before
  public void setUp() {
    product = new XProduct(NAME, MAJOR, MINOR, PATCH, 2001, 2027, TEMPLATE);
  }

  /**
   * Test the banner method.
   */
  @Test
  public void testBanner() {
    assertEquals(product.banner(), VERSION + "\n\n" + COPYRIGHT2);
  }

  /**
   * Test the copyright method.
   */
  @Test
  public void testCopyright() {
    assertEquals(product.copyright(), COPYRIGHT2);
    assertEquals(product.copyright(), COPYRIGHT2);
    product = new XProduct(NAME, MAJOR, MINOR, PATCH, 2001, 0, TEMPLATE);
    assertEquals(product.copyright(), COPYRIGHT1);
    product = new XProduct(NAME, MAJOR, MINOR, PATCH, 2001, 0, null);
    assertTrue(product.copyright().contains(XAVAX));
  }

  /**
   * Test the version method.
   */
  @Test
  public void testVersion() {
    product = new XProduct(NAME, MAJOR, MINOR, PATCH, 2001, 2027, TEMPLATE);
    assertEquals(product.version(), VERSION);
    assertEquals(product.version(), VERSION);
    assertEquals(product.majorVersion(), MAJOR);
    assertEquals(product.minorVersion(), MINOR);
    assertEquals(product.patchLevel(), PATCH);
  }

  /**
   * Test the name method.
   */
  @Test
  public void testName() {
    assertEquals(product.name(), NAME);
  }

  /**
   * Test the XCore instance of XProduct.
   */
  @Test
  public void testXCore() {
    assertEquals(XCore.getProduct().name(), XCORE);
  }
}
