package com.xavax.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static com.xavax.util.Constants.EMPTY_STRING;

/**
 * Mapping describes the mapping between a field and a target
 * such as a database column or a value in a JSON.
 */
@Documented
@Inherited
@Repeatable(Mappings.class)
@Retention(RUNTIME)
@Target(FIELD)
public @interface Mapping {
  /**
   * Returns the name of the channel associated with this mapping.
   *
   * @return the name of the channel.
   */
  String channel() default EMPTY_STRING;

  /**
   * Returns the name of this field in the target.
   *
   * @return the name of this field in the target.
   */
  String name() default EMPTY_STRING;;
 
  /**
   * Returns a list of options as strings.
   *
   * @return a list of options.
   */
  String[] options() default {};

  /**
   * Returns the position of this field in the output,
   * or -1 for a default value.
   *
   * @return the position of this field in the output.
   */
  int position() default -1;
}
