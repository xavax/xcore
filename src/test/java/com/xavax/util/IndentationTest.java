package com.xavax.util;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import static org.junit.Assert.*;
import static com.xavax.util.Constants.EMPTY_STRING;

public class IndentationTest {
  private final static String INDENT1 = "\t";
  private final static String INDENT2 = "  ";

  @Test
  public void testIndent() {
    final Map<String,Indentation> map = Indentation.getMap();
    assertNotNull(map);
    assertEquals(0, map.size());
    final Indentation indenter1 = Indentation.getInstance(INDENT1);
    assertNotNull(indenter1);
    assertEquals(1, map.size());
    Indentation indenter = Indentation.getInstance(INDENT1);
    assertSame(indenter1, indenter);
    assertEquals(INDENT1, indenter1.getIndentString());
    assertNotNull(indenter1.getBuilder());
    List<String> list = indenter1.getList();
    assertNotNull(list);
    assertEquals(1, list.size());
    String indent = indenter1.indent(0);
    assertEquals(EMPTY_STRING, indent);
    indent = indenter1.indent(1);
    assertEquals(INDENT1, indent);
    indent = indenter1.indent(10);
    assertEquals(10, indent.length());
    assertEquals(11, list.size());
    indent = indenter1.indent(5);
    assertEquals(5, indent.length());
    assertEquals(11, list.size());
    final Indentation indenter2 = Indentation.getInstance(INDENT2);
    assertNotNull(indenter2);
    assertEquals(2, map.size());
    indenter = Indentation.getInstance(INDENT2);
    assertSame(indenter2, indenter);
    list = indenter2.getList();
    assertNotNull(list);
    assertEquals(1, list.size());
    indent = indenter2.indent(0);
    assertEquals(EMPTY_STRING, indent);
    indent = indenter2.indent(1);
    assertEquals(INDENT2, indent);
    indent = indenter2.indent(10);
    assertEquals(20, indent.length());
    assertEquals(11, list.size());
    indent = indenter2.indent(5);
    assertEquals(10, indent.length());
    assertEquals(11, list.size());
    indenter2.indent((int) Long.SIZE + 10);
    assertEquals((int) Long.SIZE, list.size());
  }

}
