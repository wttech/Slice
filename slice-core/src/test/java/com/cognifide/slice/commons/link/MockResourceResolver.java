package com.cognifide.slice.commons.link;

/*
 * #%L
 * Slice - Core
 * $Id:$
 * $HeadURL:$
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

public class MockResourceResolver implements ResourceResolver {

	private List<String> resources;

	public MockResourceResolver(String... resources) {
		this.resources = new ArrayList<String>();
		Collections.addAll(this.resources, resources);
	}

	@Override
	public Resource resolve(HttpServletRequest request, String absPath) {
		return null;
	}

	@Override
	public Resource resolve(String absPath) {
		return null;
	}

	@Override
	public Resource resolve(HttpServletRequest request) {
		return null;
	}

	@Override
	public String map(String resourcePath) {
		return null;
	}

	@Override
	public String map(HttpServletRequest request, String resourcePath) {
		return null;
	}

	@Override
	public Resource getResource(String path) {
		for (String resource : this.resources) {
			if (StringUtils.equals(resource, path)) {
				return new MockResource(this, resource, "app/test");
			}
		}
		return null;
	}

	@Override
	public Resource getResource(Resource base, String path) {
		return null;
	}

	@Override
	public String[] getSearchPath() {
		return new String[0];
	}

	@Override
	public Iterator<Resource> listChildren(Resource parent) {
		return null;
	}

	@Override
	public Iterator<Resource> findResources(String query, String language) {
		return null;
	}

	@Override
	public Iterator<Map<String, Object>> queryResources(String query, String language) {
		return null;
	}

	@Override
	public ResourceResolver clone(Map<String, Object> authenticationInfo) throws LoginException {
		return null;
	}

	@Override
	public boolean isLive() {
		return false;
	}

	@Override
	public void close() {

	}

	@Override
	public String getUserID() {
		return null;
	}

	@Override
	public Iterator<String> getAttributeNames() {
		return null;
	}

	@Override
	public Object getAttribute(String name) {
		return null;
	}

	@Override
	public <AdapterType> AdapterType adaptTo(Class<AdapterType> type) {
		return null;
	}

}
