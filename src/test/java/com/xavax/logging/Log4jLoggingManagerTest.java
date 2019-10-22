//
// Copyright 2019 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//

package com.xavax.logging;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.testng.Assert.*;

public class Log4jLoggingManagerTest {
  private final static Logger logger =
      LoggerFactory.getLogger(Log4jLoggingManagerTest.class);

  private LoggingManager manager;

  @BeforeMethod
  public void setUp() throws Exception {
    manager = LoggingManager.getInstance();
  }

  @Test
  public void testGetLevel() {
    logger.info("starting testGetLevel");
    assertNotNull(manager);
    final Logger logger1 = LoggingManager.getLogger(getClass());
    assertNotNull(logger1);
    final Logger logger2 = LoggingManager.getLogger(getClass());
    assertNotNull(logger2);
    assertEquals(logger1, logger2);
    Level level1 = manager.getLevel(logger1);
    assertNotNull(level1);
    assertEquals(level1, Level.TRACE);
    Level level2 = manager.getLevel(logger2);
    assertNotNull(level2);
    assertEquals(level1, level2);
  }

  @Test
  public void testSetLevel() {
    logger.info("starting testSetLevel");
    assertNotNull(manager);
    final Logger logger1 = LoggingManager.getLogger(getClass());
    assertNotNull(logger1);
    Level level1 = manager.getLevel(logger1);
    assertNotNull(level1);
    manager.setLevel(logger1, Level.DEBUG);
    level1 = manager.getLevel(logger1);
    assertNotNull(level1);
    assertEquals(level1, Level.DEBUG);
    manager.setLevel(logger1, Level.INFO);
    level1 = manager.getLevel(logger1);
    assertNotNull(level1);
    assertEquals(level1, Level.INFO);
  }
}
