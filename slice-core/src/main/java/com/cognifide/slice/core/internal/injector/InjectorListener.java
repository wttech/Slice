package com.cognifide.slice.core.internal.injector;

import com.cognifide.slice.api.injector.InjectorConfig;
import com.google.inject.Injector;

public interface InjectorListener {
	void injectorCreated(Injector injector, InjectorConfig config);

	void injectorDestroyed(Injector injector, InjectorConfig config);
}
