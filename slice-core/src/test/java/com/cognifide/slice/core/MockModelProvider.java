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

package com.cognifide.slice.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.sling.api.resource.Resource;

import com.cognifide.slice.api.provider.ModelProvider;

import org.apache.sling.api.resource.ResourceResolver;

import com.google.inject.Key;

public class MockModelProvider implements ModelProvider {

	private final Map<ClassPathPair, Object> modelStore;

	public MockModelProvider() {
		modelStore = new HashMap<MockModelProvider.ClassPathPair, Object>();
	}

	public <T> MockModelProvider add(Class<T> type, String path, T model) {
		modelStore.put(new ClassPathPair(type, path), model);
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Class<T> type, String path) {
		ClassPathPair key = new ClassPathPair(type, path);
		if (modelStore.containsKey(key)) {
			return (T) modelStore.get(key);
		}
		return null;
	}

	@Override
	public <T> T get(Class<T> type, Resource resource) {
		// TODO
		return null;
	}

	@Override
	public <T> T get(Key<T> key, Resource resource) {
		// TODO
		return null;
	}

	@Override
	public <T> T get(Key<T> key, String path) {
		// TODO
		return null;
	}

	@Override
	public Object get(String className, String path) throws ClassNotFoundException {
		// TODO
		return null;
	}

	@Override
	public Object get(String className, Resource resource) throws ClassNotFoundException {
		// TODO
		return null;
	}

	@Override
	public <T> List<T> getList(Class<T> type, Iterator<String> paths) {
		List<T> result = new ArrayList<T>();
		while (paths.hasNext()) {
			String path = paths.next();
			T model = get(type, path);
			if (model != null) {
				result.add(model);
			}
		}
		return result;
	}

	@Override
	public <T> List<T> getList(Class<T> type, String[] paths) {
		List<T> result = new ArrayList<T>();
		for (String path : paths) {
			T model = get(type, path);
			if (model != null) {
				result.add(model);
			}
		}
		return result;
	}

	@Override
	@SuppressWarnings("null")
	public <T> List<T> getChildModels(Class<T> type, String path) {
		ResourceResolver resolver = null;
		return getChildModels(type, resolver.getResource(path));
	}

	@Override
	public <T> List<T> getChildModels(Class<T> type, Resource resource) {
		final ArrayList<T> result = new ArrayList<T>();
		if (resource != null) {
			Iterator<Resource> listChildren = resource.listChildren();
			while (listChildren.hasNext()) {
				Resource childResource = listChildren.next();
				result.add(get(type, childResource));
			}
		}
		return result;
	}

	@Override
	public <T> List<T> getListFromResources(Class<T> type, Iterator<Resource> resources) {
		List<T> result = new ArrayList<T>();
		while (resources.hasNext()) {
			Resource resource = resources.next();
			result.add(get(type, resource));
		}
		return result;
	}

	private class ClassPathPair {
		private Class<?> type;

		private String path;

		public ClassPathPair(Class<?> type, String path) {
			super();
			this.type = type;
			this.path = path;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((path == null) ? 0 : path.hashCode());
			result = prime * result + ((type == null) ? 0 : type.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ClassPathPair other = (ClassPathPair) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (path == null) {
				if (other.path != null)
					return false;
			} else if (!path.equals(other.path))
				return false;
			if (type == null) {
				if (other.type != null)
					return false;
			} else if (!type.equals(other.type))
				return false;
			return true;
		}

		private MockModelProvider getOuterType() {
			return MockModelProvider.this;
		}
	}

	@Override
	public Object get(Resource resource) throws ClassNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

}
