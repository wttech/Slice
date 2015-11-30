package com.cognifide.slice.mapper.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Method annotated by PostMapping is triggered before resource mapping.
 * If there is more than one such method, only the first one will be invoked.
 *
 * @author Krzysztof Watral
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PostMapping {
}
