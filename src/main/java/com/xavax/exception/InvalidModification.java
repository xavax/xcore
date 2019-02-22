package com.xavax.exception;

import static com.xavax.message.XMessage.INVALID_MODIFICATION;

/**
 * InvalidModification is thrown when an attempt is made to
 * modify a read-only resource.
 */
public class InvalidModification extends XRuntimeException {
  private final static long serialVersionUID = 1L;

  /**
   * Construct a InvalidModification exception.
   *
   * @param typeName  the type name of the resource.
   */
  public InvalidModification(final String typeName) {
    super(INVALID_MODIFICATION, typeName);
  }
}
