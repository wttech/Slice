/*-
 * #%L
 * Slice - Core
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

package com.cognifide.slice.core.internal.injector;

import java.util.Collection;

import com.cognifide.slice.api.injector.InjectorConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.framework.Constants;

import com.cognifide.slice.api.injector.InjectorWithContext;
import com.cognifide.slice.api.injector.InjectorsRepository;
import com.google.inject.Injector;

/**
 * @author Witold Szczerba
 * @author Rafał Malinowski
 * @author Jan Kuźniak
 * @class InjectorsRepositoryService
 */
@Component(immediate = true)
@Service
// @formatter:off
@Properties({ @Property(name = Constants.SERVICE_DESCRIPTION, value = "Repository of all Slice injectors."),
		@Property(name = Constants.SERVICE_VENDOR, value = "Cognifide") })
// @formatter:on
public final class InjectorsRepositoryService implements InjectorsRepository {

	@Reference
	private ResourceResolverFactory resourceResolverFactory;

	@Reference
	private InjectorHierarchy injectors;

	@Override
	public InjectorWithContext getInjector(final String injectorName) {
		InjectorWithContextImpl injectorWithContext = null;
		Injector injector = injectors.getInjectorByName(injectorName);
		if (injector != null) {
			injectorWithContext = new InjectorWithContextImpl(injector);
		}
		return injectorWithContext;
	}

	@Override
	public String getInjectorName(Injector injector) {
		return injectors.getRegisteredName(injector);
	}

	@Override
	public Collection<String> getInjectorNames() {
		return injectors.getInjectorNames();
	}

	/**
	 * Provides proper injector name for given resource path
	 * Current implementation finds an injector using "best match" approach, ie:
	 * if we have two injector, one with path "/apps/appname/" and second with path "/apps/appname/x"
	 * then for resources "appname/x/..." it will return injector "/apps/appname/x" and for resource
	 * "appname/y/..." it will return injector with path "/apps/appname/"
	 */
	@Override
	public String getInjectorNameByPath(final String resourcePath)
	{
		String injectorName = null;
		String iteratedPath = InjectorConfig.DEFAULT_INJECTOR_PATH + resourcePath;
		while (!iteratedPath.isEmpty())
		{
			injectorName = injectors.getInjectorNameByPath(iteratedPath);
			if (StringUtils.isEmpty(injectorName))
			{
				injectorName = injectors.getInjectorNameByPath(iteratedPath+"/");
			}
			if (!StringUtils.isEmpty(injectorName)) {
				break;
			}
			iteratedPath = getParentPath(iteratedPath);
		}
		return injectorName;
	}

	private String getParentPath(String iteratedPath) {
		int lastIndexOfSlash = iteratedPath.lastIndexOf('/');
		if (lastIndexOfSlash != -1) {
			return iteratedPath.substring(0, iteratedPath.lastIndexOf('/'));
		}
		return StringUtils.EMPTY;
	}
}
