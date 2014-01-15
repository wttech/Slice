package com.cognifide.slice.core.internal.module;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.resource.Resource;

import com.cognifide.slice.api.model.InitializableModel;
import com.cognifide.slice.core.internal.provider.CurrentResourceProvider;
import com.cognifide.slice.mapper.annotation.SliceResource;
import com.cognifide.slice.mapper.api.Mapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

/**
 * This module creates an InjectorListener that postprocesses every injection of a {@code @SliceResource},
 * maps its {@code @JcrProperty} fields and invokes {@code afterCreated()} method.
 * 
 * @author Tomasz Rękawek
 * 
 */
public class SliceResourceModule extends AbstractModule {

	private final SliceResourceAnnotationCache annotationCache = new SliceResourceAnnotationCache();

	@Override
	protected void configure() {
		final Provider<CurrentResourceProvider> currentResourceProvider = getProvider(CurrentResourceProvider.class);
		final Provider<Mapper> mapperProvider = getProvider(Mapper.class);

		// listener that checks if given injectee is annotated with @SliceResource
		AbstractMatcher<TypeLiteral<?>> matcher = new AbstractMatcher<TypeLiteral<?>>() {
			@Override
			public boolean matches(TypeLiteral<?> t) {
				return annotationCache.annotationPresent(t.getRawType());
			}
		};
		// register SliceResourceTypeListener for classes matched with matcher
		TypeListener listener = new TypeListener() {
			@Override
			public <I> void hear(final TypeLiteral<I> type, final TypeEncounter<I> encounter) {
				encounter.register(new SliceResourceTypeListener(currentResourceProvider, mapperProvider));
			}
		};
		bindListener(matcher, listener);
	}

	private static class SliceResourceTypeListener implements InjectionListener<Object> {

		private final Provider<CurrentResourceProvider> currentResourceProvider;

		private final Provider<Mapper> mapperProvider;

		public SliceResourceTypeListener(Provider<CurrentResourceProvider> currentResourceProvider,
				Provider<Mapper> mapperProvider) {
			this.currentResourceProvider = currentResourceProvider;
			this.mapperProvider = mapperProvider;
		}

		@Override
		public void afterInjection(Object injectee) {
			Resource resource = currentResourceProvider.get().get();
			if (resource == null) {
				return;
			}
			Mapper mapper = mapperProvider.get();
			mapper.get(resource, injectee);
			if (injectee instanceof InitializableModel) {
				((InitializableModel) injectee).afterCreated();
			}
		}
	}

	private static class SliceResourceAnnotationCache {
		private Map<Class<?>, Boolean> cache = new HashMap<Class<?>, Boolean>();

		public boolean annotationPresent(Class<?> clazz) {
			if (!cache.containsKey(clazz)) {
				cache.put(clazz, clazz.isAnnotationPresent(SliceResource.class));
			}
			return cache.get(clazz);
		}
	}
}
