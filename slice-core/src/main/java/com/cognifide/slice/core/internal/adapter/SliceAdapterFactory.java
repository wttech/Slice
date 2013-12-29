package com.cognifide.slice.core.internal.adapter;

import org.apache.sling.api.adapter.AdapterFactory;
import org.apache.sling.api.resource.Resource;

import com.cognifide.slice.api.context.Context;
import com.cognifide.slice.api.context.ContextFactory;
import com.cognifide.slice.api.context.ContextProvider;
import com.cognifide.slice.api.context.RequestContextProvider;
import com.cognifide.slice.api.injector.InjectorWithContext;
import com.cognifide.slice.api.injector.InjectorsRepository;
import com.cognifide.slice.api.provider.ModelProvider;
import com.cognifide.slice.util.SliceUtil.ConstantContextProvider;

public class SliceAdapterFactory implements AdapterFactory {

	private final InjectorsRepository repository;

	private final String injectorName;

	private final RequestContextProvider requestContextProvider;

	public SliceAdapterFactory(String injectorName, InjectorsRepository repository,
			RequestContextProvider requestContextProvider) {
		this.repository = repository;
		this.requestContextProvider = requestContextProvider;
		this.injectorName = injectorName;
	}

	@Override
	public <AdapterType> AdapterType getAdapter(Object adaptable, Class<AdapterType> type) {
		if (!(adaptable instanceof Resource)) {
			return null;
		}
		Resource resource = (Resource) adaptable;

		InjectorWithContext injector = getInjector(resource);
		try {
			ModelProvider modelProvider = injector.getInstance(ModelProvider.class);
			return modelProvider.get(type, resource);
		} finally {
			injector.popContextProvider();
		}
	}

	private InjectorWithContext getInjector(Resource resource) {
		InjectorWithContext injector = repository.getInjector(injectorName);
		ContextProvider contextProvider = requestContextProvider.getContextProvider(injectorName);
		if (contextProvider == null) {
			ContextFactory factory = injector.getInstance(ContextFactory.class);
			Context context = factory.getResourceResolverContext(resource.getResourceResolver());
			contextProvider = new ConstantContextProvider(context);
		}
		injector.pushContextProvider(contextProvider);
		return injector;
	}

}
