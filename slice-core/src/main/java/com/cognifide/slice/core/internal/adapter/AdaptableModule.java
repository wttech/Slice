package com.cognifide.slice.core.internal.adapter;

import java.util.Collection;

/**
 * Modules implementing this interface may expose a list of classes which will be available as the Sling
 * Adapters (where adaptable is Resource).
 * 
 * @author Tomasz Rękawek
 * 
 */
public interface AdaptableModule {
	/**
	 * Return collection of adapter classes.
	 */
	Collection<Class<?>> getClasses();
}
