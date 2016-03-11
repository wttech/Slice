/*-
 * #%L
 * Slice - Core API
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
package com.cognifide.slice.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;

import com.cognifide.slice.api.injector.InjectorRunner;
import com.cognifide.slice.api.injector.InjectorsRepository;

/**
 * Util for getting injector name required for given request.
 * 
 * @author rafal.malinowski
 * @author Jan Kuźniak
 * @deprecated since 4.1. User {@link InjectorsRepository} instead
 */
@Deprecated
public final class InjectorNameUtil {

	private static final Pattern RESOURCE_TYPE_PATTERN = Pattern.compile("(/[^/]+/)?([^/]+)(/.*)?");

	private InjectorNameUtil() {
		// hiding util class constructor
	}

	/**
	 * Gets injector name for resource from specified request . It is name of path item directly after apps.
	 * For /apps/slice/... it will return slice.
	 * 
	 * @deprecated use {@link InjectorsRepository#getInjectorNameForResource(String)} instead
	 */
	@Deprecated
	public static String getFromRequest(final SlingHttpServletRequest request) {
		if (null != request.getResource()) {
			return getFromResourceType(request.getResource().getResourceType());
		}
		return StringUtils.EMPTY;
	}

	/**
	 * This util provides injector name for a given resource. The name is read from second part of the
	 * resource type, i.e. part after /apps/. For instance, for /apps/myapp/someresource, it will return
	 * <code>myapp</code> <br>
	 * <br>
	 * Please note that this method is deprecated and it doesn't support injectors registered for a given path
	 * (with use of {@link InjectorRunner#setInjectorPath(String)})
	 * 
	 * @deprecated use {@link InjectorsRepository#getInjectorNameForResource(String)} instead
	 * 
	 * @param resourceType resource type to take the injector name from
	 * @return injector name, always second part of the resource type
	 */
	@Deprecated
	public static String getFromResourceType(String resourceType) {
		String injectorName = null;
		if (StringUtils.isNotEmpty(resourceType)) {
			Pattern pattern = RESOURCE_TYPE_PATTERN;
			Matcher matcher = pattern.matcher(resourceType);
			if (matcher.matches()) {
				injectorName = matcher.group(2);
			}
		}
		return injectorName;
	}
}
