package com.xavax.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static com.xavax.util.Constants.EMPTY_STRING;

/**
 * PersistentField describes a field of a persistent class.
 */
@Documented
@Inherited
@Retention(RUNTIME)
@Target(FIELD)
public @interface PersistentField {
  /**
   * Returns the name of this field, or an empty string to
   * default to the native name.
   *
   * @return the name of this field.
   */
  String name() default EMPTY_STRING;

  /**
   * Returns the position within the output (0 = first) or -1
   * to indicate the default order.
   *
   * @return the position within the output
   */
  int position() default -1;

  /**
   * Returns true if this field is transient and should not
   * be persisted.
   *
   * @return true if this field is transient
   */
  boolean isTransient() default false;
}
