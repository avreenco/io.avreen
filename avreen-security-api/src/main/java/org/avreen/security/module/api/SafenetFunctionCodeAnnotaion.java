package org.avreen.security.module.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The interface Safenet function code.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SafenetFunctionCodeAnnotaion {
    /**
     * Code string.
     *
     * @return the string
     */
    String code();
}

