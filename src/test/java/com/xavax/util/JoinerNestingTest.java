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
@SuppressWarnings("PMD.SystemPrintln")
public class JoinerNestingTest {

  private final static String FORMAT = "depth: %d, tree: %s";
  private final static String NAME_FORMAT = "Node%d";
  // private final static String NODE = "Node";
  private final static String ROOT  = "root: ";
  private final static String LEFT  = "left: ";
  private final static String RIGHT = "right: ";
  private final static String VALUE = "value: ";
  private final static Value V01 = new Value(1);
  private final static Value V02 = new Value(2);
  private final static Value V03 = new Value(3);
  private final static Value V04 = new Value(4);
  private final static Value V05 = new Value(5);
  private final static Value V06 = new Value(6);
  private final static Value V07 = new Value(7);
  private final static Value V08 = new Value(8);
  private final static Value V09 = new Value(9);
  private final static Value V10 = new Value(10);
  private final static Value V11 = new Value(11);
  private final static Value V12 = new Value(12);
  private final static Value V13 = new Value(13);
  private final static Value V14 = new Value(14);
  private final static Value V15 = new Value(15);

  private final static TreeNode<Value> NODE1  = new TreeNode<>(V01);
  private final static TreeNode<Value> NODE3  = new TreeNode<>(V03);
  private final static TreeNode<Value> NODE2  = new TreeNode<>(V02, NODE1, NODE3);
  private final static TreeNode<Value> NODE5  = new TreeNode<>(V05);
  private final static TreeNode<Value> NODE7  = new TreeNode<>(V07);
  private final static TreeNode<Value> NODE6  = new TreeNode<>(V06, NODE5, NODE7);
  private final static TreeNode<Value> NODE4  = new TreeNode<>(V04, NODE2, NODE6);
  private final static TreeNode<Value> NODE9  = new TreeNode<>(V09);
  private final static TreeNode<Value> NODE11 = new TreeNode<>(V11);
  private final static TreeNode<Value> NODE10 = new TreeNode<>(V10, NODE9, NODE11);
  private final static TreeNode<Value> NODE13 = new TreeNode<>(V13);
  private final static TreeNode<Value> NODE15 = new TreeNode<>(V15);
  private final static TreeNode<Value> NODE14 = new TreeNode<>(V14, NODE13, NODE15);
  private final static TreeNode<Value> NODE12 = new TreeNode<>(V12, NODE10, NODE14);
  private final static TreeNode<Value> NODE8  = new TreeNode<>(V08, NODE4, NODE12);
  private final static Tree<Value> TREE = new Tree<>(NODE8);

  private final static String NODE_ELLIPSIS =
      LEFT + LBRACE + ELLIPSIS + RBRACE + SEPARATOR + RIGHT + LBRACE + ELLIPSIS + RBRACE;
  private final static String VALUE_ELLIPSIS =
      VALUE + LBRACE + ELLIPSIS + RBRACE;
  private final static String NO_CHILDREN =
      LEFT + INDICATOR + SEPARATOR + RIGHT + INDICATOR + RBRACE;
  private final static String EDGE_NODE =
      LBRACE + VALUE_ELLIPSIS + SEPARATOR + NO_CHILDREN;
  private final static String SUPPRESSED_NODE =
      LBRACE + VALUE_ELLIPSIS + SEPARATOR + NODE_ELLIPSIS + RBRACE;

  private final static String EXPECT_NODE1A =
      LBRACE + VALUE + V01 + SEPARATOR + NO_CHILDREN;
  private final static String EXPECT_NODE3A =
      LBRACE + VALUE + V03 + SEPARATOR +
      LEFT + INDICATOR + SEPARATOR + RIGHT + INDICATOR + RBRACE;
  private final static String EXPECT_NODE2A = 
      LBRACE + VALUE + V02 + SEPARATOR +
      LEFT + EXPECT_NODE1A + SEPARATOR + RIGHT + EXPECT_NODE3A + RBRACE;
  private final static String EXPECT_NODE2B = 
      LBRACE + VALUE + V02 + SEPARATOR +
      LEFT + EDGE_NODE + SEPARATOR + RIGHT + EDGE_NODE + RBRACE;

  private final static String EXPECT_NODE5A =
      LBRACE + VALUE + V05 + SEPARATOR + NO_CHILDREN;
  private final static String EXPECT_NODE7A =
      LBRACE + VALUE + V07 + SEPARATOR + NO_CHILDREN;
  private final static String EXPECT_NODE6A =
      LBRACE + VALUE + V06 + SEPARATOR +
      LEFT + EXPECT_NODE5A + SEPARATOR + RIGHT + EXPECT_NODE7A + RBRACE;
  private final static String EXPECT_NODE6B =
      LBRACE + VALUE + V06 + SEPARATOR +
      LEFT + EDGE_NODE + SEPARATOR + RIGHT + EDGE_NODE + RBRACE;
      
  private final static String EXPECT_NODE4A =
      LBRACE + VALUE + V04 + SEPARATOR +
      LEFT + EXPECT_NODE2A + SEPARATOR + RIGHT + EXPECT_NODE6A + RBRACE;
  private final static String EXPECT_NODE4B =
      LBRACE + VALUE + V04 + SEPARATOR +
      LEFT + EXPECT_NODE2B + SEPARATOR +
      RIGHT + EXPECT_NODE6B + RBRACE;
  private final static String EXPECT_NODE4C =
      LBRACE + VALUE + V04 + SEPARATOR +
      LEFT + SUPPRESSED_NODE + SEPARATOR + RIGHT + SUPPRESSED_NODE + RBRACE;

  private final static String EXPECT_NODE9A =
      LBRACE + VALUE + V09 + SEPARATOR + NO_CHILDREN;
  private final static String EXPECT_NODE11A =
      LBRACE + VALUE + V11 + SEPARATOR + NO_CHILDREN;
  private final static String EXPECT_NODE10A = 
      LBRACE + VALUE + V10 + SEPARATOR +
      LEFT + EXPECT_NODE9A + SEPARATOR + RIGHT + EXPECT_NODE11A + RBRACE;
  private final static String EXPECT_NODE10B =
      LBRACE + VALUE + V10 + SEPARATOR +
      LEFT + EDGE_NODE + SEPARATOR + RIGHT + EDGE_NODE + RBRACE;

  private final static String EXPECT_NODE13A =
      LBRACE + VALUE + V13 + SEPARATOR + NO_CHILDREN;
  private final static String EXPECT_NODE15A =
      LBRACE + VALUE + V15 + SEPARATOR + NO_CHILDREN;
  private final static String EXPECT_NODE14A = 
      LBRACE + VALUE + V14 + SEPARATOR +
      LEFT + EXPECT_NODE13A + SEPARATOR + RIGHT + EXPECT_NODE15A + RBRACE;
  private final static String EXPECT_NODE14B =
      LBRACE + VALUE + V14 + SEPARATOR +
      LEFT + EDGE_NODE + SEPARATOR + RIGHT + EDGE_NODE + RBRACE;

  private final static String EXPECT_NODE12A =
      LBRACE + VALUE + V12 + SEPARATOR +
      LEFT + EXPECT_NODE10A + SEPARATOR + RIGHT + EXPECT_NODE14A + RBRACE;
  private final static String EXPECT_NODE12B =
      LBRACE + VALUE + V12 + SEPARATOR +
      LEFT + EXPECT_NODE10B + SEPARATOR +
      RIGHT + EXPECT_NODE14B + RBRACE;
  private final static String EXPECT_NODE12C =
      LBRACE + VALUE + V12 + SEPARATOR +
      LEFT + SUPPRESSED_NODE + SEPARATOR + RIGHT + SUPPRESSED_NODE + RBRACE;

  private final static String EXPECT_NODE8A =
      LBRACE + VALUE + V08 + SEPARATOR +
      LEFT + EXPECT_NODE4A + SEPARATOR +
      RIGHT + EXPECT_NODE12A + RBRACE;
  private final static String EXPECT_NODE8B =
      LBRACE + VALUE + V08 + SEPARATOR +
      LEFT + EXPECT_NODE4B + SEPARATOR +
      RIGHT + EXPECT_NODE12B + RBRACE;
  private final static String EXPECT_NODE8C =
      LBRACE + VALUE + V08 + SEPARATOR +
      LEFT + EXPECT_NODE4C + SEPARATOR +
      RIGHT + EXPECT_NODE12C + RBRACE;
  private final static String EXPECT_NODE8D =
      LBRACE + VALUE + V08 + SEPARATOR +
      LEFT + SUPPRESSED_NODE + SEPARATOR + RIGHT + SUPPRESSED_NODE + RBRACE;

  private final static String EXPECT_TREE   = LBRACE + ROOT + EXPECT_NODE8A + RBRACE;
  private final static String EXPECT_TREE_A = ROOT + EXPECT_NODE8A;
  private final static String EXPECT_TREE_B = ROOT + EXPECT_NODE8B;
  private final static String EXPECT_TREE_C = ROOT + EXPECT_NODE8C;
  private final static String EXPECT_TREE_D = ROOT + EXPECT_NODE8D;
  private final static String EXPECT_TREE_E = ROOT + SUPPRESSED_NODE;
  private final static String EXPECT_TREE_F = ROOT + LBRACE + ELLIPSIS + RBRACE;

  /**
   * Test max depth..
   */
  @Test
  public void testMaxDepth() {
    String output = NODE1.toString();
    assertEquals(output, EXPECT_NODE1A);
    output = NODE3.toString();
    assertEquals(output, EXPECT_NODE3A);
    output = NODE2.toString();
    assertEquals(output, EXPECT_NODE2A);

    output = NODE5.toString();
    assertEquals(output, EXPECT_NODE5A);
    output = NODE7.toString();
    assertEquals(output, EXPECT_NODE7A);
    output = NODE6.toString();
    assertEquals(output, EXPECT_NODE6A);

    output = NODE9.toString();
    assertEquals(output, EXPECT_NODE9A);
    output = NODE11.toString();
    assertEquals(output, EXPECT_NODE11A);
    output = NODE10.toString();
    assertEquals(output, EXPECT_NODE10A);

    output = NODE13.toString();
    assertEquals(output, EXPECT_NODE13A);
    output = NODE15.toString();
    assertEquals(output, EXPECT_NODE15A);
    output = NODE14.toString();
    assertEquals(output, EXPECT_NODE14A);

    output = NODE4.toString();
    assertEquals(output, EXPECT_NODE4A);
    output = NODE12.toString();
    assertEquals(output, EXPECT_NODE12A);
    output = NODE8.toString();
    assertEquals(output, EXPECT_NODE8A);
    output = TREE.toString();
    assertEquals(output, EXPECT_TREE);

    
    output = Joiner.create()
		   .withMaxDepth(6)
		   .append(TREE)
		   .toString();
    System.out.println(String.format(FORMAT, 6, output));
    assertEquals(output, EXPECT_TREE_A);

    output = Joiner.create()
	   	   .withMaxDepth(5)
	   	   .append(TREE)
	   	   .toString();
    System.out.println(String.format(FORMAT, 5, output));
    assertEquals(output, EXPECT_TREE_B); 
 
    output = Joiner.create()
	   	   .withMaxDepth(4)
	   	   .append(TREE)
	   	   .toString();
    System.out.println(String.format(FORMAT, 4, output));
    assertEquals(output, EXPECT_TREE_C);

    output = Joiner.create()
		   .withMaxDepth(3)
		   .append(TREE)
		   .toString();
    System.out.println(String.format(FORMAT, 3, output));
    assertEquals(output, EXPECT_TREE_D);

    output = Joiner.create()
		   .withMaxDepth(2)
		   .append(TREE)
		   .toString();
    System.out.println(String.format(FORMAT, 2, output));
    assertEquals(output, EXPECT_TREE_E);

    output = Joiner.create()
	   .withMaxDepth(1)
	   .append(TREE)
	   .toString();
    System.out.println(String.format(FORMAT, 1, output));
    assertEquals(output, EXPECT_TREE_F);

output = Joiner.create()
		   .append(TREE)
		   .toString();
    System.out.println(String.format(FORMAT, 0, output));
    assertEquals(output, EXPECT_TREE_A); 
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
      joiner.append("value", value)
      	    .append("left", left)
      	    .append("right", right);
      return joiner;
    }
  }

  public static class Value extends AbstractJoinableObject {
    private final Long value;
    private final String name;
  
    public Value(final long value) {
      this.value = Long.valueOf(value);
      this.name = String.format(NAME_FORMAT, value);
    }

    /**
     * Output this object to the specified joiner.
     *
     * @param joiner  the joiner to use for output.
     * @return this joiner.
     */
    @Override
    protected Joiner doJoin(final Joiner joiner) {
      joiner.append("name", name)
      	    .append("value", value);
      return joiner;
    }
  }
}
