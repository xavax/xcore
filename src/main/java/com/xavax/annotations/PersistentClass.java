package com.xavax.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static com.xavax.util.Constants.EMPTY_STRING;

/**
 * PersistentClass describes a persistent class.
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
@Inherited
public @interface PersistentClass {
  //  String collection() default EMPTY_STRING;
  //  String database() default EMPTY_STRING;
  String name() default EMPTY_STRING;
  //  String serializedName() default EMPTY_STRING;
  String strategy() default EMPTY_STRING;
  // String table() default EMPTY_STRING;
  String variant() default EMPTY_STRING;
  String variantEnum() default EMPTY_STRING;
  String variantField() default EMPTY_STRING;
}
