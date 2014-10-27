package com.cognifide.slice.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Jaromir Celejewski
 * @annotation OsgiService
 *
 * This annotation marks OSGi services that should be bound automatically by the Guice injector.
 * It is supposed to be used in conjunction with @Inject annotation, e.g:
 *    @OsgiService
 *    @Inject
 *    AnyOsgiService anyOsgiService;
 *
 * Also @see com.cognifide.slice.core.internal.module.OsgiToGuiceAutoBindModule
 */

@Target({ ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface OsgiService {
}