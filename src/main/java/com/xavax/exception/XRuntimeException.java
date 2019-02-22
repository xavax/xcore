package com.xavax.exception;

import static com.xavax.message.XMessage.UNEXPECTED_EXCEPTION;
import static com.xavax.util.Constants.EMPTY_ARGS;

import com.xavax.message.XMessage;

/**
 * XRuntimeException is a wrapper for XCore runtime exceptions. It
 * implements standard exception handling.
 * @see com.xavax.exception.XException
 */
public class XRuntimeException extends RuntimeException {
  private final static long serialVersionUID = 1L;

  private final XMessage message;
  private final Object[] args;

  /**
   * Construct an XRuntimeException.
   *
   * @param cause    the cause of this exception.
   * @param message  a message describing this exception.
   * @param args     optional arguments for the message.
   */
  public XRuntimeException(final Throwable cause,
		    final XMessage message,
		    final Object... args) {
    super(cause);
    this.message = message == null ? UNEXPECTED_EXCEPTION : message;
    this.args = args == null ? EMPTY_ARGS : args;
  }

  /**
   * Construct an XRuntimeException.
   *
   * @param cause  the cause of this exception.
   */
  public XRuntimeException(final Throwable cause) {
    this(cause, UNEXPECTED_EXCEPTION, EMPTY_ARGS);
  }

  /**
   * Construct an XRuntimeException.
   *
   * @param message  a message describing this exception.
   * @param args     optional arguments for the message.
   */
  public XRuntimeException(final XMessage message, final Object... args) {
    this(null, message, args);
  }

  /**
   * Construct a default XRuntimeException. This is deprecated because it provides the
   * user with very little information.
   */
  @Deprecated
  public XRuntimeException() {
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
    XException.handle(getCause(), message, args);
  }

}
