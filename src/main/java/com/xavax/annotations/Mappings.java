package com.xavax.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Mappings is the container for Mapping annotations.
 */
@Documented
@Inherited
@Retention(RUNTIME)
@Target(FIELD)
public @interface Mappings {
  Mapping[] value() default {};
}
