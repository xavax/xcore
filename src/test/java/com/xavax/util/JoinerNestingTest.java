//
// Copyright 2017 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//
package com.xavax.util;

import static org.testng.Assert.*;
import static com.xavax.util.JoinerTestConstants.*;

import org.testng.annotations.Test;

/**
 * Test cases for testing the Joiner nesting feature.
 */
public class JoinerNestingTest {

  private final static String FORMAT = "depth: %d, tree: %s";
  private final static String ROOT  = "root: ";
  private final static String LEFT  = "left: ";
  private final static String RIGHT = "right: ";
  private final static String VALUE = "value: ";
  private final static String V01 = "1";
  private final static String V02 = "2";
  private final static String V03 = "3";
  private final static String V04 = "4";
  private final static String V05 = "5";
  private final static String V06 = "6";
  private final static String V07 = "7";
  private final static String V08 = "8";
  private final static String V09 = "9";
  private final static String V10 = "10";
  private final static String V11 = "11";
  private final static String V12 = "12";
  private final static String V13 = "13";
  private final static String V14 = "14";
  private final static String V15 = "15";

  private final static String EXPECT_NODE1A =
      VALUE + V01 + SEPARATOR + LEFT + INDICATOR + SEPARATOR + RIGHT + INDICATOR;
  private final static String EXPECT_NODE3A =
      VALUE + V03 + SEPARATOR + LEFT + INDICATOR + SEPARATOR + RIGHT + INDICATOR;
  private final static String EXPECT_NODE2A = VALUE + V02 + SEPARATOR
      + LEFT + LPAREN + ELLIPSIS + RPAREN + SEPARATOR
      + RIGHT + LPAREN + ELLIPSIS + RPAREN;
  private final static String EXPECT_NODE2B = VALUE + V02 + SEPARATOR
      + LEFT + LPAREN + EXPECT_NODE1A + RPAREN + SEPARATOR
      + RIGHT + LPAREN + EXPECT_NODE3A + RPAREN;

  private final static String EXPECT_NODE5A =
      VALUE + V05 + SEPARATOR + LEFT + INDICATOR + SEPARATOR + RIGHT + INDICATOR;
  private final static String EXPECT_NODE7A =
      VALUE + V07 + SEPARATOR + LEFT + INDICATOR + SEPARATOR + RIGHT + INDICATOR;
  private final static String EXPECT_NODE6A = VALUE + V06 + SEPARATOR
      + LEFT + LPAREN + ELLIPSIS + RPAREN + SEPARATOR
      + RIGHT + LPAREN + ELLIPSIS + RPAREN;
  private final static String EXPECT_NODE6B = VALUE + V06 + SEPARATOR
      + LEFT + LPAREN + EXPECT_NODE5A + RPAREN + SEPARATOR
      + RIGHT + LPAREN + EXPECT_NODE7A + RPAREN;

  private final static String EXPECT_NODE4A = VALUE + V04 + SEPARATOR
      + LEFT + LPAREN + ELLIPSIS + RPAREN + SEPARATOR
      + RIGHT + LPAREN + ELLIPSIS + RPAREN;
  private final static String EXPECT_NODE4B = VALUE + V04 + SEPARATOR
      + LEFT + LPAREN + EXPECT_NODE2A + RPAREN + SEPARATOR
      + RIGHT + LPAREN + EXPECT_NODE6A + RPAREN;
  private final static String EXPECT_NODE4C = VALUE + V04 + SEPARATOR
      + LEFT + LPAREN + EXPECT_NODE2B + RPAREN + SEPARATOR
      + RIGHT + LPAREN + EXPECT_NODE6B + RPAREN;

  private final static String EXPECT_NODE9A =
      VALUE + V09 + SEPARATOR + LEFT + INDICATOR + SEPARATOR + RIGHT + INDICATOR;
  private final static String EXPECT_NODE11A =
      VALUE + V11 + SEPARATOR + LEFT + INDICATOR + SEPARATOR + RIGHT + INDICATOR;
  private final static String EXPECT_NODE10A = VALUE + V10 + SEPARATOR
      + LEFT + LPAREN + ELLIPSIS + RPAREN + SEPARATOR
      + RIGHT + LPAREN + ELLIPSIS + RPAREN;
  private final static String EXPECT_NODE10B = VALUE + V10 + SEPARATOR
      + LEFT + LPAREN + EXPECT_NODE9A + RPAREN + SEPARATOR
      + RIGHT + LPAREN + EXPECT_NODE11A + RPAREN;

  private final static String EXPECT_NODE13A =
      VALUE + V13 + SEPARATOR + LEFT + INDICATOR + SEPARATOR + RIGHT + INDICATOR;
  private final static String EXPECT_NODE15A =
      VALUE + V15 + SEPARATOR + LEFT + INDICATOR + SEPARATOR + RIGHT + INDICATOR;
  private final static String EXPECT_NODE14A = VALUE + V14 + SEPARATOR
      + LEFT + LPAREN + ELLIPSIS + RPAREN + SEPARATOR
      + RIGHT + LPAREN + ELLIPSIS + RPAREN;
  private final static String EXPECT_NODE14B = VALUE + V14 + SEPARATOR
      + LEFT + LPAREN + EXPECT_NODE13A + RPAREN + SEPARATOR
      + RIGHT + LPAREN + EXPECT_NODE15A + RPAREN;

  private final static String EXPECT_NODE12A = VALUE + V12 + SEPARATOR
      + LEFT + LPAREN + ELLIPSIS + RPAREN + SEPARATOR
      + RIGHT + LPAREN + ELLIPSIS + RPAREN;
  private final static String EXPECT_NODE12B = VALUE + V12 + SEPARATOR
      + LEFT + LPAREN + EXPECT_NODE10A + RPAREN + SEPARATOR
      + RIGHT + LPAREN + EXPECT_NODE14A + RPAREN;
  private final static String EXPECT_NODE12C = VALUE + V12 + SEPARATOR
      + LEFT + LPAREN + EXPECT_NODE10B + RPAREN + SEPARATOR
      + RIGHT + LPAREN + EXPECT_NODE14B + RPAREN;

  private final static String EXPECT_NODE8A = VALUE + V08 + SEPARATOR
      + LEFT + LPAREN + ELLIPSIS + RPAREN + SEPARATOR
      + RIGHT + LPAREN + ELLIPSIS + RPAREN;
  private final static String EXPECT_NODE8B = VALUE + V08 + SEPARATOR
      + LEFT + LPAREN + EXPECT_NODE4A + RPAREN + SEPARATOR
      + RIGHT + LPAREN + EXPECT_NODE12A + RPAREN;
  private final static String EXPECT_NODE8C = VALUE + V08 + SEPARATOR
      + LEFT + LPAREN + EXPECT_NODE4B + RPAREN + SEPARATOR
      + RIGHT + LPAREN + EXPECT_NODE12B + RPAREN;
  private final static String EXPECT_NODE8D = VALUE + V08 + SEPARATOR
      + LEFT + LPAREN + EXPECT_NODE4C + RPAREN + SEPARATOR
      + RIGHT + LPAREN + EXPECT_NODE12C + RPAREN;

  private final static String EXPECT_TREEA = ROOT + LPAREN + ELLIPSIS + RPAREN;
  private final static String EXPECT_TREEB = ROOT + LPAREN + EXPECT_NODE8A + RPAREN;
  private final static String EXPECT_TREEC = ROOT + LPAREN + EXPECT_NODE8B + RPAREN;
  private final static String EXPECT_TREED = ROOT + LPAREN + EXPECT_NODE8C + RPAREN;
  private final static String EXPECT_TREEE = ROOT + LPAREN + EXPECT_NODE8D + RPAREN;

  private final static TreeNode<Long> NODE1  = new TreeNode<>(new Long(1));
  private final static TreeNode<Long> NODE3  = new TreeNode<>(new Long(3));
  private final static TreeNode<Long> NODE2  = new TreeNode<>(new Long(2), NODE1, NODE3);
  private final static TreeNode<Long> NODE5  = new TreeNode<>(new Long(5));
  private final static TreeNode<Long> NODE7  = new TreeNode<>(new Long(7));
  private final static TreeNode<Long> NODE6  = new TreeNode<>(new Long(6), NODE5, NODE7);
  private final static TreeNode<Long> NODE4  = new TreeNode<>(new Long(4), NODE2, NODE6);
  private final static TreeNode<Long> NODE9  = new TreeNode<>(new Long(9));
  private final static TreeNode<Long> NODE11 = new TreeNode<>(new Long(11));
  private final static TreeNode<Long> NODE10 = new TreeNode<>(new Long(10), NODE9, NODE11);
  private final static TreeNode<Long> NODE13 = new TreeNode<>(new Long(13));
  private final static TreeNode<Long> NODE15 = new TreeNode<>(new Long(15));
  private final static TreeNode<Long> NODE14 = new TreeNode<>(new Long(14), NODE13, NODE15);
  private final static TreeNode<Long> NODE12 = new TreeNode<>(new Long(12), NODE10, NODE14);
  private final static TreeNode<Long> NODE8  = new TreeNode<>(new Long(8), NODE4, NODE12);
  private final static Tree<Long> TREE = new Tree<>(NODE8);

  /**
   * Test max depth..
   */
  @Test
  public void testMaxDepth() {
    String output = NODE1.toString();
    assertEquals(output, EXPECT_NODE1A);
    output = NODE2.toString();
    assertEquals(output, EXPECT_NODE2B);
    output = NODE4.toString();
    assertEquals(output, EXPECT_NODE4C);
    output = NODE8.toString();
    assertEquals(output, EXPECT_NODE8D);
    output = Joiner.create()
		   .withMaxDepth(1)
		   .append(TREE)
		   .toString();
    System.out.println(String.format(FORMAT, 1, output));
    assertEquals(output, EXPECT_TREEA);
    output = Joiner.create()
	   	   .withMaxDepth(2)
	   	   .append(TREE)
	   	   .toString();
    System.out.println(String.format(FORMAT, 2, output));
    assertEquals(output, EXPECT_TREEB); 
    output = Joiner.create()
	   	   .withMaxDepth(3)
	   	   .append(TREE)
	   	   .toString();
    System.out.println(String.format(FORMAT, 3, output));
    assertEquals(output, EXPECT_TREEC);    
    output = Joiner.create()
		   .withMaxDepth(4)
		   .append(TREE)
		   .toString();
    System.out.println(String.format(FORMAT, 4, output));
    assertEquals(output, EXPECT_TREED);
    output = Joiner.create()
		   .withMaxDepth(5)
		   .append(TREE)
		   .toString();
    System.out.println(String.format(FORMAT, 5, output));
    assertEquals(output, EXPECT_TREEE);
    output = Joiner.create()
		   .append(TREE)
		   .toString();
    System.out.println(String.format(FORMAT, 0, output));
    assertEquals(output, EXPECT_TREEE); 
  }

  /**
   * A tree class for testing max depth.
   */
  private static class Tree<T> extends AbstractJoinableObject {
    private final TreeNode<T> root;

    /**
     * Construct a Tree.
     *
     * @param root  the root node.
     */
    public Tree(final TreeNode<T> root) {
      this.root = root;
    }

    /**
     * Output this object to the specified joiner.
     *
     * @param joiner  the joiner to use for output.
     * @return this joiner.
     */
    @Override
    protected Joiner doJoin(final Joiner joiner) {
      joiner.append("root", root);
      return joiner;
    }
  }

  /**
   * TreeNode represents a node in a binary tree.
   */
  private static class TreeNode<T> extends AbstractJoinableObject {
    private final T value;
    private final TreeNode<T> left;
    private final TreeNode<T> right;

    /**
     * Construct a TreeNode.
     *
     * @param value  the node value;
     */
    public TreeNode(final T value) {
      this(value, null, null);
    }

    /**
     * Construct a TreeNode.
     *
     * @param value  the node value;
     * @param left   the left node;
     * @param right  the right node;
     */
    public TreeNode(final T value, final TreeNode<T> left, final TreeNode<T> right) {
      this.value = value;
      this.left  = left;
      this.right = right;
    }

    /**
     * Output this object to the specified joiner.
     *
     * @param joiner  the joiner to use for output.
     * @return this joiner.
     */
    @Override
    protected Joiner doJoin(final Joiner joiner) {
      joiner.appendField("value", value.toString())
      	    .append("left", left)
      	    .append("right", right);
      return joiner;
    }
  }

}
