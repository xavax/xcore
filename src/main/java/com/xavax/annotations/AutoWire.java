package com.xavax.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static com.xavax.util.Constants.EMPTY_STRING;

/**
 * AutoWire specifies a resource to be injected into a field.
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
@Inherited
public @interface AutoWire {
  Class<?> resource() default Object.class;
  String name() default EMPTY_STRING;
}
