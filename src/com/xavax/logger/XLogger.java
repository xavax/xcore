//
// Copyright 2007 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//

package com.xavax.logger;

import java.util.Formatter;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class XLogger {

  public XLogger() {
    // TODO Auto-generated constructor stub
  }

  /**
   * Write a message to the log at debug level.
   *
   * @param logger  the logger used to write this log entry.
   * @param method  the method name.
   * @param msg     the message to be written to the log.
   */
  public static void debug(Logger logger, String method, String msg)
  {
    if ( logger != null && logger.isDebugEnabled() ) {
      msg = format(logger, method, msg);
      logger.debug(msg);
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
  public static void debug(Logger logger, String method, String format,
                           Object... params)
  {
    if ( logger != null && logger.isDebugEnabled() ) {
      String msg = format(logger, method, format, params);
      logger.debug(msg);
    }
  }

  /**
   * Write a message to the log at error level.
   *
   * @param method  the method name.
   * @param msg     the message to be written to the log.
   */
  public static void error(Logger logger, String method, String msg)
  {
    if ( logger != null && logger.isEnabledFor(Level.ERROR) ) {
      msg = format(logger, method, msg);
      logger.error(msg);
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
  public static void error(Logger logger, String method, String format,
                           Object... params)
  {
    if ( logger != null && logger.isEnabledFor(Level.ERROR) ) {
      String msg = format(logger, method, format, params);
      logger.error(msg);
    }
  }

  /**
   * Write a message to the log at error level.
   *
   * @param method  the method name.
   * @param cause   the cause of the error.
   * @param msg     the message to be written to the log.
   */
  public static void error(Logger logger, String method, Throwable cause,
			   String msg)
  {
    if ( logger != null && logger.isEnabledFor(Level.ERROR) ) {
      msg = format(logger, method, msg);
      logger.error(msg, cause);
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
  public static void error(Logger logger, String method, Throwable cause,
			   String format, Object... params)
  {
    if ( logger != null && logger.isEnabledFor(Level.ERROR) ) {
      String msg = format(logger, method, format, params);
      logger.error(msg, cause);
    }
  }

  /**
   * Write a message to the log at fatal level.
   *
   * @param logger  the logger used to write this log entry.
   * @param method  the method name.
   * @param msg     the message to be written to the log.
   */
  public static void fatal(Logger logger, String method, String msg)
  {
    if ( logger != null ) {
      msg = format(logger, method, msg);
      logger.fatal(msg);
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
  public static void fatal(Logger logger, String method, String format,
                           Object... params)
  {
    if ( logger != null ) {
      String msg = format(logger, method, format, params);
      logger.fatal(msg);
    }
  }

  /**
   * Write a message to the log at fatal level.
   *
   * @param logger  the logger used to write this log entry.
   * @param method  the method name.
   * @param cause   the cause of the fatal.
   * @param msg     the message to be written to the log.
   */
  public static void fatal(Logger logger, String method, Throwable cause,
			   String msg)
  {
    if ( logger != null ) {
      msg = format(logger, method, msg);
      logger.fatal(msg, cause);
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
  public static void fatal(Logger logger, String method, Throwable cause,
			   String format, Object... params)
  {
    if ( logger != null ) {
      String msg = format(logger, method, format, params);
      logger.fatal(msg, cause);
    }
  }

  /**
   * Write a message to the log at info level.
   *
   * @param logger  the logger used to write this log entry.
   * @param method  the method name.
   * @param msg     the message to be written to the log.
   */
  public static void info(Logger logger, String method, String msg)
  {
    if ( logger != null && logger.isEnabledFor(Level.INFO) ) {
      msg = format(logger, method, msg);
      logger.info(msg);
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
  public static void info(Logger logger, String method, String format,
                           Object... params)
  {
    if ( logger != null && logger.isEnabledFor(Level.INFO) ) {
      String msg = format(logger, method, format, params);
      logger.info(msg);
    }
  }

  /**
   * Write a message to the log at info level.
   *
   * @param method  the method name.
   * @param cause   the cause of the error.
   * @param msg     the message to be written to the log.
   */
  public static void info(Logger logger, String method, Throwable cause,
			  String msg)
  {
    if ( logger != null && logger.isEnabledFor(Level.INFO) ) {
      msg = format(logger, method, msg);
      logger.info(msg, cause);
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
  public static void info(Logger logger, String method, Throwable cause,
			   String format, Object... params)
  {
    if ( logger != null && logger.isEnabledFor(Level.INFO) ) {
      String msg = format(logger, method, format, params);
      logger.info(msg, cause);
    }
  }

  /**
   * Write a message to the log at warn level.
   *
   * @param logger  the logger used to write this log entry.
   * @param method  the method name.
   * @param msg     the message to be written to the log.
   */
  public static void warn(Logger logger, String method, String msg)
  {
    if ( logger != null && logger.isEnabledFor(Level.WARN) ) {
      msg = format(logger, method, msg);
      logger.warn(msg);
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
  public static void warn(Logger logger, String method, String format,
                           Object... params)
  {
    if ( logger != null && logger.isEnabledFor(Level.WARN) ) {
      String msg = format(logger, method, format, params);
      logger.warn(msg);
    }
  }
 
  /**
   * Write a message to the log at warn level.
   *
   * @param logger  the logger used to write this log entry.
   * @param method  the method name.
   * @param cause   the cause of the error.
   * @param msg     the message to be written to the log.
   */
  public static void warn(Logger logger, String method, Throwable cause,
			  String msg)
  {
    if ( logger != null && logger.isEnabledFor(Level.WARN) ) {
      msg = format(logger, method, msg);
      logger.warn(msg, cause);
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
  public static void warn(Logger logger, String method, Throwable cause,
			   String format, Object... params)
  {
    if ( logger != null && logger.isEnabledFor(Level.WARN) ) {
      String msg = format(logger, method, format, params);
      logger.warn(msg, cause);
    }
  }

  /**
   * Write a message to the log at trace level.
   *
   * @param method  the method name.
   * @param msg     the message to be written to the log.
   */
  public static void trace(Logger logger, String method, String msg)
  {
    if ( logger != null && logger.isTraceEnabled() ) {
      msg = format(logger, method, msg);
      logger.trace(msg);
    }
  }

  /**
   * Write a message to the log at trace level when entering a static method.
   *
   * @param method  the method name.
   * @param logger  the logger used to write this log entry.
   */
  public static void enter(Logger logger, String method)
  {
    if ( logger != null && logger.isTraceEnabled() ) {
      String msg = format(logger, method, "enter");
      logger.trace(msg);
    }
  }

  /**
   * Write a message to the log at trace level when leaving a static method.
   *
   * @param logger  the logger used to write this log entry.
   * @param method  the method name.
   */
  public static void leave(Logger logger, String method)
  {
    if ( logger.isTraceEnabled() ) {
      String msg = format(logger, method, "leave");
      logger.trace(msg);
    }
  }

  /**
   * Write a message to the log at trace level when leaving a static method.
   *
   * @param logger  the logger used to write this log entry.
   * @param method  the method name.
   * @param result  the method's return value.
   */
  public static void leave(Logger logger, String method, Object result)
  {
    if ( logger != null && logger.isTraceEnabled() ) {
      String s = result == null ? "null" : result.toString();
      String msg = formatLeave(logger, method, s);
      logger.trace(msg);
    }
  }

  /**
   * Write a message to the log at trace level when leaving a static method.
   *
   * @param logger  the logger used to write this log entry.
   * @param method  the method name.
   * @param result  the method's return value.
   */
  public static void leave(Logger logger, String method, boolean result)
  {
    if ( logger != null && logger.isTraceEnabled() ) {
      String msg = formatLeave(logger, method, String.valueOf(result));
      logger.trace(msg);
    }
  }

  /**
   * Add the class and method name prefix to a string buffer.
   *
   * @param sb      the string builder for building the message.
   * @param logger  the logger used to write this log entry.
   * @param method  the name of the method being traced.
   */
  private static void addPrefix(StringBuilder sb, Logger logger, String method)
  {
    String s = logger.getName();
    int i = s.lastIndexOf('.');
    s = i >= 0 ? s.substring(i + 1) : s;
    sb.append(s);
    sb.append(".");
    sb.append(method == null ? "<unknown>" : method);
    sb.append(": ");
  }

  /**
   * Returns a formatted message with the class and method name inserted.
   *
   * @param logger  the logger used to write this log entry.
   * @param method  the name of the method being traced.
   * @param message  the message to be included in the log entry.
   * @return a formatted message.
   */
  private static String format(Logger logger, String method, String message)
  {
    String s = "";
    if ( logger != null ) {
      StringBuilder sb = new StringBuilder();
      addPrefix(sb, logger, method);
      sb.append(message);
      s = sb.toString();
    }
    return s;
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
  private static String formatLeave(Logger logger, String method, String result)
  {
    String s = "";
    if ( logger != null ) {
      StringBuilder sb = new StringBuilder(); 
      addPrefix(sb, logger, method);
      sb.append(leaveMsg);
      sb.append(result == null ? "<null>" : result);
      sb.append("]");
      s = sb.toString();
    }
    return s;
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
  public static String format(Logger logger, String method, String format,
                              Object... params)
  {
    String s = "";
    if ( logger != null ) {
      StringBuilder sb = new StringBuilder();
      addPrefix(sb, logger, method);
      Formatter formatter = new Formatter(sb);
      formatter.format(format, params);
      formatter.close();
      s = sb.toString();
    }
    return s;
  }

  public final static String leaveMsg = "leave, return value = [";
}
