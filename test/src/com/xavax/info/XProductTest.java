package com.xavax.info;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class XProductTest {

  private final static String NAME = "XBacon";
  private final static String COMPANY = " Yoyodyne Propulsion";
  private final static String TEMPLATE = "Copyright %s" + COMPANY;
  private final static String COPYRIGHT1 = "Copyright 2001" + COMPANY;
  private final static String COPYRIGHT2 = "Copyright 2001, 2027" + COMPANY;
  private final static String VERSION = NAME + " 1.2.3";

  private XProduct product;

  @BeforeMethod
  public void setup() {
    product = new XProduct(NAME, 1, 2, 3, 2001, 2027, TEMPLATE);
  }

  @Test
  public void banner() {
    assertEquals(product.banner(), VERSION + "\n\n" + COPYRIGHT2);
  }

  @Test
  public void copyright() {
    assertEquals(product.copyright(), COPYRIGHT2);
    product = new XProduct(NAME, 1, 2, 3, 2001, 0, TEMPLATE);
    assertEquals(product.copyright(), COPYRIGHT1);
  }

  @Test
  public void version() {
    assertEquals(product.version(), VERSION);
  }
}
