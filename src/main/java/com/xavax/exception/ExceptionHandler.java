package com.xavax.exception;

import com.xavax.message.XMessage;

/**
 * ExceptionHandler defines the interface for XCore exception handlers.
 */
public interface ExceptionHandler {
  /**
   * Handle an exception.
   *
   * @param cause    the exception or cause of the exception.
   * @param message  the exception message.
   * @param args     optional message parameters.
   */
  void handle(final Throwable cause,
              final XMessage message,
              final Object... args);
}
