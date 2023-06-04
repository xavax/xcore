package com.xavax.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Documents the type of elements in collections and maps.
 */   
@Documented
@Inherited
@Repeatable(Types.class)
@Retention(RUNTIME)
@Target(FIELD)
public @interface Type {
  Class<?> value() default Object.class;
}
