package com.cognifide.slice.core.internal.injector;

import com.cognifide.slice.api.injector.InjectorConfig;
import com.google.inject.Injector;

/**
 * OSGi services implementing this interface will be informed about events related to the Slice injector
 * lifecycle.
 * 
 * @author Tomasz Rękawek
 * 
 */
public interface InjectorListener {
	/**
	 * Called when Slice injector is successfully created.
	 * 
	 * @param injector Created injector
	 * @param config Configuration of the created injector
	 */
	void injectorCreated(Injector injector, InjectorConfig config);

	/**
	 * Called when Slice injector is destroyed.
	 * 
	 * @param injector Destroyed injector
	 * @param config Configuration of the destroyed injector
	 */
	void injectorDestroyed(Injector injector, InjectorConfig config);
}
