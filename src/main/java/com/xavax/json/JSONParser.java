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
import java.util.Locale;

import com.xavax.util.CollectionFactory;

import static com.xavax.util.Constants.*;

/**
 * JSONParser is a parser for strings in JSON format.
 */
@SuppressWarnings({ "PMD.CyclomaticComplexity",
    		    "PMD.ModifiedCyclomaticComplexity",
    		    "PMD.StdCyclomaticComplexity" })
public class JSONParser {
  /**
   * ScannerState enumerates the possible states of the input scanner.
   */
  public enum ScannerState {
    ACCEPT_DIGIT_SIGN_RADIX,
    ACCEPT_DIGIT_RADIX,
    ACCUMULATE_DIGITS,
    ACCUMULATE_FRACTION,
    ACCEPT_EXPONENT_SIGN,
    ACCUMULATE_EXPONENT
  };

  private final static char NULL_CHARACTER = (char) 0;
  private final static int DEFAULT_BUFFER_SIZE = 64;

  private final static char[] EMPTY_ARRAY = new char[] {};
  private final static char[] BACKSPACE_ARRAY = new char[] { BACKSPACE };
  private final static char[] FORMFEED_ARRAY = new char[] { FORMFEED };
  private final static char[] NEWLINE_ARRAY = new char[] { NEWLINE };
  private final static char[] RETURN_ARRAY = new char[] { CRETURN };
  private final static char[] TAB_ARRAY = new char[] { TAB };

  private boolean abortOnError;
  private boolean allowCompoundIdentifiers;
  private boolean ignoreCase;
  private boolean quiet = true;
  private int cursor;
  private int length;
  private int level;
  private int line;
  private char[] array;
  private String lineBuffer;
  private String source;
  private List<String> errors;
  private Locale locale;
  private BufferedReader reader;

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
   * @param reader  the reader to use for input.
   * @param source  the source name to associate with the input.
   */
  public JSONParser(final Reader reader, final String source) {
    this.source = source;
    this.reader = reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
    init();
  }

  /**
   * Initialize this parser.
   */
  private void init() {
    line = 0;
    level = 0;
    cursor = 0;
    length = 0;
    array = null;
    lineBuffer = null;
    errors = null;
    locale = Locale.getDefault();
  }

  /**
   * Parse an input string in JSON format and return a JSON.
   *
   * @param reader  the reader to use for input.
   * @param source  the source name to associate with the input.
   * @return a JSON object.
   */
  static public JSON parse(final Reader reader, final String source) {
    JSON result = null;
    if ( reader != null ) {
      final JSONParser parser = new JSONParser(reader, source);
      result = parser.parse();
    }
    return result;
  }

  /**
   * Initialize this parser to parse an input string.
   *
   * @param input  a string in JSON format to be parsed.
   */
  private void init(final String input) {
    source = null;
    reader = new BufferedReader(new StringReader(input));
    init();
  }

  /**
   * Construct a JSONParser configured to parse the specified input.
   *
   * @param input  a string in JSON format.
   * @return the JSON resulting from parsing the input.
   */
  public JSON parse(final String input) {
    init(input);
    return parse();
  }

  /**
   * Parse the input and return a JSON.
   *
   * @return the JSON resulting from parsing the input.
   */
  public JSON parse() {
    level = 0;
    final JSON json = new JSON();
    boolean flag = false;
    try {
      if ( expect('{', NULL_CHARACTER, true) ) {
	flag = true;
	parseItems(json);
      }
    }
    catch (UnexpectedEndOfInputException e) {
      final String msg = "unexpected end of input";
      addError(msg);
    }
    catch (ParserException e) {
      // Ignore Exception.
    }
    if ( flag && !abortOnError && hasNext() ) {
      addError("unexpected characters after closing brace");
    }
    return json;
  }

  /**
   * Parse a string in JSON format containing an array.
   *
   * @param input  a string in JSON format.
   * @return the JSONArray resulting from parsing the input.
   */
  public JSONArray parseArray(final String input) {
    init(input);
    level = 0;
    final JSONArray list = new JSONArray();
    try {
      if ( expect('[', NULL_CHARACTER, true) ) {
	parseArrayItems(list);
      }
      if ( level != 0 ) {
	addError("unmatched braces or brackets");
      }
    }
    catch (UnexpectedEndOfInputException e) {
      final String msg = "unexpected end of input";
      addError(msg);
    }
    catch (ParserException e) {
      // Ignore Exception.
    }
    if ( !abortOnError && hasNext() ) {
      addError("unexpected characters after closing brace");
    }
    return list;
  }

  private void parseItems(final JSON map) {
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
      // expected("identifier", peek());
      expect(',', '}', true);
      pushback();
    }
    else {
      if ( expect(':', NULL_CHARACTER, true) ) {
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
      key = parseIdentifier(true, input);
    }
    else {
      pushback();
      key = parseIdentifier(false, input);
    }
    return key;
  }

  private boolean checkIdentifier(final char input, final boolean first) {
    return first && Character.isLetter(input)
	   || !first && Character.isLetterOrDigit(input)
	   || !first && input == '.' && allowCompoundIdentifiers
	   || input == '_' || input == '$';
  }

  private String parseIdentifier(final boolean mustMatch, final char opening) {
    final StringBuilder builder = new StringBuilder();
    final int mark = cursor;
    char input = next(false);
    if ( checkIdentifier(input, true) ) {
      builder.append(input);
      while ( hasNext() ) {
	input = next(false);
	if ( checkIdentifier(input, false) ) {
	  builder.append(input);
	}
	else {
	  if ( mustMatch ) {
	    if ( input != opening ) {
	      skipToNextItem(true);
	      invalidIdentifier(mark);
	    }
	  }
	  else {
	    pushback();
	  }
	  break;
	}
      }
    }
    else {
      if ( input == ',' || input == '}' ) {
	expected("identifier", input);
	pushback();
      }
      else if ( input  == ':' ) {
	expected("identifier", input);
	skipToNextItem(true);
      }
      else {
	invalidIdentifier(mark);
	skipToNextItem(true);
      }
    }
    return builder.toString();
  }

  @SuppressWarnings("PMD.NcssCount")
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
	    word = word.toLowerCase(locale);
	  }
	  if ( NULL_STRING.equals(word) ) {
	    result = null;
	  }
	  else if ( TRUE_STRING.equals(word) ) {
	    result = Boolean.TRUE;
	  }
	  else if ( FALSE_STRING.equals(word) ) {
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
	expected(',', ']', input);
	break;
      }
    }
  }

  @SuppressWarnings({ "PMD.NPathComplexity", "PMD.NcssCount" })
  private Object parseNumber() {
    boolean done = false;
    boolean isDouble = false;
    boolean leadingZero = false;
    ScannerState state = ScannerState.ACCEPT_DIGIT_SIGN_RADIX;
    final int mark = cursor;
    final StringBuilder builder = new StringBuilder();
    while ( !done && hasNext() ) {
      // Skip whitespace only for first character of number.
      final char input = next(state == ScannerState.ACCEPT_DIGIT_SIGN_RADIX);
      switch ( state ) {
      case ACCEPT_DIGIT_SIGN_RADIX:
	if ( Character.isDigit(input) ) {
	  state = ScannerState.ACCUMULATE_DIGITS;
	  if ( input == '0' ) {
	    leadingZero = true;
	  }
	}
	else if ( input == '-' ) {
	  state = ScannerState.ACCEPT_DIGIT_RADIX;
	}
	else if ( input == '.' ) {
	  state = ScannerState.ACCUMULATE_FRACTION;
	}
	else {
	  unexpectedNumericInput(mark, input);
	  done = true;
	}
	break;
      case ACCEPT_DIGIT_RADIX:
	if ( Character.isDigit(input) ) {
	  state = ScannerState.ACCUMULATE_DIGITS;
	  if ( input == '0' ) {
	    leadingZero = true;
	  }
	}
	else if ( input == '.' ) {
	  state = ScannerState.ACCUMULATE_FRACTION;
	}
	else {
	  unexpectedNumericInput(mark, input);
	  done = true;
	}
	break;
      case ACCUMULATE_DIGITS:
	// Accumulating initial digits.
	if ( input == '.' ) {
	  state = ScannerState.ACCUMULATE_FRACTION;
	}
	else if ( input == 'e' || input == 'E' ) {
	  state = ScannerState.ACCEPT_EXPONENT_SIGN;
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
      case ACCUMULATE_FRACTION:
	isDouble = true;
	if ( input == 'e' || input == 'E' ) {
	  state = ScannerState.ACCEPT_EXPONENT_SIGN;
	}
	else if ( !Character.isDigit(input) ) {
	  unexpectedNumericInput(mark, input);
	  done = true;
	}
	break;
      case ACCEPT_EXPONENT_SIGN:
	isDouble = true;
	if ( input == '-' || input == '+' ) {
	  state = ScannerState.ACCUMULATE_EXPONENT;
	}
	else if ( !Character.isDigit(input) ) {
	  unexpectedNumericInput(mark, input);
	  done = true;
	}
	break;
      case ACCUMULATE_EXPONENT:
	if ( !Character.isDigit(input) ) {
	  unexpectedNumericInput(mark, input);
	  done = true;
	}
	break;
      default:
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
      char[] chars = EMPTY_ARRAY;
      if ( escape ) {
	switch ( input ) {
	case 'b':
	  chars = BACKSPACE_ARRAY;
	  break;
	case 'f':
	  chars = FORMFEED_ARRAY;
	  break;
	case 'n':
	  chars = NEWLINE_ARRAY;
	  break;
	case 'r':
	  chars = RETURN_ARRAY;
	  break;
	case 't':
	  chars = TAB_ARRAY;
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
    char[] result = EMPTY_ARRAY;
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

  private boolean expect(final char expected1, final char expected2, final boolean skipWhitespace) {
    boolean result = false;
    final char input = next(skipWhitespace);
    if ( input == expected1 || expected2 != 0 && input == expected2 ) {
      result = true;
    }
    else {
      expected(expected1, input);
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
    return cursor < length || readNextLine();
  }

  private boolean readNextLine() {
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
	throw new UnexpectedEndOfInputException();
      }
    } while ( skipWhitespace && Character.isWhitespace(result) );
    return result;
  }

//  private char peek() {
//    return cursor < array.length ? array[cursor] : 0;
//  }

  private void pushback() {
    --cursor;
  }

  private void skipToNextItem(final boolean skipWhitespace) {
    while ( hasNext() ) {
      final char input = next(false);
      if ( !skipWhitespace && Character.isWhitespace(input) || input == ','
	  || input == '}' ) {
	pushback();
	break;
      }
    }
  }

  private void expected(final char expected, final char received) {
    addError("expected [" + expected + "] but received [" + received + "]");
  }

  private void expected(final char expected1, final char expected2, final char received) {
    addError("expected [" + expected1 + "] or [" + expected2 + "] but received [" + received + "]");
  }

  private void expected(final String expected, final char received) {
    expected(expected, Character.toString(received));
  }

  private void expected(final String expected, final String received) {
    addError("expected [" + expected + "] but received [" + received + "]");
  }

  private void invalidIdentifier(final int mark) {
    invalid(mark, "identifier");
  }

  private void invalidNumber(final int mark) {
    invalid(mark, "number");
  }

  private void invalidUnicode(final int mark) {
    invalid(mark, "Unicode escape sequence");
  }

  private void invalid(final int mark, final String detail) {
    final String rejected = lineBuffer == null ? "" : lineBuffer.substring(mark, cursor);
    addError(mark, "invalid " + detail + " [" + rejected + "]");
  }

  private void addError(final String msg) {
    addError(cursor - 1, msg);
  }

  private void addError(final int mark, final String message) {
    final StringBuilder builder = new StringBuilder(DEFAULT_BUFFER_SIZE);
    builder.append(source == null ? "JSON" : source)
      	   .append(": error at line ").append(line)
      	   .append(" position ").append(mark)
      	   .append(" - ").append(message);
    if ( lineBuffer != null ) {
      builder.append(NEWLINE).append(lineBuffer).append(NEWLINE);
      for ( int i = 0; i < mark ; ++i ) {
	builder.append(SPACE);
      }
      builder.append(CARET);
    }
    final String msg = builder.toString();
    if ( errors == null ) {
      errors = CollectionFactory.arrayList();
    }
    errors.add(msg);
    if ( !quiet ) {
      System.out.println(msg);
    }
    if ( abortOnError ) {
      throw new ParserException();
    }
  }

  /**
   * Return the list of errors.
   *
   * @return the list of errors.
   */
  public List<String> getErrors() {
    return errors;
  }

  /**
   * Return the error count.
   *
   * @return the error count.
   */
  public int errorCount() {
    return errors == null ? 0 : errors.size();
  }

  /**
   * Return true if no errors occurred during parsing.
   *
   * @return true if no errors occurred during parsing.
   */
  public boolean isValid() {
    return errors == null;
  }

  /**
   * Returns true if parsing should be aborted after the first error.
   *
   * @return true if parsing should be aborted after the first error.
   */
  public boolean abortOnError() {
    return this.abortOnError;
  }

  /**
   * Set the abortOnError flag.
   *
   * @param abortOnError  if true, abort parsing on first error.
   */
  public JSONParser abortOnError(final boolean abortOnError) {
    this.abortOnError = abortOnError;
    return this;
  }

  /**
   * Returns true if compound identifiers should be allowed. Compound
   * identifiers are of the form "id.name". This is not compliant with
   * the JSON specification but commonly used in MongoDB code.
   *
   * @return true if compound identifiers should be allowed.
   */
  public boolean allowCompoundIdentifiers() {
    return this.allowCompoundIdentifiers;
  }

  /**
   * Set the allowCompoundIdentifiers flag.
   *
   * @param allowCompoundIdentifiers  if true, allow compound identifiers.
   * @return this parser.
   */
  public JSONParser allowCompoundIdentifiers(final boolean allowCompoundIdentifiers) {
    this.allowCompoundIdentifiers = allowCompoundIdentifiers;
    return this;
  }

  /**
   * Return true if the parser is ignoring case.
   *
   * @return true if the parser is ignoring case.
   */
  public boolean ignoreCase() {
    return this.ignoreCase;
  }

  /**
   * Set the ignoreCase flag.
   *
   * @param ignoreCase  if true, ignore case while parsing.
   * @return this parser.
   */
  public JSONParser ignoreCase(final boolean ignoreCase) {
    this.ignoreCase = ignoreCase;
    return this;
  }

  /**
   * Returns true if console messages should be inhibited.
   *
   * @return true if console messages should be inhibited.
   */
  public boolean quiet() {
    return quiet;
  }

  /**
   * Sets the quiet flag.
   *
   * @param quiet  true if console messages should be inhibited.
   * @return this parser.
   */
  public JSONParser quiet(final boolean quiet) {
    this.quiet = quiet;
    return this;
  }

  /**
   * EndOfInputException is thrown when we encounter an unexpected
   * end of input (end of file).
   */
  public final class UnexpectedEndOfInputException extends RuntimeException {
    public final static long serialVersionUID = 0;

    /**
     * Construct an EndOfInputException.
     */
    public UnexpectedEndOfInputException() {
      super("UnexpectedEndOfInput");
    }
  }

  public static class ParserException extends RuntimeException {
    private static final long serialVersionUID = 1L;
  }
}
