package io.avreen.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The interface Safenet function code.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuditAbleAnnotaion {
    /**
     * Code string.
     *
     * @return the string
     */
    String value() default "";
}

