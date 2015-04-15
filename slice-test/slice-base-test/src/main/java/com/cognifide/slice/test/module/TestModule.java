package com.cognifide.slice.test.module;

import com.cognifide.slice.api.scope.ContextScoped;
import com.cognifide.slice.mapper.api.SliceReferencePathResolver;
import com.cognifide.slice.mapper.impl.SliceReferencePathResolverImpl;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;

/**
 * @author Mariusz Kubiś Date: 15.04.15
 */
public class TestModule extends AbstractModule {

	@Override
	protected void configure() {

	}

	@Provides
	@ContextScoped
	public SliceReferencePathResolver getSliceReferencePathResolver(Injector injector) {
		return new SliceReferencePathResolverImpl(injector);
	}
}
