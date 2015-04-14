package com.cognifide.slice.test.setup

import com.citytechinc.aem.prosper.specs.ProsperSpec
import com.cognifide.slice.api.context.ConstantContextProvider
import com.cognifide.slice.api.context.Context
import com.cognifide.slice.api.context.ContextFactory
import com.cognifide.slice.api.context.ContextScope
import com.cognifide.slice.api.provider.ModelProvider
import com.cognifide.slice.core.internal.context.SliceContextScope
import com.cognifide.slice.core.internal.module.JcrModule
import com.cognifide.slice.core.internal.module.SliceModule
import com.cognifide.slice.core.internal.module.SliceResourceModule
import com.cognifide.slice.core.internal.module.SlingModule
import com.cognifide.slice.mapper.module.MapperModule
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Module
import spock.lang.Shared;

/**
 * @author Mariusz Kubiś
 * Date: 10.04.15
 */


class BaseSetup extends ProsperSpec {

    @Shared
    protected Injector injector

    @Shared
    protected ModelProvider modelProvider

    def setup() {
        ContextScope contextScope = new SliceContextScope()
        List<Module> modules = new ArrayList<Module>()
        modules.add(new SliceModule(contextScope, null))
        modules.add(new SlingModule(contextScope))
        modules.add(new JcrModule())
        modules.add(new MapperModule())
        modules.add(new SliceResourceModule())

        injector = Guice.createInjector(modules)

        ContextFactory factory = injector.getInstance(ContextFactory.class)
        Context context = factory.getResourceResolverContext(resourceResolver)
        contextScope.setContextProvider(new ConstantContextProvider(context))

        modelProvider = injector.getInstance(ModelProvider.class)
    }
}
