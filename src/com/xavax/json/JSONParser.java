//
// Copyright 2011 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//
package com.xavax.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import com.xavax.util.CollectionFactory;

/**
 * JSONParser is a parser for strings in JSON format.
 */
public class JSONParser {
  /**
   * Construct a JSONParser.
   */
  public JSONParser() {
    this.source = null;
    this.reader = null;
    init();
  }

  /**
   * Construct a JSONParser with the specified reader and source.
   *
   * @param reader
   * @param source
   */
  public JSONParser(final Reader reader, final String source) {
    this.source = source;
    this.reader = reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
    init();
  }

  private void init() {
    line = 0;
    level = 0;
    cursor = 0;
    length = 0;
    array = null;
    lineBuffer = null;
    errors = null;
  }

  static public JSON parse(final Reader reader, final String source) {
    JSON result = null;
    if ( reader != null ) {
      final JSONParser parser = new JSONParser(reader, source);
      result = parser.parse();
    }
    return result;
  }

  private void init(final String input) {
    source = null;
    reader = new BufferedReader(new StringReader(input));
    init();
  }

  public JSON parse(final String input) {
    init(input);
    return parse();
  }

  public JSON parse() {
    level = 0;
    json = new JSON();
    try {
      if ( expect('{', true) ) {
	parseItems(json);
      }
      if ( level != 0 ) {
	addError("unmatched braces or brackets");
      }
    }
    catch (EndOfInputException e) {
      final String msg = "unexpected end of input";
      addError(msg);
    }
    if ( hasNext() ) {
      addError("unexpected characters after closing brace");
    }
    return json;
  }

  public JSONArray parseArray(final String input) {
    init(input);
    level = 0;
    final JSONArray list = new JSONArray();
    try {
      if ( expect('[', true) ) {
	parseArrayItems(list);
      }
      if ( level != 0 ) {
	addError("unmatched braces or brackets");
      }
    }
    catch (EndOfInputException e) {
      final String msg = "unexpected end of input";
      addError(msg);
    }
    if ( hasNext() ) {
      addError("unexpected characters after closing brace");
    }
    return list;
  }

  private void parseItems(JSON map) {
    boolean first = true;
    while ( hasNext() ) {
      if ( first ) {
	first = false;
	if ( scanFor('}') ) {
	  break;
	}
      }
      parseItem(map);
      final char input = next(true);
      if ( input == '}' ) {
	break;
      }
      else if ( input != ',' ) {
	expected(',', input);
	break;
      }
    }
  }

  private boolean parseItem(final JSON map) {
    boolean result = false;
    final String key = parseKey();
    if ( key == null || key.equals("") ) {
      expected("identifier", peek());
    }
    else {
      if ( expect(':', true) ) {
	final Object value = parseValue();
	map.put(key, value);
	result = true;
      }
      else {
	skipToNextItem(true);
      }
    }
    return result;
  }

  private String parseKey() {
    String key = null;
    final char input = next(true);
    if ( input == '"' || input == '\'' ) {
      key = parseIdentifier();
      expect(input, false);
    }
    else {
      pushback();
      key = parseIdentifier();
    }
    return key;
  }

  private String parseIdentifier() {
    final StringBuilder builder = new StringBuilder();
    char input = next(false);
    if ( Character.isLetter(input) || input == '_' ) {
      builder.append(input);
      while ( hasNext() ) {
	input = next(false);
	if ( Character.isLetterOrDigit(input) || input == '_' ) {
	  builder.append(input);
	}
	else {
	  pushback();
	  break;
	}
      }
    }
    else {
      pushback();
    }
    return builder.toString();
  }

  private Object parseValue() {
    Object result = null;
    if ( hasNext() ) {
      char input = next(true);
      switch ( input ) {
      case '"':
      case '\'':
	result = parseString(input);
	break;
      case '{':
	final JSON map = new JSON();
	++level;
	parseItems(map);
	--level;
	result = map;
	break;
      case '}':
      case ',':
	pushback();
	expected("value", input);
	break;
      case '[':
	final JSONArray list = new JSONArray();
	++level;
	parseArrayItems(list);
	--level;
	result = list;
	break;
      case '-':
      case '.':
	pushback();
	result = parseNumber();
	break;
      default:
	if ( Character.isDigit(input) ) {
	  pushback();
	  result = parseNumber();
	}
	else if ( Character.isLetter(input) ) {
	  final int mark = cursor - 1;
	  while ( hasNext() ) {
	    input = next(false);
	    if ( !Character.isLetter(input) ) {
	      pushback();
	      break;
	    }
	  }
	  String word = lineBuffer.substring(mark, cursor);
	  if ( ignoreCase ) {
	    word = word.toLowerCase();
	  }
	  if ( word.equals("null") ) {
	    result = null;
	  }
	  else if ( word.equals("true") ) {
	    result = Boolean.TRUE;
	  }
	  else if ( word.equals("false") ) {
	    result = Boolean.FALSE;
	  }
	  else {
	    expected("true, false, or null", word);
	  }
	}
	else {
	  expected("value", input);
	}
	break;
      }
    }
    return result;
  }

  private void parseArrayItems(final JSONArray list) {
    while ( hasNext() ) {
      if ( scanFor(']') ) {
	break;
      }
      final Object value = parseValue();
      list.add(value);
      final char input = next(true);
      if ( input == ']' ) {
	break;
      }
      else if ( input != ',' ) {
	expected(',', input);
	break;
      }
    }
  }

  private Object parseNumber() {
    boolean done = false;
    boolean isDouble = false;
    boolean leadingZero = false;
    int state = AcceptDigitSignRadix;
    final int mark = cursor;
    final StringBuilder builder = new StringBuilder();
    while ( !done && hasNext() ) {
      // Skip whitespace only for first character of number.
      final char input = next(state == AcceptDigitSignRadix);
      switch ( state ) {
      case AcceptDigitSignRadix:
	if ( Character.isDigit(input) ) {
	  state = AccumulateInitialDigits;
	  if ( input == '0' ) {
	    leadingZero = true;
	  }
	}
	else if ( input == '-' ) {
	  state = AcceptDigitRadix;
	}
	else if ( input == '.' ) {
	  state = AccumulateFractionalDigits;
	}
	else {
	  unexpectedNumericInput(mark, input);
	  done = true;
	}
	break;
      case AcceptDigitRadix:
	if ( Character.isDigit(input) ) {
	  state = AccumulateInitialDigits;
	  if ( input == '0' ) {
	    leadingZero = true;
	  }
	}
	else if ( input == '.' ) {
	  state = AccumulateFractionalDigits;
	}
	else {
	  unexpectedNumericInput(mark, input);
	  done = true;
	}
	break;
      case AccumulateInitialDigits:
	// Accumulating initial digits.
	if ( input == '.' ) {
	  state = AccumulateFractionalDigits;
	}
	else if ( input == 'e' || input == 'E' ) {
	  state = AcceptExponentDigitSign;
	}
	else if ( Character.isDigit(input) ) {
	  if ( leadingZero ) {
	    invalid(mark, "leading zero");
	  }
	}
	else {
	  unexpectedNumericInput(mark, input);
	  done = true;
	}
	break;
      case AccumulateFractionalDigits:
	isDouble = true;
	if ( input == 'e' || input == 'E' ) {
	  state = AcceptExponentDigitSign;
	}
	else if ( !Character.isDigit(input) ) {
	  unexpectedNumericInput(mark, input);
	  done = true;
	}
	break;
      case AcceptExponentDigitSign:
	isDouble = true;
	if ( input == '-' || input == '+' ) {
	  state = AccumulateExponentDigits;
	}
	else if ( !Character.isDigit(input) ) {
	  unexpectedNumericInput(mark, input);
	  done = true;
	}
	break;
      case AccumulateExponentDigits:
	if ( !Character.isDigit(input) ) {
	  unexpectedNumericInput(mark, input);
	  done = true;
	}
	break;
      }
      if ( !done ) {
	builder.append(input);
      }
    }
    final String value = builder.toString();
    // The casting is necessary to keep Java from converting the Long to a Double.
    return isDouble ? (Object) Double.valueOf(value) : (Object) Long.valueOf(value);
  }

  private void unexpectedNumericInput(final int mark, final char input) {
    if ( Character.isWhitespace(input) || input == ',' || input == '}' ) {
      pushback();
    }
    else {
      skipToNextItem(false);
      invalidNumber(mark);
    }
  }

  private String parseString(final char sentinel) {
    boolean escape = false;
    String result = null;
    final StringBuilder builder = new StringBuilder();
    while ( hasNext() ) {
      final char input = next(false);
      char[] chars = empty;
      if ( escape ) {
	switch ( input ) {
	case 'b':
	  chars = backspace;
	  break;
	case 'f':
	  chars = formfeed;
	  break;
	case 'n':
	  chars = newline;
	  break;
	case 'r':
	  chars = creturn;
	  break;
	case 't':
	  chars = tab;
	  break;
	case 'u':
	  chars = getUnicodeChar();
	  break;
	default:
	  builder.append(input);
	  break;
	}
	builder.append(chars);
	escape = false;
      }
      else if ( input == '\\' ) {
	escape = true;
      }
      else if ( input == sentinel ) {
	result = builder.toString();
	break;
      }
      else {
	builder.append(input);
      }
    }
    return result;
  }

  private char[] getUnicodeChar() {
    char[] result = empty;
    final int mark = cursor - 2;
    int codePoint = 0;
    int count = 4;
    while ( count > 0 && hasNext() ) {
      final char input = next(false);
      final int value = Character.digit(input, 16);
      if ( value >= 0 ) {
	codePoint = (codePoint << 4) + value;
	--count;
      }
      else {
	expected("hex-digit", input);
	break;
      }
    }
    if ( count == 0 ) {
      result = Character.toChars(codePoint);
    }
    else {
      invalidUnicode(mark);
    }
    return result;
  }

  private boolean expect(final char expected, final boolean skipWhitespace) {
    boolean result = false;
    final char input = next(skipWhitespace);
    if ( input == expected ) {
      result = true;
    }
    else {
      expected(expected, input);
      pushback();
    }
    return result;
  }

  private boolean scanFor(final char expected) {
    final char input = next(true);
    final boolean result = input == expected;
    if ( !result ) {
      pushback();
    }
    return result;
  }

  private boolean hasNext() {
    return cursor < length || getNextLine();
  }

  private boolean getNextLine() {
    boolean result = false;
    try {
      if ( reader.ready() ) {
	lineBuffer = reader.readLine();
	if ( lineBuffer != null ) {
	  ++line;
	  cursor = 0;
	  length = lineBuffer.length();
	  array = new char[length];
	  lineBuffer.getChars(0, length, array, 0);
	  result = true;
	}
      }
    }
    catch (IOException e) {
      // Ignore this exception and assume there is no more input.
    }
    return result;
  }

  private char next(final boolean skipWhitespace) {
    char result = '\0';
    do {
      if ( hasNext() ) {
	result = array[cursor++];
      }
      else {
	throw new EndOfInputException();
      }
    } while ( skipWhitespace && Character.isWhitespace(result) );
    return result;
  }

  private char peek() {
    return cursor < array.length ? array[cursor] : 0;
  }

  private void pushback() {
    --cursor;
  }

  private void skipToNextItem(final boolean skipWhitespace) {
    while ( hasNext() ) {
      char c = next(false);
      if ( (!skipWhitespace && Character.isWhitespace(c)) || c == ','
	  || c == '}' ) {
	pushback();
	break;
      }
    }
  }

  private void expected(char expected, char received) {
    String msg = "expected [" + expected + "] but received [" + received + "]";
    addError(msg);
  }

  private void expected(String expected, char received) {
    String s = "" + received;
    expected(expected, s);
  }

  private void expected(String expected, String received) {
    String msg = "expected [" + expected + "] but received [" + received + "]";
    addError(msg);
  }

  private void invalidNumber(int mark) {
    invalid(mark, "number");
  }

  private void invalidUnicode(int mark) {
    invalid(mark, "Unicode escape sequence");
  }

  private void invalid(int mark, String detail) {
    String msg = "invalid " + detail + " [" + lineBuffer.substring(mark, cursor)
	+ "]";
    addError(mark, msg);
  }

  private void addError(String msg) {
    addError(cursor - 1, msg);
  }

  private void addError(int mark, String msg) {
    StringBuilder sb = new StringBuilder();
    sb.append(source == null ? "JSON" : source)
      .append(": error at line ").append(line)
      .append(" position ").append(mark)
      .append(" - ").append(msg);
    if ( lineBuffer != null ) {
      sb.append("\n").append(lineBuffer).append("\n");
      for ( int i = 0; i < mark ; ++i ) {
	sb.append(" ");
      }
      sb.append("^");
    }
    String s = sb.toString();
    if ( errors == null ) {
      errors = CollectionFactory.arrayList();
    }
    errors.add(s);
    System.out.println(s);
  }

  public List<String> getErrors() {
    return errors;
  }

  public int errorCount() {
    return errors == null ? 0 : errors.size();
  }

  public boolean isValid() {
    return errors == null;
  }

  public boolean ignoreCase() {
    return this.ignoreCase;
  }

  public void ignoreCase(boolean ignoreCase) {
    this.ignoreCase = ignoreCase;
  }

  static final int AcceptDigitSignRadix = 0;
  static final int AcceptDigitRadix = 1;
  static final int AccumulateInitialDigits = 2;
  static final int AccumulateFractionalDigits = 3;
  static final int AcceptExponentDigitSign = 4;
  static final int AccumulateExponentDigits = 5;

  private final static char[] empty = new char[] {};
  private final static char[] backspace = new char[] { '\b' };
  private final static char[] formfeed = new char[] { '\f' };
  private final static char[] newline = new char[] { '\n' };
  private final static char[] creturn = new char[] { '\r' };
  private final static char[] tab = new char[] { '\t' };

  private boolean ignoreCase = false;
  private int cursor;
  private int length;
  private int level;
  private int line;
  private char[] array;
  private String lineBuffer;
  private String source;
  private JSON json;
  private List<String> errors;
  private BufferedReader reader;

  public final class EndOfInputException extends RuntimeException {
    public EndOfInputException() {
      super("UnexpectedEndOfInput");
    }

    public final static long serialVersionUID = 0;
  }
}
