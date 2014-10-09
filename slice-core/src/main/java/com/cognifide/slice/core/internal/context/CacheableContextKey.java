package com.cognifide.slice.core.internal.context;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.google.inject.Key;

/**
 * This class presents a wrapper key used in CacheableContext map.
 * 
 * @author kamil.ciecierski
 */

public class CacheableContextKey {

	private static final int INITIAL = 1;

	private static final int MULTIPLIER = 31;

	private final String path;

	private final Key<?> type;

	public CacheableContextKey(final String path, final Key<?> type) {
		this.path = path;
		this.type = type;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder(INITIAL, MULTIPLIER);
		builder.append(path);
		builder.append(type);
		return builder.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		CacheableContextKey other = (CacheableContextKey) obj;
		EqualsBuilder equalsBuilder = new EqualsBuilder();
		equalsBuilder.append(path, other.path);
		equalsBuilder.append(type, other.type);
		return equalsBuilder.isEquals();
	}
}
