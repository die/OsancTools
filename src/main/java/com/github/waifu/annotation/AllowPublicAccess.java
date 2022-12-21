package com.github.waifu.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Bypasses private access restriction by linter.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface AllowPublicAccess {
}
