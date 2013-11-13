package com.cognifide.slice.commons.provider;

import java.util.HashMap;
import java.util.Map;

import com.cognifide.slice.api.qualifier.EmptyObject;
import com.google.inject.Key;
import com.google.inject.Singleton;

@Singleton
public class KeyCache {
	private final Map<Class<?>, Key<?>> cache;

	public KeyCache() {
		cache = new HashMap<Class<?>, Key<?>>();
	}

	@SuppressWarnings("unchecked")
	public <T> Key<T> getKey(final Class<T> clazz) {
		Key<T> key = (Key<T>) cache.get(clazz);
		if (key == null) {
			key = Key.get(clazz, EmptyObject.class);
			cache.put(clazz, key);
		}
		return key;
	}
}
