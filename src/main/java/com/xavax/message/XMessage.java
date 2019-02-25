package com.xavax.message;

/**
 * Message catalog for XCore.
 */
public enum XMessage {
  INVALID_MODIFICATION("Attempted to modify a read-only object of type %s."),
  OUT_OF_RANGE("value %d for %s is not within the range %d and %d"),
  TEST("test message."),
  UNEXPECTED_EXCEPTION("Unexpected exception: %s");

  private final String message;

  /**
   * Construct a message with the specified message text.
   *
   * @param message the message text.
   */
  XMessage(final String message) {
    this.message = message;
  }

  /**
   * Returns the message text.
   *
   * @return the message text.
   */
  public String message() {
    return this.message;
  }

  /**
   * Returns a formatted string using this message as the format.
   *
   * @param args the message parameters.
   * @return a formatted string.
   */
  public String format(final Object... args) {
    return String.format(this.message, args);
  }

  /**
   * Formats a message and writes it to System.out.
   *
   * @param args the message parameters.
   */
  @SuppressWarnings("PMD.SystemPrintln")
  public void print(final Object... args) {
    System.out.println(format(args));
  }

  /**
   * Formats an error message and writes it to System.err.
   *
   * @param args the message parameters.
   */
  @SuppressWarnings("PMD.SystemPrintln")
  public void error(final Object... args) {
    System.err.println(format(args));
  }
}
