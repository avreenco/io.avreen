package io.avreen.common.jmx;

/**
 * Created by asgharnejad on 3/18/2019.
 */

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;

/**
 * The interface Jmx operation param annotaion.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({PARAMETER})
public @interface JmxOperationParamAnnotaion {
    /**
     * Value string.
     *
     * @return the string
     */
    String value();
}