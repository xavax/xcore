//
// Copyright 2007, 2013 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//

package com.xavax.logger;

import java.util.Formatter;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import static com.xavax.util.Constants.*;

/**
 * XLogger is a logging utility class used in Xavax products.
 *
 * @author alvitar@xavax.com
 */
@SuppressWarnings({"PMD.GodClass", "PMD.TooManyMethods"})
public final class XLogger {
  public final static int DEFAULT_LENGTH = 128;
  public final static int EXTRA_LENGTH = 64;
  public final static String ENTER = "enter";
  public final static String LEAVE = "leave";
  public final static String LEAVE_MESSAGE = "leave, return value = [";
  public final static String TRACE_ENTER = "{}.{}: enter";
  public final static String TRACE_LEAVE = "{}.{}: leave";
  public final static String TRACE_RETURN = "{}.{}: returns {}";
  public final static String UNKNOWN = "<unknown>";

  /**
   * Private constructor provided to keep the compiler from generating
   * a public default constructor.
   */
  private XLogger() {}

  /**
   * Write a message to the log at debug level.
   *
   * @param logger   the logger used to write this log entry.
   * @param method   the method name.
   * @param message  the message to be written to the log.
   */
  public static void debug(final Logger logger, final String method,
                           final String message)
  {
    if ( logger != null && logger.isDebugEnabled() ) {
      logger.debug(format(logger, method, message));
    }
  }

  /**
   * Format and write a message to the log at debug level.
   *
   * @param logger  the logger used to write this log entry.
   * @param method  the method name.
   * @param format  the message format string.
   * @param params  the message parameters.
   */
  public static void debug(final Logger logger, final String method,
                           final String format, final Object... params)
  {
    if ( logger != null && logger.isDebugEnabled() ) {
      logger.debug(format(logger, method, format, params));
    }
  }

  /**
   * Write a message to the log at error level.
   *
   * @param logger   the logger used to write this log entry
   * @param method   the method name.
   * @param message  the message to be written to the log.
   */
  public static void error(final Logger logger, final String method,
                           final String message)
  {
    if ( logger != null && logger.isEnabledFor(Level.ERROR) ) {
      logger.error(format(logger, method, message));
    }
  }

  /**
   * Format and write a message to the log at error level.
   *
   * @param logger  the logger used to write this log entry.
   * @param method  the method name.
   * @param format  the message format string.
   * @param params  the message parameters.
   */
  public static void error(final Logger logger, final String method,
                           final String format, final Object... params)
  {
    if ( logger != null && logger.isEnabledFor(Level.ERROR) ) {
      logger.error(format(logger, method, format, params));
    }
  }

  /**
   * Write a message to the log at error level.
   *
   * @param logger   the logger used to write this log entry
   * @param method   the method name.
   * @param cause    the cause of the error.
   * @param message  the message to be written to the log.
   */
  public static void error(final Logger logger, final String method,
                           final Throwable cause, final String message)
  {
    if ( logger != null && logger.isEnabledFor(Level.ERROR) ) {
      logger.error(format(logger, method, message), cause);
    }
  }

  /**
   * Format and write a message to the log at error level.
   *
   * @param logger  the logger used to write this log entry.
   * @param method  the method name.
   * @param cause   the cause of the error.
   * @param format  the message format string.
   * @param params  the message parameters.
   */
  public static void error(final Logger logger, final String method,
                           final Throwable cause, final String format,
                           final Object... params)
  {
    if ( logger != null && logger.isEnabledFor(Level.ERROR) ) {
      logger.error(format(logger, method, format, params), cause);
    }
  }

  /**
   * Write a message to the log at fatal level.
   *
   * @param logger   the logger used to write this log entry.
   * @param method   the method name.
   * @param message  the message to be written to the log.
   */
  public static void fatal(final Logger logger, final String method,
                           final String message)
  {
    if ( logger != null ) {
      logger.fatal(format(logger, method, message));
    }
  }

  /**
   * Format and write a message to the log at fatal level.
   *
   * @param logger  the logger used to write this log entry.
   * @param method  the method name.
   * @param format  the message format string.
   * @param params  the message parameters.
   */
  public static void fatal(final Logger logger, final String method,
                           final String format, final Object... params)
  {
    if ( logger != null ) {
      logger.fatal(format(logger, method, format, params));
    }
  }

  /**
   * Write a message to the log at fatal level.
   *
   * @param logger   the logger used to write this log entry.
   * @param method   the method name.
   * @param cause    the cause of the fatal.
   * @param message  the message to be written to the log.
   */
  public static void fatal(final Logger logger, final String method,
                           final Throwable cause, final String message)
  {
    if ( logger != null ) {
      logger.fatal(format(logger, method, message), cause);
    }
  }

  /**
   * Format and write a message to the log at fatal level.
   *
   * @param logger  the logger used to write this log entry.
   * @param method  the method name.
   * @param cause   the cause of the error.
   * @param format  the message format string.
   * @param params  the message parameters.
   */
  public static void fatal(final Logger logger, final String method,
                           final Throwable cause, final String format,
                           final Object... params)
  {
    if ( logger != null ) {
      logger.fatal(format(logger, method, format, params), cause);
    }
  }

  /**
   * Write a message to the log at info level.
   *
   * @param logger   the logger used to write this log entry.
   * @param method   the method name.
   * @param message  the message to be written to the log.
   */
  public static void info(final Logger logger, final String method,
                          final String message)
  {
    if ( logger != null && logger.isEnabledFor(Level.INFO) ) {
      logger.info(format(logger, method, message));
    }
  }

  /**
   * Format and write a message to the log at info level.
   *
   * @param logger  the logger used to write this log entry.
   * @param method  the method name.
   * @param format  the message format string.
   * @param params  the message parameters.
   */
  public static void info(final Logger logger, final String method,
                          final String format, final Object... params)
  {
    if ( logger != null && logger.isEnabledFor(Level.INFO) ) {
      logger.info(format(logger, method, format, params));
    }
  }

  /**
   * Write a message to the log at info level.
   *
   * @param logger   the logger used to write this log entry
   * @param method   the method name.
   * @param cause    the cause of the error.
   * @param message  the message to be written to the log.
   */
  public static void info(final Logger logger, final String method,
                          final Throwable cause, final String message)
  {
    if ( logger != null && logger.isEnabledFor(Level.INFO) ) {
      logger.info(format(logger, method, message), cause);
    }
  }

  /**
   * Format and write a message to the log at info level.
   *
   * @param logger  the logger used to write this log entry.
   * @param method  the method name.
   * @param cause   the cause of the error.
   * @param format  the message format string.
   * @param params  the message parameters.
   */
  public static void info(final Logger logger, final String method,
                          final Throwable cause, final String format,
                          final Object... params)
  {
    if ( logger != null && logger.isEnabledFor(Level.INFO) ) {
      logger.info(format(logger, method, format, params), cause);
    }
  }

  /**
   * Write a message to the log at warn level.
   *
   * @param logger   the logger used to write this log entry.
   * @param method   the method name.
   * @param message  the message to be written to the log.
   */
  public static void warn(final Logger logger, final String method,
                          final String message)
  {
    if ( logger != null && logger.isEnabledFor(Level.WARN) ) {
      logger.warn(format(logger, method, message));
    }
  }

  /**
   * Format and write a message to the log at warn level.
   *
   * @param logger  the logger used to write this log entry.
   * @param method  the method name.
   * @param format  the message format string.
   * @param params  the message parameters.
   */
  public static void warn(final Logger logger, final String method,
                          final String format, final Object... params)
  {
    if ( logger != null && logger.isEnabledFor(Level.WARN) ) {
      logger.warn(format(logger, method, format, params));
    }
  }
 
  /**
   * Write a message to the log at warn level.
   *
   * @param logger   the logger used to write this log entry.
   * @param method   the method name.
   * @param cause    the cause of the error.
   * @param message  the message to be written to the log.
   */
  public static void warn(final Logger logger, final String method,
                          final Throwable cause, final String message)
  {
    if ( logger != null && logger.isEnabledFor(Level.WARN) ) {
      logger.warn(format(logger, method, message), cause);
    }
  }

  /**
   * Format and write a message to the log at warn level.
   *
   * @param logger  the logger used to write this log entry.
   * @param method  the method name.
   * @param cause   the cause of the error.
   * @param format  the message format string.
   * @param params  the message parameters.
   */
  public static void warn(final Logger logger, final String method,
                          final Throwable cause, final String format,
                          final Object... params)
  {
    if ( logger != null && logger.isEnabledFor(Level.WARN) ) {
      logger.warn(format(logger, method, format, params), cause);
    }
  }

  /**
   * Write a message to the log at trace level.
   *
   * @param logger   the logger used to write this log entry
   * @param method   the method name.
   * @param message  the message to be written to the log.
   */
  public static void trace(final Logger logger, final String method,
                           final String message)
  {
    if ( logger != null && logger.isTraceEnabled() ) {
      logger.trace(format(logger, method, message));
    }
  }

  /**
   * Write a message to the log at trace level when entering a static method.
   *
   * @param logger  the logger used to write this log entry.
   * @param method  the method name.
   */
  public static void enter(final Logger logger, final String method)
  {
    if ( logger != null && logger.isTraceEnabled() ) {
      logger.trace(format(logger, method, ENTER));
    }
  }

  /**
   * Write a message to the log at trace level when leaving a static method.
   *
   * @param logger  the logger used to write this log entry.
   * @param method  the method name.
   */
  public static void leave(final Logger logger, final String method)
  {
    if ( logger != null && logger.isTraceEnabled() ) {
      logger.trace(format(logger, method, LEAVE));
    }
  }

  /**
   * Write a message to the log at trace level when leaving a static method.
   *
   * @param logger  the logger used to write this log entry.
   * @param method  the method name.
   * @param result  the method's return value.
   */
  public static void leave(final Logger logger, final String method,
                           final Object result)
  {
    if ( logger != null && logger.isTraceEnabled() ) {
      logger.trace(formatLeave(logger, method,
	  result == null ? NULL_STRING : result.toString()));
    }
  }

  /**
   * Write a message to the log at trace level when leaving a static method.
   *
   * @param logger  the logger used to write this log entry.
   * @param method  the method name.
   * @param result  the method's return value.
   */
  public static void leave(final Logger logger, final String method,
                           final boolean result)
  {
    if ( logger != null && logger.isTraceEnabled() ) {
      logger.trace(formatLeave(logger, method, String.valueOf(result)));
    }
  }

  /**
   * Add the class and method name prefix to a string builder.
   *
   * @param builder  the string builder for building the message.
   * @param logger   the logger used to write this log entry.
   * @param method   the name of the method being traced.
   */
  static void addPrefix(final StringBuilder builder, final Logger logger,
                        final String method)
  {
    String name = logger.getName();
    if ( name == null ) {
      name = UNKNOWN;
    }
    final int index = name.lastIndexOf('.');
    builder.append(index >= 0 ? name.substring(index + 1) : name)
           .append('.')
           .append(method == null ? UNKNOWN : method)
           .append(": ");
  }

  /**
   * Returns a formatted message with the class and method name inserted.
   *
   * @param logger   the logger used to write this log entry.
   * @param method   the name of the method being traced.
   * @param message  the message to be included in the log entry.
   * @return a formatted message.
   */
  static String format(final Logger logger, final String method,
                       final String message)
  {
    String result = EMPTY_STRING;
    if ( logger != null ) {
      final int length = message.length() + EXTRA_LENGTH;
      final StringBuilder builder = new StringBuilder(length);
      addPrefix(builder, logger, method);
      builder.append(message);
      result = builder.toString();
    }
    return result;
  }

  /**
   * Returns a formatted message with the class and method name inserted
   * and the return value of the method.
   *
   * @param logger  the logger used to write this log entry.
   * @param method  the name of the method being traced.
   * @param result  the return value as a string.
   * @return a formatted message.
   */
  static String formatLeave(final Logger logger, final String method,
                            final String result)
  {
    String output = EMPTY_STRING;
    if ( logger != null ) {
      final StringBuilder builder = new StringBuilder(DEFAULT_LENGTH); 
      addPrefix(builder, logger, method);
      builder.append(LEAVE_MESSAGE)
             .append(result == null ? NULL_INDICATOR : result)
             .append(']');
      output = builder.toString();
    }
    return output;
  }

  /**
   * Returns a formatted message with the class and method name inserted
   * and the format string expanded with parameters.
   *
   * @param logger  the logger used to write this log entry.
   * @param method  the name of the method being traced.
   * @param format  the message format string.
   * @param params  the message parameters.
   * @return a formatted message.
   */
  public static String format(final Logger logger, final String method,
                              final String format, final Object... params)
  {
    String output = EMPTY_STRING;
    if ( logger != null ) {
      final StringBuilder builder = new StringBuilder();
      addPrefix(builder, logger, method);
      final Formatter formatter = new Formatter(builder);
      formatter.format(format, params);
      formatter.close();
      output = builder.toString();
    }
    return output;
  }
}
