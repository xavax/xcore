package com.xavax.util;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import com.xavax.tools.test.Joiner;

import static org.testng.Assert.*;

/**
 * Test cases for the Varargs class.
 */
public class TestVarargs {
  private final static int CAPACITY = 32;
  private final static String FLATTENED = "[A1, A2, A3, B1, B2, B3]";
  private final static String PARAMA1 = "A1";
  private final static String PARAMA2 = "A2";
  private final static String PARAMA3 = "A3";
  private final static String PARAMB1 = "B1";
  private final static String PARAMB2 = "B2";
  private final static String PARAMB3 = "B3";
  private final static String[] APARAMS = new String[] { PARAMA1, PARAMA2, PARAMA3 };
  private final static String[] BPARAMS = new String[] { PARAMB1, PARAMB2, PARAMB3 };
  private final static List<String> ALIST = Arrays.asList(APARAMS);
  private final static List<String> BLIST = Arrays.asList(BPARAMS);

  /**
   * Test the constructors and getInstance methods.
   */
  @Test
  public void testConstructors() {
    Varargs varargs = new Varargs();
    assertNotNull(varargs);
    varargs = new Varargs(CAPACITY);
    assertNotNull(varargs);
    varargs = Varargs.getInstance();
    assertNotNull(varargs);
    varargs = Varargs.getInstance(CAPACITY);
    assertNotNull(varargs);
  }

  @Test
  public void testAppendObject() {
    final Object[] args =
	Varargs.getInstance().append(PARAMA1).append(PARAMB1).toArray();
    final String output = Joiner.join(args);
    assertEquals(output, "[A1, B1]");
  }

  @Test
  public void appendArray() {
    final Object[] args =
	Varargs.getInstance().append(APARAMS).append(BPARAMS).toArray();
    final String output = Joiner.join(args);
    assertEquals(output, "[[A1, A2, A3], [B1, B2, B3]]");
  }

  @Test
  public void appendCollection() {
    final Object[] args =
	Varargs.getInstance().append(ALIST).append(BLIST).toArray();
    final String output = Joiner.join(args);
    assertEquals(output, "[{A1, A2, A3}, {B1, B2, B3}]");
  }

  @Test
  public void flattenArray() {
    final Object[] args =
	Varargs.getInstance().flatten(APARAMS).flatten(BPARAMS).toArray();
    final String output = Joiner.join(args);
    assertEquals(output, FLATTENED);
  }

  @Test
  public void flattenCollection() {
    final Object[] args =
	Varargs.getInstance().flatten(ALIST).flatten(BLIST).toArray();
    final String output = Joiner.join(args);
    assertEquals(output, FLATTENED);
  }

}
