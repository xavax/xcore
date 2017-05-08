package com.xavax.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.testng.annotations.Test;

import com.xavax.test.Joiner;

import static org.testng.Assert.*;

/**
 * Test cases for the Varargs class.
 */
public class VarargsTest {
  private final static int CAPACITY = 32;
  private final static String FLATTENED = "[A1, A2, A3, B1, B2, B3]";
  private final static String NESTED1 = "[[A1, A2, A3], [B1, B2, B3]]";
  private final static String NESTED2 = "[{A1, A2, A3}, {B1, B2, B3}]";
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
    varargs = Varargs.create();
    assertNotNull(varargs);
    varargs = Varargs.create(CAPACITY);
    assertNotNull(varargs);
  }

  /**
   * Test the append(Object) method.
   */
  @Test
  public void testAppendObject() {
    final Object[] args =
	Varargs.create().append(PARAMA1).append(PARAMB1).toArray();
    final String output = Joiner.join(args);
    assertEquals(output, "[A1, B1]");
  }

  /**
   * Test the append(Object[]) method.
   */
  @Test
  public void appendArray() {
    final Object[] args =
	Varargs.create()
	       .append((Object[]) APARAMS)
	       .append((Object[]) BPARAMS)
	       .toArray();
    final String output = Joiner.join(args);
    assertEquals(output, NESTED1);
  }

  /**
   * Test the append(Collection<?>) method.
   */
  @Test
  public void appendCollection() {
    final Object[] args =
	Varargs.create()
	       .append(ALIST)
	       .append(BLIST)
	       .toArray();
    final String output = Joiner.join(args);
    assertEquals(output, NESTED2);
    
  }

  /**
   * Test the flatten(Object[]) method.
   */
  @Test
  public void flattenArray() {
    final Object[] args =
	Varargs.create()
	       .flatten((Object[]) null)
	       .flatten((Object[]) APARAMS)
	       .flatten((Object[]) BPARAMS)
	       .toArray();
    final String output = Joiner.join(args);
    assertEquals(output, FLATTENED);
  }

  /**
   * Test the flatten(Collection<?>) method.
   */
  @Test
  public void flattenCollection() {
    final Object[] args =
	Varargs.create()
	       .flatten(ALIST)
	       .flatten(BLIST)
	       .flatten((Collection<String>) null)
	       .toArray();
    final String output = Joiner.join(args);
    assertEquals(output, FLATTENED);
  }

}
