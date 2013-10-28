package com.cognifide.slice.util;

/*
 * #%L Slice - Core API $Id:$ $HeadURL:$ %% Copyright (C) 2012 Cognifide Limited %% Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License. #L%
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;


/**
 * Util for getting injector name required for given request.
 * 
 * @author rafal.malinowski
 * @author Jan Kuźniak
 */
public final class InjectorNameUtil {

	private static final String JCR_CONTENT_NODE_NAME = "jcr:content";

	private static final String INJECTOR_NAME_PROPERTY = "injectorName";

	private static final Pattern RESOURCE_TYPE_PATTERN = Pattern.compile("(/[^/]+/)?([^/]+)(/.*)?");

	private InjectorNameUtil() {
		// hiding util class constructor
	}

	/**
	 * Gets injector name for given request. First a CRX tree is searched from current resource to top one.
	 * First found injectorName is returned. For each resource subresource jcr:content is also checked.
	 * 
	 * If no injectorName property is found then name is fetched from current resource type. It is name of
	 * path item directly after apps. For /apps/slice/... it will return slice.
	 * 
	 * @deprecated use {@link com.cognifide.slice.api.injector.InjectorsRepository#getInjectorForResource(Resource)} instead
	 */
	@Deprecated
	public static String getFromRequest(final SlingHttpServletRequest request) {
		if (null != request.getResource()) {
			return getFromResourceType(request.getResource().getResourceType());
		}

		return StringUtils.EMPTY;
	}

	public static String getForResource(Resource resource) {
		String injectorName = null;
		if (resource != null) {
			injectorName = getFromContentTree(resource);
			if (injectorName == null) {
				injectorName = getFromResourceType(resource.getResourceType());
			}
		}
		return injectorName;
	}

	public static String getFromContentTree(Resource resource) {
		String injectorName = null;
		Resource currentResource = resource;
		while ((null == injectorName) && (null != currentResource)) {
			injectorName = getFromContentNode(currentResource);
			currentResource = currentResource.getParent();
		}
		return injectorName;
	}

	private static String getFromContentNode(Resource resource) {
		String injectorName;
		injectorName = getInjectorNameProperty(resource);
		if (null == injectorName) {
			Resource contentResource = resource.getChild(JCR_CONTENT_NODE_NAME);
			if (null != contentResource) {
				injectorName = getInjectorNameProperty(contentResource);
			}
		}
		return injectorName;
	}

	public static String getInjectorNameProperty(Resource resource) {
		String injectorName = null;
		final ValueMap properties = resource.adaptTo(ValueMap.class);
		if (null != properties) {
			injectorName = properties.get(INJECTOR_NAME_PROPERTY, String.class);
		}
		return injectorName;
	}

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
