package com.xavax.util;

import java.util.List;
import java.util.Map;

import static com.xavax.util.CollectionFactory.*;
import static com.xavax.util.Constants.EMPTY_STRING;

/**
 * Indentation manages indentation strings.
 */
public class Indentation {
  private final static int MAX_LEVELS = Long.SIZE;
  private final static Map<String,Indentation> map = hashMap();

  private final StringBuilder builder;
  private final List<String> indentations;
  private final String indentString;

  /**
   * Construct an indentation.
   *
   * @param indentString  the string to add for each indentation level.
   */
  private Indentation(final String indentString) {
    this.indentString = indentString;
    builder = new StringBuilder();
    indentations = arrayList(MAX_LEVELS);
    indentations.add(EMPTY_STRING);
  }

  /**
   * Returns the proper indentation for the specified level.
   *
   * @param level  the level of indentation.
   * @return the proper indentation for the specified level.
   */
  public String indent(final int level) {
    final int max = level < MAX_LEVELS ? level : MAX_LEVELS - 1;
    if ( indentations.size() <= max ) {
      for ( int i = indentations.size(); i <= max; ++i ) {
	builder.append(indentString);
	indentations.add(builder.toString());
      }
    }
    return indentations.get(max);
  }

  /**
   * Returns the Indentation for the specified indent string.
   * If an Indentation already exists for the specified indent
   * string it will be shared.
   *
   * @param indentString  the string to add for each indentation level.
   * @return an Indentation for the specified indent string.
   */
  public static Indentation getInstance(final String indentString) {
    Indentation indentation = null;
    synchronized ( map ) {
      indentation = map.get(indentString);
      if ( indentation == null ) {
	indentation = new Indentation(indentString);
	map.put(indentString, indentation);
      }
    }
    return indentation;
  }

  /**
   * Returns the indent string.
   *
   * @return the indent string.
   */
  String getIndentString() {
    return indentString;
  }

  /**
   * Returns the builder.
   * FOR TESTING
   *
   * @return the builder.
   */
  StringBuilder getBuilder() {
    return builder;
  }

  /**
   * Returns the list of indentations.
   * FOR TESTING
   *
   * @return the list of indentations.
   */
  List<String> getList() {
    return indentations;
  }

  /**
   * Returns the map of Indentations.
   * FOR TESTING
   *
   * @return
   */
  static Map<String,Indentation> getMap() {
    return map;
  }
}
