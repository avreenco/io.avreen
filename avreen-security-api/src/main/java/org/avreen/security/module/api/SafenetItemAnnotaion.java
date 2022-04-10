package org.avreen.security.module.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The interface Safenet item.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SafenetItemAnnotaion {
    /**
     * Order int.
     *
     * @return the int
     */
    int order();

    /**
     * Type safenet field type.
     *
     * @return the safenet field type
     */
    SafenetFieldType type();

    /**
     * Length int.
     *
     * @return the int
     */
// for variable length
    int length() default -1;
}

