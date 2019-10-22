//
// Copyright 2019 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//

package com.xavax.logging;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import static org.apache.log4j.Level.*;

/**
 * Log4jLoggingManager is a LoggingManager that uses Log4J.
 */
public final class Log4jLoggingManager extends LoggingManager {
  private final static Map<org.apache.log4j.Level, String> levelToNameMap = new HashMap<>();
  private final static Map<String, org.apache.log4j.Level> nameToLevelMap = new HashMap<>();

  static {
    levelToNameMap.put(OFF, OFF_NAME);
    levelToNameMap.put(FATAL, FATAL_NAME);
    levelToNameMap.put(ERROR, ERROR_NAME);
    levelToNameMap.put(WARN, WARN_NAME);
    levelToNameMap.put(INFO, INFO_NAME);
    levelToNameMap.put(DEBUG, DEBUG_NAME);
    levelToNameMap.put(TRACE, TRACE_NAME);
    nameToLevelMap.put(OFF_NAME, OFF);
    nameToLevelMap.put(FATAL_NAME, FATAL);
    nameToLevelMap.put(ERROR_NAME, ERROR);
    nameToLevelMap.put(WARN_NAME, WARN);
    nameToLevelMap.put(INFO_NAME, INFO);
    nameToLevelMap.put(DEBUG_NAME, DEBUG);
    nameToLevelMap.put(TRACE_NAME, TRACE);
  }

  /**
   * Construct a Log4jLoggingManager
   */
  public Log4jLoggingManager() {
    super();
    setInstance(this);
  }

  /**
   * Returns the native logger class.
   *
   * @return the native logger class.
   */
  public Class<?> getNativeLoggerClass() {
    return Logger.class;
  }

  /**
   * Returns the logging level for the named logger.
   *
   * @param loggerName  the logger name.
   * @return the logging level.
   */
  @Override
  public <T> String getLevelName(final T object) {
    String result = null;
    if ( object instanceof Category ) {
      Category category = (Category) object;
      org.apache.log4j.Level level = category.getLevel();
      while (level == null) {
	category = category.getParent();
	level = category.getLevel();
      }
      result = levelToNameMap.get(level);
    }
    return result;
  }

}
