package com.xavax.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Modified annotates a persistent class method that modifies
 * the object. This annotation is used as part of the Automatic
 * strategy for managing the dirty flag.
 */
@Documented
@Retention(RUNTIME)
@Target(METHOD)
public @interface Modifier {
}
