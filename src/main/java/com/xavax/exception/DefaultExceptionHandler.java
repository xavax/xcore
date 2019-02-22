package com.xavax.exception;

import com.xavax.message.XMessage;

/**
 * DefaultExceptionHandler is the default exception handler for XCore.
 */
public class DefaultExceptionHandler implements ExceptionHandler {

  /**
   * Handle an exception.
   *
   * @param cause    the exception or cause of the exception.
   * @param message  the exception message.
   * @param args     optional message parameters.
   */
  @Override
  @SuppressWarnings({
    "PMD.SystemPrintln",
    "PMD.AvoidPrintStackTrace"
  })
  public void handle(final Throwable cause, final XMessage message, final Object... args) {
    System.err.println(message.format(args));
    if ( cause != null ) {
      cause.printStackTrace();
    }
  }

}
