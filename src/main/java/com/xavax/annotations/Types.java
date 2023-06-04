package com.xavax.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;

/**
 * Documents the type of elements in collections and maps.
 */
@Documented
@Inherited
@Retention(RUNTIME)
@Target(FIELD)
public @interface Types {
  Type[] value() default {};
}
