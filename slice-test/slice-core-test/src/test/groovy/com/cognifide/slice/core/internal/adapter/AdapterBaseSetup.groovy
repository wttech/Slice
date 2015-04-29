/*-
 * #%L
 * Slice - Core Tests
 * %%
 * Copyright (C) 2012 Cognifide Limited
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.cognifide.slice.core.internal.adapter

import com.citytechinc.aem.prosper.specs.ProsperSpec
import com.cognifide.slice.api.context.ConstantContextProvider
import com.cognifide.slice.api.context.Context
import com.cognifide.slice.api.context.ContextFactory
import com.cognifide.slice.api.context.ContextProvider
import com.cognifide.slice.api.context.ContextScope
import com.cognifide.slice.api.context.RequestContextProvider
import com.cognifide.slice.api.injector.InjectorConfig
import com.cognifide.slice.api.injector.InjectorRunner
import com.cognifide.slice.api.provider.ModelProvider
import com.cognifide.slice.core.internal.context.SliceContextScope
import com.cognifide.slice.core.internal.injector.InjectorHierarchy
import com.cognifide.slice.core.internal.injector.InjectorRepositoryAdapterFactory
import com.cognifide.slice.core.internal.injector.InjectorsRepositoryService
import com.cognifide.slice.core.internal.module.JcrModule
import com.cognifide.slice.core.internal.module.SliceModule
import com.cognifide.slice.core.internal.module.SliceResourceModule
import com.cognifide.slice.core.internal.module.SlingModule
import com.cognifide.slice.mapper.module.MapperModule
import com.google.inject.Injector
import com.google.inject.Module
import org.apache.sling.api.adapter.AdapterFactory
import org.apache.sling.api.resource.ValueMap
import spock.lang.Shared

import java.lang.reflect.Field
import java.lang.reflect.Method

/**
 * Created by Jaromir Celejewski on 2015-04-23.
 */
class AdapterBaseSetup extends ProsperSpec {

    @Shared
    InjectorHierarchy injectorHierarchy

    @Shared
    InjectorsRepositoryService repositoryService

    @Shared
    SliceAdapterFactory sliceAdapterFactory

    @Shared
    InjectorRepositoryAdapterFactory injectorRepositoryAdapterFactory

    @Shared
    ModelProvider modelProvider

    @Shared
    Injector injector

    @Shared
    ContextScope contextScope

    @Override
    Collection<AdapterFactory> addAdapterFactories() {
        sliceAdapterFactory = new SliceAdapterFactory("slice-test") {
            @Override
            def <AdapterType> AdapterType getAdapter(Object adaptable, Class<AdapterType> type) {
                //A workaround necessary to force code to use Prosper's mock objects instead of real Sling
                if (ValueMap.class.equals(type))
                {
                    return null;
                }
                return super.getAdapter(adaptable, type)
            }
        }

        [sliceAdapterFactory = sliceAdapterFactory, injectorRepositoryAdapterFactory = new InjectorRepositoryAdapterFactory()]
    }

    protected void injectPrivateField(Object targetObject, Object objectToInject, String fieldName) {
        if (targetObject != null) {
            Field f = targetObject.getClass().getDeclaredField(fieldName)
            f.setAccessible(true)
            f.set(targetObject, objectToInject)
        }
    }

    protected RequestContextProvider createRequestContextProvider() {
        final ContextScope scope = contextScope;
        return new RequestContextProvider() {
            @Override
            ContextProvider getContextProvider(String injectorName) {
                scope.getContextProvider()
            }
        }
    }

    def setup() {

        contextScope = new SliceContextScope()
        List<Module> modules = new ArrayList<Module>()
        modules.add(new SliceModule(contextScope, null))
        modules.add(new SlingModule(contextScope))
        modules.add(new JcrModule())
        modules.add(new MapperModule())
        modules.add(new SliceResourceModule())

        //Preparing injector configuration
        final InjectorRunner injectorRunner = new InjectorRunner(null,
                "slice-test",
                "slice-test-app.*",
                "com.cognifide.example")

        injectorRunner.installModules(modules)
        InjectorConfig config = new InjectorConfig(injectorRunner)

        //creation of InjectorsRepositoryService with InjectorHierarchy
        repositoryService = new InjectorsRepositoryService()
        injectorHierarchy = new InjectorHierarchy()
        injectPrivateField(repositoryService, injectorHierarchy, "injectors")

        //creation and registration of injectors in the hierarchy
        Method bindConfigMethod = injectorHierarchy.getClass().getDeclaredMethod("bindConfig", InjectorConfig.class)
        bindConfigMethod.setAccessible(true)
        bindConfigMethod.invoke(injectorHierarchy, config)

        injectPrivateField(injectorRepositoryAdapterFactory, repositoryService, "repository")

        injector = repositoryService.getInjector("slice-test").getInjector()

        ContextFactory factory = injector.getInstance(ContextFactory.class)
        Context context = factory.getResourceResolverContext(resourceResolver)
        contextScope.setContextProvider(new ConstantContextProvider(context))

        modelProvider = injector.getInstance(ModelProvider.class)
    }

}
