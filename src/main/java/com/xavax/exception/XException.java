package com.xavax.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import com.xavax.message.XMessage;

import static com.xavax.message.XMessage.*;
import static com.xavax.util.Constants.*;

/**
 * XException is a wrapper for XCore exceptions. It implements standard
 * exception handling. The default exception handler writes a message and
 * stack trace to stderr, but you can override this by providing your own
 * handler. You can also add handlers for specific types of exceptions,
 * and those handlers will be used instead of the default exception.
 *
 * Note: Class-specific exception handlers only apply to that class and not
 * any subclasses; however, you can use the same handler for multiple classes.
 */
public class XException extends Exception {
  private final static long serialVersionUID = 1L;

  private final static ExceptionHandler INITIAL_DEFAULT_HANDLER = new DefaultExceptionHandler();
  private final static Map<Class<? extends Throwable>, ExceptionHandler> HANDLER_MAP = new HashMap<>();

  private static ExceptionHandler defaultHandler = INITIAL_DEFAULT_HANDLER;

  private final XMessage message;
  private final Object[] args;

  /**
   * Construct an XException.
   *
   * @param cause    the cause of this exception.
   * @param message  a message describing this exception.
   * @param args     optional arguments for the message.
   */
  public XException(final Throwable cause,
		    final XMessage message,
		    final Object... args) {
    super(cause);
    this.message = message == null ? UNEXPECTED_EXCEPTION : message;
    this.args = args == null ? EMPTY_ARGS : args;
  }

  /**
   * Construct an XException.
   *
   * @param cause  the cause of this exception.
   */
  public XException(final Throwable cause) {
    this(cause, UNEXPECTED_EXCEPTION, EMPTY_ARGS);
  }

  /**
   * Construct an XException.
   *
   * @param message  a message describing this exception.
   * @param args     optional arguments for the message.
   */
  public XException(final XMessage message, final Object... args) {
    this(null, message, args);
  }

  /**
   * Construct a default XException. This is deprecated because it provides the
   * user with very little information.
   */
  @Deprecated
  public XException() {
    this(null, UNEXPECTED_EXCEPTION, EMPTY_ARGS);
  }

  /**
   * Returns the message.
   *
   * @return the message.
   */
  public XMessage getXMessage() {
    return message;
  }

  /**
   * Returns the optional message arguments.
   *
   * @return the optional message arguments.
   */
//  public Object[] getArguments() {
//    return args;
//  }

  /**
   * Returns the formatted message with arguments substituted.
   *
   * @return the formatted message.
   */
  public String getFormattedMessage() {
    return message.format(args);
  }

  /**
   * Perform standard exception handling.
   */
  public void handle() {
    handle(getCause(), message, args);
  }

  /**
   * Perform standard exception handling. This can be called directly when it is
   * not necessary to throw a new XException.
   *
   * @param cause the exception.
   * @param message the exception message (GambitMessage).
   * @param args optional message parameters.
   */
  public static void handle(final Throwable cause, final XMessage message,
			    final Object... args) {
    final ExceptionHandler handler =
	cause == null ? defaultHandler : getHandler(cause.getClass());
    (handler == null ? defaultHandler : handler).handle(cause, message, args);
  }

  /**
   * Returns the current default exception handler.
   *
   * @return the current default exception handler.
   */
  public static ExceptionHandler getDefaultExceptionHandler() {
    return defaultHandler;
  }

  /**
   * Sets the default exception handler.
   *
   * @param handler the new default exception handler.
   */
  public static void setDefaultExceptionHandler(final ExceptionHandler handler) {
    defaultHandler = handler == null ? INITIAL_DEFAULT_HANDLER : handler;
  }

  /**
   * Add an exception handler for a specific class of exceptions.
   *
   * @param handler         the new exception handler
   * @param exceptionClass  the exception class (must extend Throwable).
   */
  public static void addExceptionHandler(final ExceptionHandler handler,
					 final Class<? extends Throwable> exceptionClass) {
    synchronized ( HANDLER_MAP ) {
      final ExceptionHandler currentHandler = HANDLER_MAP.get(exceptionClass);
      if ( currentHandler == null ) {
	HANDLER_MAP.put(exceptionClass, handler);
      }
      else {
	HANDLER_MAP.replace(exceptionClass, handler);
      }
    }
  }

  /**
   * Returns the exception handler registered for an exception class, or null if
   * no handler is registered.
   *
   * @param exceptionClass  the exception class.
   * @return the exception handler registered for an exception class
   */
  public static ExceptionHandler getHandler(final Class<? extends Throwable> exceptionClass) {
    ExceptionHandler handler = null;
    synchronized ( HANDLER_MAP ) {
      handler = HANDLER_MAP.get(exceptionClass);
    }
    return handler;
  }

  /**
   * Returns the stack trace as a string.
   *
   * @param exception  the exception.
   * @return the stack trace as a string.
   */
  public static String getStackTrace(final Throwable exception) {
    final StringWriter stringWriter = new StringWriter();
    final PrintWriter printWriter = new PrintWriter(stringWriter);
    exception.printStackTrace(printWriter);
    return stringWriter.toString();
  }

}
