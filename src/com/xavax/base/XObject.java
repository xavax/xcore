//
// Copyright 2007 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//

package com.xavax.base;

import java.util.Formatter;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.xavax.logger.XLogger;

/**
 * XObject (eXtended Object) is an abstract base class for objects that
 * use a Logger to trace program flow.
 */
abstract public class XObject {
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
  protected XObject(Logger logger)
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
    String s = getClass().getSimpleName() + ".";
    this.prefix = s.intern();		
  }

  /**
   * Returns true if debug is enabled.
   */
  public boolean debugEnabled()
  {
    boolean result = logger != null ? logger.isDebugEnabled() : false;
    return result;
  }

  /**
   * Write a message to the log at debug level.
   *
   * @param method  the method name.
   * @param msg     the message to display.
   */
  public void debug(String method, String msg)
  {
    if ( logger != null && logger.isDebugEnabled() ) {
      msg = format(method, msg);
      logger.debug(msg);
    }
  }

  /**
   * Format and write a message to the log at debug level.
   *
   * @param method  the method name.
   * @param format  the message format string.
   * @param params  the message parameters.
   */
  public void debug(String method, String format, Object... params)
  {
    if ( logger != null && logger.isDebugEnabled() ) {
      String msg = format(method, format, params);
      logger.debug(msg);
    }
  }

  /**
   * Write a message to the log at error level.
   *
   * @param method  the method name.
   * @param msg     the message to be written to the log.
   */
  public void error(String method, String msg)
  {
    if ( logger != null && logger.isEnabledFor(Level.ERROR) ) {
      msg = format(method, msg);
      logger.error(msg);
    }
  }

  /**
   * Format and write a message to the log at error level.
   *
   * @param method  the method name.
   * @param format  the message format string.
   * @param params  the message parameters.
   */
  public void error(String method, String format, Object... params)
  {
    if ( logger != null && logger.isEnabledFor(Level.ERROR) ) {
      String msg = format(method, format, params);
      logger.error(msg);
    }
  }

  /**
   * Write a message to the log at error level.
   *
   * @param method  the method name.
   * @param cause  the cause of the error.
   * @param msg    the message to be written to the log.
   */
  public void error(String method, Throwable cause, String msg)
  {
    if ( logger != null && logger.isEnabledFor(Level.ERROR) ) {
      msg = format(method, msg);
      logger.error(msg, cause);
    }
  }

  /**
   * Format and write a message to the log at error level.
   *
   * @param method  the method name.
   * @param msg    the message to be written to the log.
   * @param format  the message format string.
   * @param params  the message parameters.
   */
  public void error(String method, Throwable cause, String format,
		    Object... params)
  {
    if ( logger != null && logger.isEnabledFor(Level.ERROR) ) {
      String msg = format(method, format, params);
      logger.error(msg, cause);
    }
  }

  /**
   * Write a message to the log at fatal level.
   *
   * @param method  the method name.
   * @param msg     the message to be written to the log.
   */
  public void fatal(String method, String msg)
  {
    if ( logger != null ) {
      msg = format(method, msg);
      logger.fatal(msg);
    }
  }

  /**
   * Format and write a message to the log at fatal level.
   *
   * @param method  the method name.
   * @param format  the message format string.
   * @param params  the message parameters.
   */
  public void fatal(String method, String format, Object... params)
  {
    if ( logger != null ) {
      String msg = format(method, format, params);
      logger.fatal(msg);
    }
  }

  /**
   * Write a message to the log at fatal level.
   *
   * @param method  the method name.
   * @param cause  the cause of the fatal.
   * @param msg    the message to be written to the log.
   */
  public void fatal(String method, Throwable cause, String msg)
  {
    if ( logger != null ) {
      msg = format(method, msg);
      logger.fatal(msg, cause);
    }
  }

  /**
   * Format and write a message to the log at fatal level.
   *
   * @param method  the method name.
   * @param msg    the message to be written to the log.
   * @param format  the message format string.
   * @param params  the message parameters.
   */
  public void fatal(String method, Throwable cause, String format,
		    Object... params)
  {
    if ( logger != null ) {
      String msg = format(method, format, params);
      logger.fatal(msg, cause);
    }
  }

  /**
   * Write a message to the log at info level.
   *
   * @param method  the method name.
   * @param msg     the message to be written to the log.
   */
  public void info(String method, String msg)
  {
    if ( logger != null && logger.isEnabledFor(Level.INFO) ) {
      msg = format(method, msg);
      logger.info(msg);
    }
  }

  /**
   * Format and write a message to the log at info level.
   *
   * @param method  the method name.
   * @param format  the message format string.
   * @param params  the message parameters.
   */
  public void info(String method, String format, Object... params)
  {
    if ( logger != null && logger.isEnabledFor(Level.INFO) ) {
      String msg = format(method, format, params);
      logger.info(msg);
    }
  }

  /**
   * Write a message to the log at info level.
   *
   * @param method  the method name.
   * @param msg    the message to be written to the log.
   * @param cause  the cause of the info.
   */
  public void info(String method, Throwable cause, String msg)
  {
    if ( logger != null && logger.isEnabledFor(Level.INFO) ) {
      msg = format(method, msg);
      logger.info(msg, cause);
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
  public void info(String method, Throwable cause, String format,
		    Object... params)
  {
    if ( logger != null && logger.isEnabledFor(Level.INFO) ) {
      String msg = format(method, format, params);
      logger.info(msg, cause);
    }
  }

  /**
   * Write a message to the log at warn level.
   *
   * @param method  the method name.
   * @param msg     the message to be written to the log.
   */
  public void warn(String method, String msg)
  {
    if ( logger != null && logger.isEnabledFor(Level.WARN) ) {
      msg = format(method, msg);
      logger.warn(msg);
    }
  }

  /**
   * Format and write a message to the log at warn level.
   *
   * @param method  the method name.
   * @param format  the message format string.
   * @param params  the message parameters.
   */
  public void warn(String method, String format, Object... params)
  {
    if ( logger != null && logger.isEnabledFor(Level.WARN) ) {
      String msg = format(method, format, params);
      logger.warn(msg);
    }
  }

  /**
   * Write a message to the log at warn level.
   *
   * @param method  the method name.
   * @param cause  the cause of the error.
   * @param msg    the message to be written to the log.
   */
  public void warn(String method, Throwable cause, String msg)
  {
    if ( logger != null && logger.isEnabledFor(Level.WARN) ) {
      msg = format(method, msg);
      logger.warn(msg, cause);
    }
  }

  /**
   * Format and write a message to the log at warn level.
   *
   * @param method  the method name.
   * @param msg    the message to be written to the log.
   * @param format  the message format string.
   * @param params  the message parameters.
   */
  public void warn(String method, Throwable cause, String format,
		    Object... params)
  {
    if ( logger != null && logger.isEnabledFor(Level.WARN) ) {
      String msg = format(method, format, params);
      logger.warn(msg, cause);
    }
  }

  /**
   * Returns true if trace is enabled.
   */
  public boolean traceEnabled()
  {
    boolean result = logger != null ? logger.isTraceEnabled() : false;
    return result;
  }

  /**
   * Write a message to the log at trace level.
   *
   * @param method  the method name.
   * @param msg     the message to display.
   */
  public void trace(String method, String msg)
  {
    if ( logger != null && logger.isTraceEnabled() ) {
      msg = format(method, msg);
      logger.trace(msg);
    }
  }

  /**
   * Write a message to the log at trace level when entering a method.
   *
   * @param method  the method name.
   */
  public void enter(String method)
  {
    if ( logger != null && logger.isTraceEnabled() ) {
      String msg = format(method, "enter");
      logger.trace(msg);
    }
  }

  /**
   * Write a message to the log at trace level when leaving a method.
   *
   * @param method  the method name.
   */
  public void leave(String method)
  {
    if ( logger.isTraceEnabled() ) {
      String msg = format(method, "leave");
      logger.trace(msg);
    }
  }

  /**
   * Write a message to the log at trace level when leaving a method.
   *
   * @param method  the method name.
   * @param result  the method's return value.
   */
  public void leave(String method, Object result)
  {
    if ( logger != null && logger.isTraceEnabled() ) {
      String s = result == null ? "null" : result.toString();
      String msg = formatLeave(method, s);
      logger.trace(msg);
    }
  }

  /**
   * Write a message to the log at trace level when leaving a method.
   *
   * @param method  the method name.
   * @param result  the method's return value.
   */
  public void leave(String method, boolean result)
  {
    if ( logger != null && logger.isTraceEnabled() ) {
      String msg = formatLeave(method, String.valueOf(result));
      logger.trace(msg);
    }
  }

  /**
   * Format a message with the class and method name inserted.
   *
   * @param sb       the string builder for building the message.
   * @param method   the name of the method being traced.
   * @param message  the message to be included in the log entry.
   */
  private void format(StringBuilder sb, String method, String message)
  {
    sb.append(method == null ? "<unknown>" : method);
    sb.append(": ");
    if ( message != null ) {
      sb.append(message);
    }
  }

  /**
   * Returns a formatted message with the class and method name inserted.
   *
   * @param method   the name of the method being traced.
   * @param message  the message to be included in the log entry.
   * @return a formatted message.
   */
  private String format(String method, String message)
  {
    StringBuilder sb = new StringBuilder(prefix);
    format(sb, method, message);
    return sb.toString();
  }

  /**
   * Returns a formatted message with the class and method name inserted
   * and the return value of the method.
   *
   * @param method  the name of the method being traced.
   * @param result  the method's return value.
   * @return a formatted message.
   */
  private String formatLeave(String method, String result)
  {
    StringBuilder sb = new StringBuilder(prefix);
    format(sb, method, XLogger.leaveMsg);
    sb.append(result == null ? "<null>" : result);
    sb.append("]");
    return sb.toString();
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
  protected String format(String method, String format, Object... params)
  {
    StringBuilder sb = new StringBuilder(prefix);
    sb.append(method == null ? "<unknown>" : method);
    sb.append(": ");
    Formatter formatter = new Formatter(sb);
    formatter.format(format, params);
    formatter.close();
    return sb.toString();
  }

  protected String prefix;
  protected Logger logger;
}
