//
// Copyright 2007, 2015 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//

package com.xavax.base;

import java.util.Formatter;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.xavax.logger.XLogger;
import static com.xavax.util.Constants.*;

/**
 * XObject (eXtended Object) is a base class for objects that
 * use a Logger to trace program flow.
 */
public class XObject {

  protected String prefix;
  protected final Logger logger;

  /**
   * Construct a XObject using the Logger for the class of this
   * object.
   */
  protected XObject()
  {
    this.logger = Logger.getLogger(this.getClass());
    init();
  }

  /**
   * Construct a XObject using the specified Logger.
   *
   * @param logger  the logger to use for this object.
   */
  protected XObject(final Logger logger)
  {
    this.logger = logger;
    init();
  }

  /**
   * Initialize this object. Create the prefix string to be used in
   * trace messages and convert it to canonical form so only one copy
   * is stored in memory.
   */
  private void init()
  {
    final String prefix = getClass().getSimpleName() + ".";
    this.prefix = prefix.intern();		
  }

  /**
   * Returns true if debug is enabled.
   */
  public boolean debugEnabled()
  {
    return logger == null ? false : logger.isDebugEnabled();
  }

  /**
   * Write a message to the log at debug level.
   *
   * @param method   the method name.
   * @param message  the message to display.
   */
  public void debug(final String method, final String message)
  {
    if ( logger != null && logger.isDebugEnabled() ) {
      logger.debug(format(method, message));
    }
  }

  /**
   * Format and write a message to the log at debug level.
   *
   * @param method  the method name.
   * @param format  the message format string.
   * @param params  the message parameters.
   */
  public void debug(final String method, final String format, final Object... params)
  {
    if ( logger != null && logger.isDebugEnabled() ) {
      logger.debug(format(method, format, params));
    }
  }

  /**
   * Write a message to the log at error level.
   *
   * @param method   the method name.
   * @param message  the message to be written to the log.
   */
  public void error(final String method, final String message)
  {
    if ( logger != null && logger.isEnabledFor(Level.ERROR) ) {
      logger.error(format(method, message));
    }
  }

  /**
   * Format and write a message to the log at error level.
   *
   * @param method  the method name.
   * @param format  the message format string.
   * @param params  the message parameters.
   */
  public void error(final String method, final String format, final Object... params)
  {
    if ( logger != null && logger.isEnabledFor(Level.ERROR) ) {
      logger.error(format(method, format, params));
    }
  }

  /**
   * Write a message to the log at error level.
   *
   * @param method   the method name.
   * @param cause    the cause of the error.
   * @param message  the message to be written to the log.
   */
  public void error(final String method, final Throwable cause, final String message)
  {
    if ( logger != null && logger.isEnabledFor(Level.ERROR) ) {
      logger.error(format(method, message), cause);
    }
  }

  /**
   * Format and write a message to the log at error level.
   *
   * @param method   the method name.
   * @param message  the message to be written to the log.
   * @param format   the message format string.
   * @param params   the message parameters.
   */
  public void error(final String method, final Throwable cause,
                    final String format, final Object... params)
  {
    if ( logger != null && logger.isEnabledFor(Level.ERROR) ) {
      logger.error(format(method, format, params), cause);
    }
  }

  /**
   * Write a message to the log at fatal level.
   *
   * @param method   the method name.
   * @param message  the message to be written to the log.
   */
  public void fatal(final String method, final String message)
  {
    if ( logger != null ) {
      logger.fatal(format(method, message));
    }
  }

  /**
   * Format and write a message to the log at fatal level.
   *
   * @param method  the method name.
   * @param format  the message format string.
   * @param params  the message parameters.
   */
  public void fatal(final String method, final String format, final Object... params)
  {
    if ( logger != null ) {
      logger.fatal(format(method, format, params));
    }
  }

  /**
   * Write a message to the log at fatal level.
   *
   * @param method   the method name.
   * @param cause    the cause of the fatal.
   * @param message  the message to be written to the log.
   */
  public void fatal(final String method, final Throwable cause, final String message)
  {
    if ( logger != null ) {
      logger.fatal(format(method, message), cause);
    }
  }

  /**
   * Format and write a message to the log at fatal level.
   *
   * @param method   the method name.
   * @param message  the message to be written to the log.
   * @param format   the message format string.
   * @param params   the message parameters.
   */
  public void fatal(final String method, final Throwable cause,
                    final String format, final Object... params)
  {
    if ( logger != null ) {
      logger.fatal(format(method, format, params), cause);
    }
  }

  /**
   * Write a message to the log at info level.
   *
   * @param method   the method name.
   * @param message  the message to be written to the log.
   */
  public void info(final String method, final String message)
  {
    if ( logger != null && logger.isEnabledFor(Level.INFO) ) {
      logger.info(format(method, message));
    }
  }

  /**
   * Format and write a message to the log at info level.
   *
   * @param method  the method name.
   * @param format  the message format string.
   * @param params  the message parameters.
   */
  public void info(final String method, final String format, final Object... params)
  {
    if ( logger != null && logger.isEnabledFor(Level.INFO) ) {
      logger.info(format(method, format, params));
    }
  }

  /**
   * Write a message to the log at info level.
   *
   * @param method   the method name.
   * @param message  the message to be written to the log.
   * @param cause    the cause of the info.
   */
  public void info(final String method, final Throwable cause, final String message)
  {
    if ( logger != null && logger.isEnabledFor(Level.INFO) ) {
      logger.info(format(method, message), cause);
    }
  }

  /**
   * Format and write a message to the log at info level.
   *
   * @param method  the method name.
   * @param cause   the cause of the error.
   * @param format  the message format string.
   * @param params  the message parameters.
   */
  public void info(final String method, final Throwable cause,
                   final String format, final Object... params)
  {
    if ( logger != null && logger.isEnabledFor(Level.INFO) ) {
      logger.info(format(method, format, params), cause);
    }
  }

  /**
   * Write a message to the log at warn level.
   *
   * @param method   the method name.
   * @param message  the message to be written to the log.
   */
  public void warn(final String method, final String message)
  {
    if ( logger != null && logger.isEnabledFor(Level.WARN) ) {
      logger.warn(format(method, message));
    }
  }

  /**
   * Format and write a message to the log at warn level.
   *
   * @param method  the method name.
   * @param format  the message format string.
   * @param params  the message parameters.
   */
  public void warn(final String method, final String format,
                   final Object... params)
  {
    if ( logger != null && logger.isEnabledFor(Level.WARN) ) {
      logger.warn(format(method, format, params));
    }
  }

  /**
   * Write a message to the log at warn level.
   *
   * @param method   the method name.
   * @param cause    the cause of the error.
   * @param message  the message to be written to the log.
   */
  public void warn(final String method, final Throwable cause, final String message)
  {
    if ( logger != null && logger.isEnabledFor(Level.WARN) ) {
      logger.warn(format(method, message), cause);
    }
  }

  /**
   * Format and write a message to the log at warn level.
   *
   * @param method   the method name.
   * @param message  the message to be written to the log.
   * @param format   the message format string.
   * @param params   the message parameters.
   */
  public void warn(final String method, final Throwable cause,
                   final String format, final Object... params)
  {
    if ( logger != null && logger.isEnabledFor(Level.WARN) ) {
      logger.warn(format(method, format, params), cause);
    }
  }

  /**
   * Returns true if trace is enabled.
   *
   * @return true if trace is enabled.
   */
  public boolean traceEnabled()
  {
    return logger == null ? false : logger.isTraceEnabled();
  }

  /**
   * Write a message to the log at trace level.
   *
   * @param method   the method name.
   * @param message  the message to display.
   */
  public void trace(final String method, final String message)
  {
    if ( logger != null && logger.isTraceEnabled() ) {
      logger.trace(format(method, message));
    }
  }

  /**
   * Write a message to the log at trace level when entering a method.
   *
   * @param method  the method name.
   */
  public void enter(final String method)
  {
    if ( logger != null && logger.isTraceEnabled() ) {
      logger.trace(format(method, XLogger.ENTER));
    }
  }

  /**
   * Write a message to the log at trace level when leaving a method.
   *
   * @param method  the method name.
   */
  public void leave(final String method)
  {
    if ( logger != null && logger.isTraceEnabled() ) {
      logger.trace(format(method, XLogger.LEAVE));
    }
  }

  /**
   * Write a message to the log at trace level when leaving a method.
   *
   * @param method  the method name.
   * @param result  the method's return value.
   */
  public void leave(final String method, final Object result)
  {
    if ( logger != null && logger.isTraceEnabled() ) {
      logger.trace(formatLeave(method,
	  result == null ? NULL_STRING : result.toString()));
    }
  }

  /**
   * Write a message to the log at trace level when leaving a method.
   *
   * @param method  the method name.
   * @param result  the method's return value.
   */
  public void leave(final String method, final boolean result)
  {
    if ( logger != null && logger.isTraceEnabled() ) {
      logger.trace(formatLeave(method, String.valueOf(result)));
    }
  }

  /**
   * Format a message with the class and method name inserted.
   *
   * @param builder  the string builder for building the message.
   * @param method   the name of the method being traced.
   * @param message  the message to be included in the log entry.
   */
  private void format(final StringBuilder builder, final String method,
                      final String message)
  {
    builder.append(method == null ? XLogger.UNKNOWN : method);
    builder.append(": ");
    if ( message != null ) {
      builder.append(message);
    }
  }

  /**
   * Returns a formatted message with the class and method name inserted.
   *
   * @param method   the name of the method being traced.
   * @param message  the message to be included in the log entry.
   * @return a formatted message.
   */
  private String format(final String method, final String message)
  {
    final StringBuilder builder = new StringBuilder(prefix);
    format(builder, method, message);
    return builder.toString();
  }

  /**
   * Returns a formatted message with the class and method name inserted
   * and the return value of the method.
   *
   * @param method  the name of the method being traced.
   * @param result  the method's return value.
   * @return a formatted message.
   */
  private String formatLeave(final String method, final String result)
  {
    final StringBuilder builder = new StringBuilder(prefix);
    format(builder, method, XLogger.LEAVE_MESSAGE);
    builder.append(result == null ? NULL_INDICATOR : result)
    	   .append(']');
    return builder.toString();
  }

  /**
   * Returns a formatted message with the class and method name inserted
   * and the format string expanded with parameters.
   *
   * @param method  the name of the method being traced.
   * @param format  the message format string.
   * @param params  the message parameters.
   * @return a formatted message.
   */
  protected String format(final String method, final String format,
                          final Object... params)
  {
    final StringBuilder builder = new StringBuilder(prefix);
    builder.append(method == null ? XLogger.UNKNOWN : method)
    	   .append(": ");
    final Formatter formatter = new Formatter(builder);
    formatter.format(format, params);
    formatter.close();
    return builder.toString();
  }
}
