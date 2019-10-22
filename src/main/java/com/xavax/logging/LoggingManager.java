//
// Copyright 2019 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//

package com.xavax.logging;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xavax.util.AbstractJoinableObject;
import com.xavax.util.Joiner;

import static com.xavax.message.XMessage.UNEXPECTED_EXCEPTION;

/**
 * LoggingManager is the abstract base class for logging managers.
 */
public abstract class LoggingManager extends AbstractJoinableObject {
  protected final static String OFF_NAME = "OFF";
  protected final static String FATAL_NAME = "FATAL";
  protected final static String ERROR_NAME = "ERROR";
  protected final static String WARN_NAME = "WARN";
  protected final static String INFO_NAME = "INFO";
  protected final static String DEBUG_NAME = "DEBUG";
  protected final static String TRACE_NAME = "TRACE";

  private final static String LOGGER_FIELD = "logger";
  private final static Logger localLogger =
      LoggerFactory.getLogger(LoggingManager.class);

  private static LoggingManager manager;

  private final Field loggerField;

  /**
   * Construct the LoggingManager.
   */
  protected LoggingManager() {
    super();
    loggerField = getLoggerField();
  }

  /**
   * Returns the native logger class.
   *
   * @return the native logger class.
   */
  public abstract Class<?> getNativeLoggerClass();

  /**
   * Returns the level name for a native logger.
   *
   * @param nativeLogger  the native logger.
   * @return the level name for a native logger.
   */
  protected abstract <T> String getLevelName(T nativeLogger);

  /**
   * Returns the logging level for the named logger.
   *
   * @param logger  the logger.
   * @return the logging level.
   */
  // public abstract Level getLevel(final Logger logger);
  public Level getLevel(final Logger logger) {
    return getLevel(logger.getName());
  }

  /**
   * Returns the logging level for the named logger.
   *
   * @param loggerName  the logger name.
   * @return the logging level.
   */
  protected <T> Level getLevel(final String loggerName) {
    @SuppressWarnings("unchecked")
    final T nativeLogger =
    	(T) getNativeLogger(loggerName, getNativeLoggerClass());
    final String levelName = getLevelName(nativeLogger);
    return levelName == null ? Level.OFF : Level.valueOf(levelName);
  }

  /**
   * Set the level for a logger.
   *
   * @param logger  the SLF4J logger.
   * @param level   the level (com.xavax.logging.Level)
   */
  public <T>  void setLevel(final Logger logger, final Level level) {
    @SuppressWarnings("unchecked")
    final T nativeLogger =
    	(T) getNativeLogger(logger.getName(), getNativeLoggerClass());
    final String levelName = level.name();
    setLevel(nativeLogger, levelName);
  }

  /**
   * Set the level for a logger.
   *
   * @param nativeLogger  the native logger.
   * @param levelName     the level name.
   */
  protected abstract <T> void setLevel(final T nativeLogger, final String levelName);

  /**
   * Returns the native logger for an SLF4J logger.
   *
   * @param loggerName         the logger name.
   * @param nativeLoggerClass  the native logger class.
   * @return the native logger.
   */
  <T> T getNativeLogger(final String loggerName,
			final Class<T> nativeLoggerClass) {
    final Logger logger = LoggerFactory.getLogger(loggerName);
    return getNativeLogger(logger, nativeLoggerClass);
  }

  /**
   * Returns the native logger for an SLF4J logger.
   *
   * @param logger             the SLF4J logger.
   * @param nativeLoggerClass  the native logger class.
   * @return the native logger.
   */
  <T> T getNativeLogger(final Logger logger, final Class<T> nativeLoggerClass) {
    T result = null;
    try {
      result = nativeLoggerClass.cast(loggerField.get(logger));
    }
    catch (IllegalArgumentException | IllegalAccessException e) {
      localLogger.error(UNEXPECTED_EXCEPTION.message(), e);
    }
    return result;
  }

  /**
   * Returns the native logger Field for an SLF4J Logger.
   *
   * @return the native logger Field.
   */
  final Field getLoggerField() {
    Field result = null;
    final Field fields[] = localLogger.getClass().getDeclaredFields();
    for ( final Field field : fields ) {
      final String fieldName = field.getName();
      if ( LOGGER_FIELD.equals(fieldName) ) {
	field.setAccessible(true);
	result = field;
	break;
      }
    }
    return result;
  }

  /**
   * Sets the singleton instance.
   *
   * @param manager the new manager.
   */
  protected void setInstance(final LoggingManager theManager) {
    if ( manager == null ) {
      manager = theManager;
    }
  }

  /**
   * Returns the singleton instance of a LoggingManager.
   *
   * @return the logging manager.
   */
  public static LoggingManager getInstance() {
    synchronized ( localLogger ) {
      if ( manager == null ) {
	manager = new Log4jLoggingManager();
      }
    }
    return manager;
  }

  /**
   * Returns a Logger for a class.
   *
   * @param clazz  the class.
   * @return a Logger for a class.
   */
  public static Logger getLogger(final Class<?> clazz) {
    return LoggerFactory.getLogger(clazz);
  }

  /**
   * Returns a Logger for a class name.
   *
   * @param name  the class name.
   * @return a Logger for a class name.
   */
  public static Logger getLogger(final String name) {
    return LoggerFactory.getLogger(name);
  }

  /**
   * Output this object to the specified joiner.
   *
   * @param joiner  the joiner to use for output.
   * @return this joiner.
   */
  @Override
  protected Joiner doJoin(final Joiner joiner) {
    return joiner;
  }
}
